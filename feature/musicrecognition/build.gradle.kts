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

    androidTestImplementation(project(":core:testing"))
}