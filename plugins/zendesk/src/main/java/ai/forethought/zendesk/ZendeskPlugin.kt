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

    override fun forethoughtHandoffRequested(
        handoffData: ForethoughtHandoffData,
        activityContext: Context
    ) {

        val chatConfiguration = ChatConfiguration.builder()
            .withAgentAvailabilityEnabled(false)
            .withPreChatFormEnabled(false)
            .build()

        val visitorInfo = VisitorInfo.builder()
            .withName(handoffData.name)
            .withEmail(handoffData.email)
            .withPhoneNumber("")
            .build()

        val chatProvidersConfiguration = ChatProvidersConfiguration.builder()
            .withVisitorInfo(visitorInfo)
            .build()

        Chat.INSTANCE.chatProvidersConfiguration = chatProvidersConfiguration

        Chat.INSTANCE.providers()?.connectionProvider()?.connect()
        handoffData.question?.let {
            Chat.INSTANCE.providers()?.chatProvider()?.sendMessage(it)
        }
        Chat.INSTANCE.providers()?.connectionProvider()?.disconnect()

        MessagingActivity.builder()
            .withEngines(ChatEngine.engine())
            .show(activityContext, chatConfiguration)
        forethought.hide()
    }

    override fun forethoughtViewDidAppear(activityContext: Context) {
        /* no op */
    }

    override fun onWidgetClosed() {
        /* no op */
    }

    override fun shouldShowPluginOnLaunch(): Boolean {
        return false
    }
}
