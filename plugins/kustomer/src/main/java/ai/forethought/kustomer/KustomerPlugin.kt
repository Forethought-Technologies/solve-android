package ai.forethought.kustomer

import ai.forethought.core.ForethoughtCore
import ai.forethought.core.ForethoughtHandoffData
import ai.forethought.core.ForethoughtPlugin
import ai.forethought.kustomer.KustomerResumeConversationSetting.PROMPT_IF_OPEN_OR_UNREAD
import ai.forethought.kustomer.KustomerResumeConversationSetting.SHOW_FOR_OPEN_CONVERSATION
import ai.forethought.kustomer.KustomerResumeConversationSetting.SHOW_FOR_UNREAD_MESSAGE_PROMPT_FOR_OPEN
import ai.forethought.plugin.R.string
import android.app.Application
import android.content.Context
import com.kustomer.core.KustomerCore
import com.kustomer.core.models.KusPreferredView
import com.kustomer.core.models.KusWidgetType.CHAT_ONLY
import com.kustomer.core.models.chat.KusConversation
import com.kustomer.core.models.chat.KusCustomerDescribeAttributes
import com.kustomer.core.models.chat.KusEmail
import com.kustomer.ui.Kustomer
import com.kustomer.ui.KustomerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@Suppress("UnusedPrivateMember", "MagicNumber")
/**
 * Plugin to support hand-off requests to the [Kustomer] SDK.
 *
 * @property application android application instance.
 * @property forethought [ForethoughtCore] instance to communicate with the Forethought Solve SDK.
 * @param apiKey Kustomer SDK API Key
 * @param options optionally change Kustomer SDK's Options.
 */
