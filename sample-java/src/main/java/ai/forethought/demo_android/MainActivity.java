package ai.forethought.demo_android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ai.forethought.Forethought;

class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView buttonContactSupport = findViewById(R.id.button_contact_support);
        // Show Forethought Solve UI on button click
        buttonContactSupport.setOnClickListener(v -> Forethought.INSTANCE.show());
    }
}
