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
    }


    @Override
    public void forethoughtHandoffRequested(@NonNull ForethoughtHandoffData forethoughtHandoffData) {
        // Custom hand-off action
        Log.i("FTS", "ForethoughtHandOffRequested");
        // success
        Forethought.INSTANCE.hide();
        Forethought.INSTANCE.sendHandoffResponse(true);
        // failure
        // Forethought.INSTANCE.sendHandoffResponse(false);
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
