package ai.forethought.demo_android;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import ai.forethought.Forethought;
import ai.forethought.core.ForethoughtPlugin;
import ai.forethought.kustomer.KustomerPlugin;
import ai.forethought.zendesk.ZendeskPlugin;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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

        // Pass plugins as a list to Forethought
        List<ForethoughtPlugin> plugins = new ArrayList<>();
        plugins.add(kustomerPlugin);
        plugins.add(zendeskPlugin);
        Forethought.INSTANCE.setup("KUSTOMER_API_KEY", plugins);
    }
}
