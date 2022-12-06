package ai.forethought.demo_android;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ai.forethought.Forethought;
import ai.forethought.core.ForethoughtHandoffData;
import ai.forethought.core.ForethoughtListener;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.IntercomError;
import io.intercom.android.sdk.IntercomStatusCallback;
import io.intercom.android.sdk.identity.Registration;

public class MainActivity extends AppCompatActivity implements ForethoughtListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add activity as listener to customize hand-off requests and close actions
        Forethought.INSTANCE.addListener(this);

        TextView buttonContactSupport = findViewById(R.id.button_contact_support);
        // Show Forethought Solve UI on button click
        buttonContactSupport.setOnClickListener(v -> Forethought.INSTANCE.show());


        TextView buttonContactSupportIntercom = findViewById(R.id.button_contact_support_intercom);
        // Show Forethought Solve UI on button click
        buttonContactSupportIntercom.setOnClickListener(v ->
                // Only needed if using intercom plugin
                intercomPluginOnlyDummyUserLogin()
        );
    }


    /**
     * IMPORTANT: In order to use Intercom, they require a user login.
     * PLease replace this logic with a proper login flow in your app.
     * <p>
     * More info can be found here:
     * https://developers.intercom.com/installing-intercom/docs/using-intercom-android
     */
    private void intercomPluginOnlyDummyUserLogin() {
        Intercom.client().logout();
        Registration registration = Registration.create().withUserId("123456789");
        Intercom.client().loginIdentifiedUser(registration, new IntercomStatusCallback() {
            @Override
            public void onSuccess() {
                // Only show Solve-UI widget after a successful intercom login.
                Forethought.INSTANCE.show();
            }

            @Override
            public void onFailure(@NonNull IntercomError intercomError) {
                Log.e("FTS", "Failed to register intercom user:" + intercomError.getErrorMessage());
            }
        });
    }

    @Override
    public void forethoughtHandoffRequested(@NonNull ForethoughtHandoffData forethoughtHandoffData) {
        // Custom hand-off action
        Log.i("FTS", "ForethoughtHandOffRequested");
    }

    @Override
    public void onWidgetClosed() {
        // Custom close action
        Log.i("FTS", "onWidgetClosed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the listener once the activity is destroyed.
        Forethought.INSTANCE.removeListener(this);
    }
}