class KustomerPlugin(
    private val application: Application,
    apiKey: String,
    options: KustomerOptions?,
    private val forethought: ForethoughtCore
) : ForethoughtPlugin {

    override var pluginName: String = "kustomer"
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var openConversationCount: Int = 0
    private var latestOpenConversation: KusConversation? = null
    private var latestOpenConversationWithUnread: KusConversation? = null

    /**
     * Can be changed to any value of [KustomerResumeConversationSetting]
     * This will change the behavior of the plugin on hand-off requests, and on start-up.
     */
    var resumeConversation = SHOW_FOR_UNREAD_MESSAGE_PROMPT_FOR_OPEN

    init {
        KustomerCore.init(application = application, apiKey = apiKey, kustomerCoreOptions = null)
        Kustomer.init(
            application, apiKey, options
        ) {
            Timber.d("Kustomer is initialized " + it.dataOrNull)
        }
        requestKustomerConversationData()
    }

    /**
     * Called when forethoughtView is shown.
     *
     * Fetches Kustomer SDK conversations data asynchronously,
     * then decides to show a dialog or open a conversation
     * based on the fetched Kustomer SDK data and the [resumeConversation] value.
     */
    override fun forethoughtViewDidAppear(activityContext: Context) {
        requestKustomerConversationData() {
            when (resumeConversation) {
                SHOW_FOR_UNREAD_MESSAGE_PROMPT_FOR_OPEN -> {
                    latestOpenConversation?.id?.let {
                        showContinueConversationDialog(it)
                    }
                }
                PROMPT_IF_OPEN_OR_UNREAD -> {
                    if (latestOpenConversationWithUnread != null) {
                        latestOpenConversationWithUnread?.id?.let {
                            showContinueConversationDialog(it)
                        }
                    } else if (latestOpenConversation != null) {
                        latestOpenConversation?.id?.let {
                            showContinueConversationDialog(it)
                        }
                    }
                }
            }
        }
    }

    /**
     * Calls [ForethoughtCore.showDialog] method, to show an Android Dialog,
     * where the positive action invokes [continueOpenConversation].
     * @param conversationId which conversation to open on user positive button click.
     */
    private fun showContinueConversationDialog(conversationId: String?) {
        var unreadMessageText = ""
        if (latestOpenConversationWithUnread?.unreadMessageCount ?: 0 > 0) {
            unreadMessageText = application.getString(string.continue_conversation_unread)
        }
        val title = application.getString(string.continue_conversation_title)
        val message = application.getString(string.continue_conversation_message, unreadMessageText)
        val positiveText = application.getString(string.continue_conversation_positive)
        val negativeText = application.getString(string.continue_conversation_negative)
        forethought.showDialog(title, message, positiveText, negativeText, {
            it.dismiss()
            continueOpenConversation(conversationId)
        }, {
            it.dismiss()
        })
    }

    /**
     * If [openConversationCount] is 1, call [Kustomer.openConversationWithId] to show
     * the only open conversation, while updating Kustomer SDK's data asynchronously.
     * If [openConversationCount] is bigger than 1, call [Kustomer.open] to open a
     * list of all chats.
     * @param conversationId opens this conversation if it is the only open one
     */
    private fun continueOpenConversation(conversationId: String?) {
        if (conversationId != null && openConversationCount == 1) {
            Kustomer.getInstance().openConversationWithId(conversationId) {
                it.dataOrNull?.let {
                    requestKustomerConversationData()
                    forethought.hide()
                }
            }
        } else {
            Kustomer.getInstance().open(KusPreferredView.CHAT_ONLY)
            forethought.hide()
        }
    }

    /**
     * Fetches Kustomer SDK's data asynchronously,
     * this method will launch a [Job] on a local [CoroutineScope], then will
     * cancel that job once the data is fetched.
     * @param onFinishMainThread will be invoked on [Dispatchers.Main] before canceling the [Job]
     */
    private fun requestKustomerConversationData(onFinishMainThread: (() -> Unit)? = null) {
        val defaultPage = 0
        val defaultPageSize = 10
        var job: Job? = null
        job = coroutineScope.launch {
            val chatProvider = KustomerCore.getInstance().kusChatProvider()
            val uselessCallToFixWrongCountParsing = chatProvider
                .fetchConversations(defaultPage, defaultPageSize).dataOrNull
            delay(1000)
            val conversations = chatProvider
                .fetchConversations(defaultPage, defaultPageSize).dataOrNull

            val sorted = conversations
                ?.filter { it.isConversationClosed().not() }
                ?.sortedByDescending { it.lastMessageAt }
            openConversationCount = sorted?.size ?: 0
            latestOpenConversationWithUnread = sorted?.firstOrNull { it.unreadMessageCount > 0 }
            latestOpenConversation = sorted?.firstOrNull()
            withContext(Dispatchers.Main) {
                onFinishMainThread?.invoke()
            }
            job?.cancel()
        }
    }

    override fun onWidgetClosed() {
        /* no op */
    }

    /**
     * This will be called when the user requested a hand-off.
     *
     * It will asynchronously update the user's email in [Kustomer.describeCustomer], then will
     * create and open a new conversation.
     * @param handoffData the data passed from Forethought SDK when the user requested a hand-off.
     * @param activityContext the forethought activity context, not needed for this plugin.
     */
    override fun forethoughtHandoffRequested(
        handoffData: ForethoughtHandoffData,
        activityContext: Context
    ) {
        Timber.d("Received kustomer handoff request from the SDK")
        var job: Job? = null
        job = coroutineScope.launch {
            updateCustomerEmail(handoffData)
            createNewKustomerConversation(handoffData)
            job?.cancel()
        }
    }

    /**
     * Fetches Kustomer SDK's data asynchronously, and decides if the Kustomer Plugin should be
     * shown on startup instead of the Forethought Solve UI based on the [resumeConversation] value
     * and Kustomer SDK's data.
     * @return false will show the Forethought Solve UI, true will show the Kustomer UI instead.
     */
    override fun shouldShowPluginOnLaunch(): Boolean {
        requestKustomerConversationData()
        return when {
            (resumeConversation == SHOW_FOR_UNREAD_MESSAGE_PROMPT_FOR_OPEN) &&
                (latestOpenConversationWithUnread != null) -> {
                latestOpenConversationWithUnread?.let { it ->
                    Kustomer.getInstance().openConversationWithId(it.id) { /* no op */ }
                }
                true
            }
            resumeConversation == SHOW_FOR_OPEN_CONVERSATION && latestOpenConversation != null -> {
                latestOpenConversation?.let { it ->
                    Kustomer.getInstance().openConversationWithId(it.id) { /* no op */ }
                }
                true
            }
            else -> false
        }
    }

    /**
     * Tries to create a new Kustomer Conversation, then open it with the hand-off question
     * as a user sent message.
     *
     * If failed, it will open a new Kustomer Conversation UI,
     * with [string.new_conversation_welcome] as the welcome message, this conversation will not
     * exist on the admin dashboard until the customer sends a message manually.
     * @param handoffData will be used to send the hand-off question in the new conversation.
     */
    private suspend fun createNewKustomerConversation(handoffData: ForethoughtHandoffData) {
        try {
            val messages = if (handoffData.question.isNullOrEmpty()) {
                listOf()
            } else {
                listOf(handoffData.question!!)
            }
            val conv = KustomerCore.getInstance().kusChatProvider().createConversation(
                messages = messages,
                attachments = listOf(),
                messageAction = null,
                lastDeflectionData = null
            ).dataOrNull
            if (conv != null) {
                Kustomer.getInstance().openConversationWithId(conv.first.id) {
                    if (it.dataOrNull != null) {
                        forethought.hide()
                    } else {
                        throw UnknownError("Kustomer Failed to open conversion")
                    }
                }
            } else {
                throw UnknownError("Kustomer Failed to create conversion")
            }
            forethought.sendHandoffResponse(true)
        } catch (e: UnknownError) {
            Timber.e(e.message ?: "Kustomer Failure on Handoff")
            forethought.hide()
            forethought.sendHandoffResponse(false)
            Kustomer.getInstance()
                .openNewConversation(application.getString(string.new_conversation_welcome))
        }
    }

    /**
     * Set's and updates the user's email received from the hand-off data in the Kustomer SDK.
     * @param handoffData
     */
    private suspend fun updateCustomerEmail(handoffData: ForethoughtHandoffData) {
        handoffData.email?.let {
            val attributes = KusCustomerDescribeAttributes(
                emails = listOf(KusEmail(it))
            )
            Kustomer.getInstance().describeCustomer(attributes)
        }
    }
}
