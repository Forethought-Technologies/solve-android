package ai.forethought.demo_android

import ai.forethought.Forethought
import ai.forethought.core.ForethoughtHandoffData
import ai.forethought.core.ForethoughtListener
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.IntercomError
import io.intercom.android.sdk.IntercomStatusCallback
import io.intercom.android.sdk.identity.Registration

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

        val buttonContactSupportIntercom: TextView = findViewById(R.id.button_contact_support_intercom)
        buttonContactSupportIntercom.setOnClickListener {
            // Only needed if using intercom plugin
            intercomPluginOnlyDummyUserLogin()
        }
    }

    /**
     * IMPORTANT: In order to use Intercom, they require a user login.
     * PLease replace this logic with a proper login flow in your app.
     *
     * More info can be found here:
     * https://developers.intercom.com/installing-intercom/docs/using-intercom-android
     */
    private fun intercomPluginOnlyDummyUserLogin() {
        Intercom.client().logout()
        val registration = Registration.create().withUserId("123456789")
        Intercom.client().loginIdentifiedUser(
            userRegistration = registration,
            intercomStatusCallback = object : IntercomStatusCallback{
                override fun onSuccess() {
                    // Only show Solve-UI widget after a successful intercom login.
                    Forethought.show()
                }

                override fun onFailure(intercomError: IntercomError) {
                    Log.e("FTS", "Failed to register intercom user: ${intercomError.errorMessage}")
                }

            }
        )
    }
    override fun forethoughtHandoffRequested(handoffData: ForethoughtHandoffData) {
        // Custom hand-off action
        Log.i("FTS", "ForethoughtHandOffRequested")
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
