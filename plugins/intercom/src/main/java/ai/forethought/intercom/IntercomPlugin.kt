package ai.forethought.intercom

import ai.forethought.core.ForethoughtCore
import ai.forethought.core.ForethoughtHandoffData
import ai.forethought.core.ForethoughtPlugin
import ai.forethought.intercom.IntercomResumeConversationSetting.ALWAYS_START_NEW_CONVERSATION
import ai.forethought.intercom.IntercomResumeConversationSetting.SHOW_FOR_UNREAD_MESSAGE
import android.app.Application
import android.content.Context
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.IntercomSpace

/**
 * Plugin to support hand-off requests to the Intercom SDK.
 *
 * @property forethought [ForethoughtCore] instance to communicate with the Forethought Solve SDK.
 * @param application android application instance.
 * @param appId Intercom SDK Application ID - https://app.intercom.com/a/apps/_/settings/android
 * @param apiKey Intercom SDK API key. - https://app.intercom.com/a/apps/_/settings/android
 */
class IntercomPlugin(
    application: Application,
    apiKey: String,
    appId: String,
    private val forethought: ForethoughtCore
) : ForethoughtPlugin {
    override var pluginName: String = "intercom"

    /**
     * Can be changed to any value of [IntercomResumeConversationSetting]
     * This will change the behavior of the plugin on hand-off requests, and on start-up.
     */
    var resumeSetting: IntercomResumeConversationSetting = SHOW_FOR_UNREAD_MESSAGE

    init {
        Intercom.initialize(application, apiKey, appId)
    }

    /**
     * This will be called when the user requested a hand-off.
     *
     * Opens a new Intercom conversation with the question received from the hand-off data or opens
     * the Intercom home if there are unread messages and the [resumeSetting] is set to [SHOW_FOR_UNREAD_MESSAGE].
     *
     * @param handoffData the data passed from Forethought SDK when the user requested a hand-off.
     * @param activityContext the forethought activity context, not needed for this plugin.
     */
    override fun forethoughtHandoffRequested(
        handoffData: ForethoughtHandoffData,
        activityContext: Context
    ) {
        val unreadCount = Intercom.client().unreadConversationCount
        if (unreadCount > 0 && resumeSetting == SHOW_FOR_UNREAD_MESSAGE) {
            Intercom.client().present(space = IntercomSpace.Home)
        } else {
            Intercom.client().displayMessageComposer(handoffData.question)
        }
        forethought.sendHandoffResponse(true)
    }

    override fun forethoughtViewDidAppear(activityContext: Context) {
        /* no op */
    }

    override fun onWidgetClosed() {
        /* no op */
    }

    /**
     * Decides if Intercom should be shown on startup instead of the Forethought Solve UI.
     * @return false if [resumeSetting] is set [ALWAYS_START_NEW_CONVERSATION] and there are unread messages.
     */
    override fun shouldShowPluginOnLaunch(): Boolean {
        return when {
            resumeSetting == ALWAYS_START_NEW_CONVERSATION -> false
            Intercom.client().unreadConversationCount == 0 -> false
            else -> {
                Intercom.client().present()
                true
            }
        }
    }
}