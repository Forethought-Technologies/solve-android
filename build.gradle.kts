buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}")
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint-idea") version Versions.ktlint
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
tasks.register("detektAll", io.gitlab.arturbosch.detekt.Detekt::class) {
    description = "Runs detekt analysis over all modules"
    parallel = true
    failFast = false
    config.from(rootProject.file("detekt.yml"))
    setSource(files(projectDir))
    buildUponDefaultConfig = true
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
    }
}

subprojects {
    apply(plugin = "maven-publish")
    project.afterEvaluate {
        if (plugins.hasPlugin("android").not() && name.equals("plugins").not()) {
            configure<PublishingExtension> {
                publications.create<MavenPublication>(project.name) {
                    project.afterEvaluate {
                        groupId = "ai.forethought.android"
                        artifactId = project.name
                        if (plugins.hasPlugin("java")) {
                            from(components["java"])
                        } else if (plugins.hasPlugin("android-library")) {
                            from(components["release"])
                        }
                        version = "0.0.1"
                    }
                }
            }
        }
    }

    // Setup Ktlint
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        android.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        coloredOutput.set(true)
        verbose.set(true)
        filter {
            exclude("**/generated/**")
            exclude("**/build/**")
        }
    }
}

