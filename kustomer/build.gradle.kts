plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation(Dependencies.Androidx.coreKtx)
    implementation(Dependencies.Androidx.appcompat)
    implementation(Dependencies.Androidx.constraintLayout)
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.material)
    implementation(Dependencies.timber)
    api(Dependencies.Kustomer.core)
    api(Dependencies.Kustomer.ui)
    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.extJUnit)
    androidTestImplementation(TestDependencies.espressoCore)
    implementation("ai.forethought.solve-android-source:core:0.0.7")
}
