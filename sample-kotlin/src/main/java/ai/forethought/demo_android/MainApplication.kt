package ai.forethought.demo_android

import ai.forethought.Forethought
import ai.forethought.kustomer.KustomerPlugin
import ai.forethought.zendesk.ZendeskPlugin
import android.app.Application

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Create Kustomer Plugin
        val kustomerPlugin = KustomerPlugin(
            this,
            "KUSTOMER_API_KEY",
            null,
            forethought = Forethought
        )
        // Create Zendesk Plugin
        val zendeskPlugin = ZendeskPlugin(
            this, "ZENDESK_ACCOUNT_KEY", "ZENDESK_APP_ID", Forethought
        )
        // Pass plugins as a list to Forethought
        Forethought.setup("FORETHOUGHT_API_KEY", listOf(zendeskPlugin, kustomerPlugin))
    }
}
