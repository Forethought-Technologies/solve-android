object Dependencies {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    object Androidx {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.Androidx.appcompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.Androidx.coreKtx}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.Androidx.constraintLayout}"
    }

    const val material = "com.google.android.material:material:${Versions.material}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    object Kustomer {
        const val ui = "com.kustomer.chat:ui:${Versions.kustomer}"
        const val core = "com.kustomer.chat:core:${Versions.kustomer}"
    }

    object Zendesk {
        const val chat = "com.zendesk:chat:${Versions.zendesk_chat}"
        const val messaging = "com.zendesk:messaging:${Versions.zendesk_messaging}"
    }

    object Forethought {
        const val core = "ai.forethought.solve-android-source:core:${Versions.solve}"
        const val solve = "ai.forethought:solve-android-source:${Versions.solve}"
    }
}

object TestDependencies{
    const val junit = "junit:junit:${Versions.junit}"
    const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}
