package ai.forethought.demo_android

import ai.forethought.Forethought
import ai.forethought.plugin.KustomerPlugin
import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val kustomerPlugin = KustomerPlugin(
            this,
            "KUSTOMER_API_KEY",
            null,
            forethought = Forethought
        )
        Forethought.setup("KUSTOMER_API_KEY", listOf(kustomerPlugin))
    }
}
