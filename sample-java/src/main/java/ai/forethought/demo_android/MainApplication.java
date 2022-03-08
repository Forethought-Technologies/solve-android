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
        KustomerPlugin kustomerPlugin = new KustomerPlugin(
                this,
                "KUSTOMER_API_KEY",
                null,
                Forethought.INSTANCE);

        ZendeskPlugin zendeskPlugin = new ZendeskPlugin(this,
                "ZENDESK_ACCOUNT_KEY",
                "ZENDESK_APP_ID",
                Forethought.INSTANCE);
        List<ForethoughtPlugin> plugins = new ArrayList<>();
        plugins.add(kustomerPlugin);
        plugins.add(zendeskPlugin);

        Forethought.INSTANCE.setup("KUSTOMER_API_KEY", plugins);
    }
}
