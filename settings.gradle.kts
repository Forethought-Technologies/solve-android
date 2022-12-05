dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://zendesk.jfrog.io/zendesk/repo") }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Forethought_Samples"
include(":sample-kotlin", ":sample-java", ":plugins:kustomer", ":plugins:zendesk", ":plugins:intercom")
