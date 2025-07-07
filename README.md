# Forethought - Solve Android SDK

This repository contains the framework and instructions for the Forethought Android SDK.

A valid API key is needed in order to use the Forethought Solve SDK. In additon to the instructions below, sample apps have been written in Java and Kotlin.

## Installation

### Groovy

```groovy
// add the jitpack maven repository to the project repositories, this can exist
// inside project's build.gradle, or settings.gradle.
repositories {
   google()
   // jitpack repository
   maven { url "https://jitpack.io" }
}


// add the dependency to the app's build.gradle
dependencies {
   // Solve Android SDK
   implementation "ai.forethought:solve-android-source:1.3.5"
}
```

### Kotlin

```kotlin
// add the jitpack maven repository to the project repositories, this can exist
// inside project's build.gradle, or settings.gradle.
repositories {
   google()
   // jitpack repository
   maven { url = uri("https://jitpack.io") }
}


// add the dependency to the app's build.gradle
dependencies {
   // Solve Android SDK
   implementation("ai.forethought:solve-android-source:1.3.5")
}
```

## Basic Usage

1. Inside the Android Application class, add the following lines to the onCreate() method:

```java
Forethought.INSTANCE.setup("FORETHOUGHT_API_KEY")
```

2. Replace `FORETHOUGHT_API_KEY` with a valid Forethought API key

3. To show / hide the Forethought Solve SDK, add the following in the `Activity/Fragment`:

```java
Forethought.INSTANCE.show();
Forethought.INSTANCE.hide();
```

## Additional Usage

### Widget Parameters

A comprehensive list of non workflow context variable parameters can be found [here.](https://support.forethought.ai/hc/en-us/articles/1500002917301-Installation-Guide-for-Solve-Widget#:~:text=Additional%20Attributes) To pass widget parameters, add any or all the following before calling `.show()`:

- `dataParameters` will be prefixed with `data-ft-`
- `configParameters` will be prefixed with `config-ft-`
- `additionalParameters` will NOT be prefixed

```java
// Java
Map<String, String> dataParameters = new HashMap<>();
dataParameters.put("language", "EN");
dataParameters.put("tracking-email", "test@ft.ai");
Forethought.INSTANCE.setDataParameters(dataParameters);

// Kotlin
val dataParameters = mapOf(
   "language" to "EN",
   "tracking-email" to "test@ft.ai"
   )
Forethought.INSTANCE.dataParameters = dataParameters
```

### Callback Methods

ForethoughtListener includes the following callback methods.
- `forethoughtHandoffRequested`
  - Called when the user is being handed off to another helpdesk (e.g. Salesforce, Zendesk, Kustomer, etc.).
- `onWidgetClosed`
  - Called when the widget is being closed.
- `onWidgetError`
  - Called when the widget does not render properly.

To implement these callback methods, add the following:

1. Make sure the `Activity/Fragment` implements the ForethoughtListener interface, and override it's methods. The methods
   do have default implementations so they are optional.

```java
// Java
public class MainActivity extends AppCompatActivity implements ForethoughtListener {
   // ...
   @Override
   public void forethoughtHandoffRequested(@NonNull ForethoughtHandoffData forethoughtHandoffData) {
      // Custom handoff action
      // ...

      // if handoff was successful
      Forethought.INSTANCE.sendHandoffResponse(true);

      // if handoff was unsuccessful
      Forethought.INSTANCE.sendHandoffResponse(false);

      // hide Forethought after sendHandoffResponse
      Forethought.INSTANCE.hide();
   }

   @Override
   public void onWidgetClosed() {
      // Custom close action
      // Called when customer closes the widget or when onBackPressed is triggered
   }

   @Override
   public void onWidgetError() {
   // handle when the webview doesn't render the widget
   }
}

// Kotlin
class MainActivity : AppCompatActivity(), ForethoughtListener {
   // ...
   override fun forethoughtHandoffRequested(handoffData: ForethoughtHandoffData) {
      // Custom handoff action
      // ...

      // if handoff was successful
      Forethought.INSTANCE.sendHandoffResponse(true)

      // if handoff was unsuccessful
      Forethought.INSTANCE.sendHandoffResponse(false)

      // hide Forethought after sendHandoffResponse
      Forethought.INSTANCE.hide()
   }

   override fun onWidgetClosed() {
      // Custom close action
      // Called when customer closes the widget or when onBackPressed is triggered
   }

   override fun onWidgetError() {
      // handle when the webview doesn't render the widget
   }
}
```

2. In the onCreate method, add the `Activity/Fragment` as a listener to the Forethought Solve SDk:

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
   Forethought.INSTANCE.addListener(this)
}
```

3. Don't forget to remove the listener on the `onDestory` of the `Activity/Fragment` to prevent memory leaks.

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
   Forethought.INSTANCE.removeListener(this)
}
```

### Handoffs

The `forethoughtHandoffRequested` callback method has a single parameter that will include all the data normally used by Forethought to handoff to the helpdesk's widget in the browser. This data will have the following type:

```kotlin
data class ForethoughtHandoffData(
    val event: String?,
    val name: String?,
    val email: String?,
    val question: String?,
    val integration: String?,
    val department: String?,
    val additionalParameters: Map<String, Any>?,
)
```

### Plugins

**⛔️ Plugins were removed in version 1.0.0 ⛔️**
