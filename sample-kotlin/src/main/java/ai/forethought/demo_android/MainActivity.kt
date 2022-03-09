package ai.forethought.demo_android

import ai.forethought.Forethought
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonContactSupport: TextView = findViewById(R.id.button_contact_support)
        buttonContactSupport.setOnClickListener {
            // Show Forethought Solve UI on button click
            Forethought.show()
        }
    }
}
