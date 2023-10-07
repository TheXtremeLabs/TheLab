plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    id("thelab.android.library.jacoco")
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.feature.musicrecognition"
}

dependencies {

    implementation(files("libs/acrcloud-universal-sdk-1.3.22.jar"))
    // implementation(files("libs/spotify-app-remote-release-0.8.0.aar"))
    // implementation(files("libs/spotify-auth-release-2.1.0.aar"))
    // implementation(files("libs/spotify-auth-store-release-2.1.0.aar"))

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:data"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.serialization.json)

    // AndroidX
    api(libs.androidx.core.ktx)
    api(libs.androidx.browser)

    api(libs.spotify.auth)

    androidTestImplementation(project(":core:testing"))
}