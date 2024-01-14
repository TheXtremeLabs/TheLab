plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    alias(libs.plugins.kotlin.serialization)
    id("thelab.android.hilt")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.feature.theaters"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    androidTestImplementation(project(":core:testing"))
}