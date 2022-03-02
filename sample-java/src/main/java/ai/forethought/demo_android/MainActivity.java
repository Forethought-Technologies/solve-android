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

class MainActivity extends AppCompatActivity implements ForethoughtListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Forethought.INSTANCE.addListener(this);

        TextView buttonContactSupport = findViewById(R.id.button_contact_support);
        buttonContactSupport.setOnClickListener(v -> Forethought.INSTANCE.show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Forethought.INSTANCE.removeListener(this);
    }

    @Override
    public void forethoughtHandoffRequested(@NonNull ForethoughtHandoffData forethoughtHandoffData) {
        Log.d("FTS", "MainActivity: forethoughtHandoffRequested");
    }

    @Override
    public void onWidgetClosed() {
        Log.d("FTS", "MainActivity: onWidgetClosed");
    }
}
