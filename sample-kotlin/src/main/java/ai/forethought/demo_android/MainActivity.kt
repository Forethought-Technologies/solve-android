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

        val buttonContactSupport: TextView = findViewById(R.id.button_contact_support)
        buttonContactSupport.setOnClickListener {
            Forethought.show()
        }
    }

    override fun onWidgetClosed() {
        Log.d("FTS", "MainActivity: onWidgetClosed")
    }

    override fun forethoughtHandoffRequested(handoffData: ForethoughtHandoffData) {
        Log.d("FTS", "MainActivity: forethoughtHandoffRequested")
    }
}
