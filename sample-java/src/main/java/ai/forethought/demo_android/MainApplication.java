package ai.forethought.demo_android;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import ai.forethought.Forethought;
import ai.forethought.core.ForethoughtPlugin;
import ai.forethought.plugin.KustomerPlugin;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KustomerPlugin kustomerPlugin = new KustomerPlugin(
                this,
                "KUSTOMER_API_KEY",
                null,
                Forethought.INSTANCE);

        List<ForethoughtPlugin> plugins = new ArrayList<>();
        plugins.add(kustomerPlugin);
        Forethought.INSTANCE.setup("KUSTOMER_API_KEY", plugins);
    }
}
