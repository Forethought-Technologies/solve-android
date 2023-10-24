package ai.forethought.demo_android;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

import ai.forethought.Forethought;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupForethoughtSolve();
    }

    private void setupForethoughtSolve() {
        // Data Parameters
        Map<String, String> dataParameters = new HashMap<>();
        dataParameters.put("language", "EN");
        dataParameters.put("tracking-email", "test@ft.ai");
        Forethought.INSTANCE.setDataParameters(dataParameters);

        Forethought.INSTANCE.setup("FORETHOUGHT_API_KEY");
    }
}
