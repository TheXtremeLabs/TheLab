plugins {
    /**
     * You should use `apply false` in the top-level build.gradle file
     * to add a Gradle plugin as a build dependency, but not apply it to the
     * current (root) project. You should not use `apply false` in sub-projects.
     * For more information, see
     * Applying external plugins with same version to subprojects.
     */

    /**
     * Defined in build-logic/convention/build.gradle.kts class
     */
    id("thelab.android.application")
    id("thelab.android.application.compose")
    id("thelab.android.application.jacoco")
    id("thelab.android.hilt")
    id("jacoco")
    id("androidx.navigation.safeargs.kotlin")
    id("thelab.firebase")
}

android {

    /**
     * Defined in AndroidApplicationConventionPlugin class
     */
    // compileSdk = ConfigData.compileSdkVersion

    useLibrary("org.apache.http.legacy")

    ndkVersion = "21.3.6528147"

    defaultConfig {
        applicationId = "com.riders.thelab"

        /**
         * Defined in AndroidApplicationConventionPlugin class
         */
        /*minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName*/

        // Enabling multidex support.
        multiDexEnabled = true

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = false

            //applicationIdSuffix = LabBuildType.DEBUG.applicationIdSuffix
        }

        getByName("release") {
            isDebuggable = false

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true  // Enables code shrinking for the release build type.

            // Disables PNG crunching for the "release" build type.
            isCrunchPngs = false

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //applicationIdSuffix = LabBuildType.RELEASE.applicationIdSuffix
        }
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        // Determines whether to support Data Binding.
        // Note that the dataBinding.enabled property is now deprecated.
        dataBinding = true
    }

    lint {
        abortOnError = false
        ignoreWarnings = true
        quiet = true
    }

    namespace = "com.riders.thelab"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:testing"))
    implementation(project(":feature:biometric"))
    implementation(project(":feature:musicrecognition"))
    implementation(project(":feature:weather"))

    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    //Kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.parcelize)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.play.services)

    // AndroidX
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    /*implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment)*/
//    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.palette)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.material)
//    implementation(libs.androidx.biometric)
    implementation(libs.androidx.dynamicanimation)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.window.manager)
    implementation(libs.androidx.window.extensions)

    // Compose
    /*implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)*/
    /*implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)*/
    /*implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.animation)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)*/
    /*implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3.windowSizeClass)*/
    /*implementation(libs.androidx.compose.fonts)
    implementation(libs.androidx.activity.compose)*/
    /*implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.hilt.navigation.compose)*/

    // Navigation
    implementation(libs.androidx.navigation.ktx)
    implementation(libs.androidx.navigation.fragment)

    // CameraX
    implementation(libs.androidx.camera)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    // Media
    implementation(libs.androidx.media)
    implementation(libs.androidx.media.router)
    implementation(libs.androidx.media.exoplayer)
    implementation(libs.androidx.media.ui)

    // Auto fill
    implementation(libs.androidx.autofill)

    // Worker & concurrent: implemented in weather feature
    /*implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.work.multiprocess)
    implementation(libs.androidx.concurrent)
    androidTestImplementation(libs.androidx.work.testing)*/

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewModel.ktx)
    implementation(libs.androidx.lifecycle.viewModel.savedState)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    kapt(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.lifecycle.process)

    // Datastore and Preferences
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.preferences)

    /* Hilt - We are going to use hilt.android which includes
     * support for Activity and fragment injection so we need to include
     * the following dependencies */
    // Hilt
    //implementation(Dependencies.hilt)
    //kapt(Dependencies.hiltCompiler)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)

    // Google Location (Maps / Places)
    implementation(libs.maps)
    implementation(libs.maps.utils)
    implementation(libs.location)
    implementation(libs.places)

    // Google API
    implementation(libs.google.play.services.base)
    implementation(libs.google.play.services.auth)
    implementation(libs.google.api.drive)
    implementation(libs.google.http.client)
    implementation(libs.google.api.client)
    implementation(libs.google.api.client.android)
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-jetty
    implementation(libs.google.api.client.jetty)
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-java6
    implementation(libs.google.api.client.oauth.java6)
    implementation(libs.google.api.client.oauth.jackson2)

    // Google ML Kit
    implementation(libs.google.barcode.scanning)
    implementation(libs.google.objectdetection)
    implementation(libs.google.objectdetection.custom)

    /* Retrofit using RxJava3, Okhttp, Okhttp logging interceptor, Moshi  */
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.kotlin.serialization)

    // OkHttp
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)

    // MPAndroidChart
    implementation(libs.mpandroidchart)

    // Dexter
    implementation(libs.dexter)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.glide.blurry)
    implementation(libs.glide.transformation)
    implementation(libs.glide.landscapist)

    //ThreeTen : Alternative to Android Calendar API
    implementation(libs.threeten)

    // Kotools Types
    implementation(libs.kotools.types)


    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.mockito.android)
/*
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)*/

    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.ext.compiler)
}

tasks.named("build") {
    doLast {
        print(rootProject.version)
    }
}