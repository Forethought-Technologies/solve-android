# Forethought Plugins

When you want to hand off your Forethought chat to a live agent, you have two choices:

1. Implement the handoff yourself, or
2. Use a pre-made plugin that handles all of the logic for you

All Forethought Plugins are open-source and customizable. You'll still be able to access the containing frameworks, and if you'd like, you can download the plugin file and further customize it yourself.

## Installation

### Groovy

   ```groovy
// add the required maven repositories to the project repositories, this can exist
// inside project's build.gradle, or settings.gradle based on your project.
repositories {
    google()
    // jitpack repository
    maven { url "https://jitpack.io" }
    // Required for the Zendesk Plugin only
    maven { url "https://zendesk.jfrog.io/zendesk/repo" }
}


// add the dependencies to the app's build.gradle
dependencies {
   // Solve Android SDK
   implementation "ai.forethought:solve-android-source:0.1.0"

   // To add Kustomer plugin
   implementation "ai.forethought.solve-android:kustomer:0.1.0"

   // To add Zendesk Plugin
   implementation "ai.forethought.solve-android:zendesk:0.1.0"
}
   ```

### Kotlin

   ```kotlin
// add the required maven repositories to the project repositories, this can exist
// inside project's build.gradle, or settings.gradle based on your project.
repositories {
    google()
    // jitpack repository
    maven { url = uri("https://jitpack.io") }
    // Required for the Zendesk Plugin only
    maven { url = uri("https://zendesk.jfrog.io/zendesk/repo") }
}


// add the dependencies to the app's build.gradle
dependencies {
   // Solve Android SDK
   implementation("ai.forethought:solve-android-source:0.1.0")

   // To add Kustomer plugin
   implementation("ai.forethought.solve-android:kustomer:0.1.0")

   // To add Zendesk Plugin
   implementation("ai.forethought.solve-android:zendesk:0.1.0")
}
   ```

##  Usage

To attach your Plugin to Forethought, make the following changes to your Application Class:

### Java

   ```java
// Create Kustomer Plugin
KustomerPlugin kustomerPlugin = new KustomerPlugin(
        this,
        "KUSTOMER_API_KEY",
        null, // KustomerOptions
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
Forethought.INSTANCE.setup("FORETHOUGHT_API_KEY", plugins);
   ```


### Kotlin

   ```kotlin
// Create Kustomer Plugin
val kustomerPlugin = KustomerPlugin(
    this,
    "KUSTOMER_API_KEY",
    null, // KustomerOptions
    forethought = Forethought
)

// Create Zendesk Plugin
val zendeskPlugin = ZendeskPlugin(
    this, "ZENDESK_ACCOUNT_KEY", "ZENDESK_APP_ID", Forethought
)
// Pass plugins as a list to Forethought
Forethought.setup("FORETHOUGHT_API_KEY", listOf(zendeskPlugin, kustomerPlugin))
   ```


And that's it! To further customize, feel free to check out the source code in the plugins/ directory. Each plugin has options for further customization.