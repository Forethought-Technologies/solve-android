package ai.forethought.demo_android;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.forethought.Forethought;
import ai.forethought.core.ForethoughtPlugin;
import ai.forethought.kustomer.KustomerPlugin;
import ai.forethought.zendesk.ZendeskPlugin;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupForethoughtSolve();
    }

    private void setupForethoughtSolve() {
        // Create Kustomer Plugin
        KustomerPlugin kustomerPlugin = new KustomerPlugin(
                this,
                "KUSTOMER_API_KEY",
                null,
                Forethought.INSTANCE);
        // Create Zendesk Plugin
        ZendeskPlugin zendeskPlugin = new ZendeskPlugin(this,
                "ZENDESK_ACCOUNT_KEY",
                "ZENDESK_APP_ID",
                Forethought.INSTANCE);

        // Custom Data and Config Parameters
        Map<String, String> configParameters = new HashMap<>();
        configParameters.put("exampleConfigKey", "exampleConfigValue");
        Forethought.INSTANCE.setConfigParameters(configParameters);

        Map<String, String> dataParameters = new HashMap<>();
        dataParameters.put("language", "EN");
        dataParameters.put("tracking-email", "test@ft.ai");
        Forethought.INSTANCE.setDataParameters(dataParameters);

        // Pass plugins as a list to Forethought
        List<ForethoughtPlugin> plugins = new ArrayList<>();
        plugins.add(kustomerPlugin);
        plugins.add(zendeskPlugin);
        Forethought.INSTANCE.setup("FORETHOUGHT_API_KEY", plugins);
    }
}
