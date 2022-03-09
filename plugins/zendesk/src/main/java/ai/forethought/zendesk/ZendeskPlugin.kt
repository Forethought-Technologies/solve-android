package ai.forethought.zendesk

import ai.forethought.core.ForethoughtCore
import ai.forethought.core.ForethoughtHandoffData
import ai.forethought.core.ForethoughtPlugin
import android.app.Application
import android.content.Context
import zendesk.chat.Chat
import zendesk.chat.ChatConfiguration
import zendesk.chat.ChatEngine
import zendesk.chat.ChatProvidersConfiguration
import zendesk.chat.VisitorInfo
import zendesk.messaging.MessagingActivity

/**
 * Plugin to support hand-off requests to the Zendesk SDK.
 *
 * @property forethought [ForethoughtCore] instance to communicate with the Forethought Solve SDK.
 * @param application android application instance.
 * @param accountKey Zendesk SDK Account Key.
 * @param appId Zendesk SDK Current App Id.
 */
class ZendeskPlugin(
    application: Application,
    accountKey: String,
    appId: String,
    private val forethought: ForethoughtCore
) : ForethoughtPlugin {
    override var pluginName: String = "zendesk"

    init {
        Chat.INSTANCE.init(application, accountKey, appId)
    }

    /**
     * This will be called when the user requested a hand-off.
     *
     * Opens a new Zendesk conversation with the question received from the hand-off data
     * @param handoffData the data passed from Forethought SDK when the user requested a hand-off.
     * @param activityContext the forethought activity context, needed to
     * show zendesk's [MessagingActivity]
     */
    override fun forethoughtHandoffRequested(
        handoffData: ForethoughtHandoffData,
        activityContext: Context
    ) {
        // Configure Zendesk Chat
        val chatConfiguration = ChatConfiguration.builder()
            .withAgentAvailabilityEnabled(false)
            .withPreChatFormEnabled(false)
            .build()

        // Pass name and email from the handOfData to visitor info
        val visitorInfo = VisitorInfo.builder()
            .withName(handoffData.name)
            .withEmail(handoffData.email)
            .withPhoneNumber("")
            .build()

        // Update Chat Configurations
        val chatProvidersConfiguration = ChatProvidersConfiguration.builder()
            .withVisitorInfo(visitorInfo)
            .build()
        Chat.INSTANCE.chatProvidersConfiguration = chatProvidersConfiguration

        // Connect, Send the question from the handOfData, then disconnect.
        Chat.INSTANCE.providers()?.connectionProvider()?.connect()
        handoffData.question?.let {
            Chat.INSTANCE.providers()?.chatProvider()?.sendMessage(it)

            Chat.INSTANCE.providers()?.connectionProvider()?.disconnect()

            // Show Zendesk UI
            MessagingActivity.builder()
                .withEngines(ChatEngine.engine())
                .show(activityContext, chatConfiguration)

            // Hide Forethought UI
            forethought.hide()
        }
    }

    override fun forethoughtViewDidAppear(activityContext: Context) {
        /* no op */
    }

    override fun onWidgetClosed() {
        /* no op */
    }

    /**
     * Decides if Zendesk should be shown on startup instead of the Forethought Solve UI.
     * @return always false for this plugin
     */
    override fun shouldShowPluginOnLaunch(): Boolean {
        return false
    }
}
