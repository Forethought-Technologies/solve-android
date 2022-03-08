package ai.forethought.plugin

import ai.forethought.core.ForethoughtCore
import ai.forethought.core.ForethoughtHandoffData
import ai.forethought.core.ForethoughtPlugin
import ai.forethought.plugin.KustomerResumeConversationSetting.PROMPT_IF_OPEN_OR_UNREAD
import ai.forethought.plugin.KustomerResumeConversationSetting.SHOW_FOR_OPEN_CONVERSATION
import ai.forethought.plugin.KustomerResumeConversationSetting.SHOW_FOR_UNREAD_MESSAGE_PROMPT_FOR_OPEN
import ai.forethought.plugin.R.string
import android.app.Application
import android.content.Context
import com.kustomer.core.KustomerCore
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

    private fun continueOpenConversation(conversationId: String?) {
        // If there is more than one open conversation, open selection screen
        if (conversationId != null && openConversationCount == 1) {
            Kustomer.getInstance().openConversationWithId(conversationId) {
                it.dataOrNull?.let {
                    requestKustomerConversationData()
                    forethought.hide()
                }
            }
        } else {
            Kustomer.getInstance().open(CHAT_ONLY)
            forethought.hide()
        }
    }

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
        } catch (e: UnknownError) {
            Timber.e(e.message ?: "Kustomer Failure on Handoff")
            forethought.hide()
            Kustomer.getInstance()
                .openNewConversation(application.getString(string.new_conversation_welcome))
        }
    }

    private suspend fun updateCustomerEmail(handoffData: ForethoughtHandoffData) {
        handoffData.email?.let {
            val attributes = KusCustomerDescribeAttributes(
                emails = listOf(KusEmail(it))
            )
            Kustomer.getInstance().describeCustomer(attributes)
        }
    }
}
