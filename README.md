# Forethought - Solve Android SDK

This repository contains the framework and instructions for the Forethought Android SDK.

A valid API key is needed in order to use the Forethought Solve SDK. In additon to the instructions below, you can also view sample apps written in Java and Kotlin.

## Installation

### Groovy

   ```groovy
   // add the jitpack maven repository to the project repositories, this can exist
   // inside project's build.gradle, or settings.gradle based on the project.
   repositories {
      google()
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
1. Replace `FORETHOUGHT_API_KEY` with a valid Forethought API key
1. When you'd like to show the Forethought Solve SDK, add the following in your Activity/Fragment:
   ```java
   // Java
   Forethought.INSTANCE.show();

   // Kotlin
   Forethought.show()
   ```

## Optional Additions

### Workflow Context Variables

Pass in Workflow Context Variables that have been defined via the Forethought Dashboard:
   ```java
	   // Java
     Map<String, String> dataParameters = new HashMap<>();
     dataParameters.put("language", "EN");
     dataParameters.put("user-email", "email@ft.ai");
     Forethought.INSTANCE.setDataParameters(dataParameters);

     // Kotlin
     val dataParameters = mapOf(
        "language" to "EN",
        "user-email" to "email@ft.ai"
       )
     Forethought.dataParameters = dataParameters
   ```

### Handoff Methods

To handoff customers from Forethought to an Agent Chat Provider like Kustomer:

1. Activity/Fragment needs to implement the ForethoughtListener interface, and override its methods.
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

2. In the onCreate method, add the Activity/Fragment as a listener to the Forethought Solve SDk:
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

3. Don't forget to remove the listener on the onDestory of your Activity/Fragment to prevent memory leaks.
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

To attach Forethought to another chat provider, such as Zendesk or Kustomer, please check out our [plugin documentation](plugins/PLUGINS.md).