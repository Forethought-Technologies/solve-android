package ai.forethought.demo_android

import ai.forethought.Forethought
import android.app.Application

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupForethoughtSolve()
    }

    private fun setupForethoughtSolve() {
        // Custom Data and Config Parameters
        val configParameters = mapOf("exampleConfigKey" to "exampleConfigValue")
        Forethought.INSTANCE.configParameters = configParameters
        val dataParameters = mapOf(
            "language" to "EN",
            "tracking-email" to "test@ft.ai"
        )
        Forethought.INSTANCE.dataParameters = dataParameters

        // Pass plugins as a list to Forethought
        Forethought.INSTANCE.setup("FORETHOUGHT_API_KEY")
    }
}
