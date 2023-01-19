// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // Kotlin plugin
        classpath(BuildPlugins.kotlin)
        //classpath(BuildPlugins.kotlinSerialization)

        // Gradle plugin
        classpath(BuildPlugins.gradle)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files

        // Hilt
        classpath(BuildPlugins.hilt)
        // Google Services
        classpath(BuildPlugins.playServices)
        //  Crashlytics Gradle plugin
        classpath(BuildPlugins.crashlytics)
        // Performance Monitoring plugin
        classpath(BuildPlugins.performances)
        // Navigation
        classpath(BuildPlugins.navigation)
    }
    repositories {
        mavenCentral()
    }
}

plugins {
    id("com.android.application") version "7.4.0" apply false
    id("com.android.library") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


tasks.register("printVersionName") {
    doLast {
//        android.defaultConfig.versionName
//        print(rootProject.versionName)
    }
}