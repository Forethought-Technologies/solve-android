# Forethought - Solve Android SDK

This repository contains the framework and instructions for the Forethought Android SDK.

You will need a valid API key in order to use the Forethought Solve SDK. In additon to the instructions below, you can also view sample apps written in Java and Kotlin.

## Installation

### Groovy

   ```groovy
// add the jitpack maven repository to the project repositories, this can exist
// inside project's build.gradle, or settings.gradle based on your project.
repositories {
   google()
   // jitpack repository
   maven { url "https://jitpack.io" }
}


// add the dependency to the app's build.gradle
dependencies {
   // Solve Android SDK
   implementation "ai.forethought:solve-android-source:0.1.0"
}
   ```

### Kotlin

   ```kotlin
// add the jitpack maven repository to the project repositories, this can exist
// inside project's build.gradle, or settings.gradle based on your project.
repositories {
   google()
   // jitpack repository
   maven { url = uri("https://jitpack.io") }
}


// add the dependency to the app's build.gradle
dependencies {
   // Solve Android SDK
   implementation("ai.forethought:solve-android-source:0.1.0")
}
   ```

## Basic Usage

1. Inside your Android Application class , add the following lines to the onCreate() method:

   ```java
   // Java
   Forethought.INSTANCE.setup("FORETHOUGHT_API_KEY", null);

   // Kotlin
   Forethought.setup("FORETHOUGHT_API_KEY")
   ```

1. Replace `FORETHOUGHT_API_KEY` with your API key you received from Forethought

1. When you'd like to show the Forethought Solve SDK, add the following in your Activity/Fragment:

   ```java
   // Java
   Forethought.INSTANCE.show();

   // Kotlin
   Forethought.show()
   ```


## Optional Additions

### Custom Data and Config Parameters

You can send custom parameters that you define directly with Forethought like this:

   ```java
	   // Java
     Map<String, String> configParameters = new HashMap<>();
     configParameters.put("exampleConfigKey", "exampleConfigValue");
     Forethought.INSTANCE.setConfigParameters(configParameters);

     Map<String, String> dataParameters = new HashMap<>();
     dataParameters.put("language", "EN");
     dataParameters.put("tracking-email", "test@ft.ai");
     Forethought.INSTANCE.setDataParameters(dataParameters);

     // Kotlin
     val configParameters = mapOf("exampleConfigKey" to "exampleConfigValue")
     Forethought.configParameters = configParameters

     val dataParameters = mapOf(
        "language" to "EN",
        "tracking-email" to "test@ft.ai"
       )
     Forethought.dataParameters = dataParameters
   ```

### Custom Handoff Methods

If you'd like to handoff Forethought chat to another provider, you can do so by implementing the following:

1. Make your Activity/Fragment implements the ForethoughtListener interface, and override it's methods, You can inspect and view the handoffData object to access history of the previous chat.
   ```java
   // Java
   public class MainActivity extends AppCompatActivity implements ForethoughtListener {
      // ...
      @Override
       public void forethoughtHandoffRequested(@NonNull ForethoughtHandoffData forethoughtHandoffData) {
           // Custom hand-off action
       }

       @Override
       public void onWidgetClosed() {
           // Custom close action
       }
   }

   // Kotlin
   class MainActivity : AppCompatActivity(), ForethoughtListener {
     // ...
     override fun forethoughtHandoffRequested(handoffData: ForethoughtHandoffData) {
           // Custom hand-off action
       }

       override fun onWidgetClosed() {
           // Custom close action
       }
   }
   ```

1. In the onCreate method, add the Activity/Fragment as a listener to the Forethought Solve SDk:
   ```java
   // Java
   @Override
   protected void onCreate(...) {
       super.onCreate(savedInstanceState);
       // ...
       Forethought.INSTANCE.addListener(this);
   }

   // Kotlin
   override fun onCreate(...) {
       super.onCreate(savedInstanceState)
       // ...
       Forethought.addListener(this)
   }
   ```

1. Don't forget to remove the listener on the onDestory of your Activity/Fragment to prevent memory leaks.
   ```java
   // Java
   @Override
   protected void onDestroy() {
       super.onDestroy();
       // Remove the listener once the activity is destroyed.
       Forethought.INSTANCE.removeListener(this);
   }

   // Kotlin
   override fun onDestroy() {
       super.onDestroy()
       // Remove the listener once the activity is destroyed.
       Forethought.removeListener(this)
   }
   ```


### Plugins

**Our Plugins are no longer supported and will be deprecated in future versions**

Use your Helpdesk's SDK to handle chat handoffs however you would like in the `forethoughtHandoffRequested` method explained in the [Custom Handoff Methods](#custom-handoff-methods) section
