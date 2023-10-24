package ai.forethought.demo_android

import ai.forethought.Forethought
import ai.forethought.core.ForethoughtHandoffData
import ai.forethought.core.ForethoughtListener
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ForethoughtListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // add activity as listener to customize hand-off requests and close actions
        Forethought.addListener(this)

        val buttonContactSupport: TextView = findViewById(R.id.button_contact_support)
        buttonContactSupport.setOnClickListener {
            // Show Forethought Solve UI on button click
            Forethought.show()
        }
    }

    override fun forethoughtHandoffRequested(handoffData: ForethoughtHandoffData) {
        // Custom hand-off action
        Log.i("FTS", "ForethoughtHandOffRequested")
        // success
        Forethought.hide()
        Forethought.sendHandoffResponse(true)
        // failure
        // Forethought.sendHandoffResponse(false)
    }

    override fun onWidgetClosed() {
        // Custom close action
        Log.i("FTS", "onWidgetClosed")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the listener once the activity is destroyed.
        Forethought.removeListener(this)
    }
}
