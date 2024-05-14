plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.riders.thelab.feature.transitions"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:permissions"))
    implementation(project(":core:ui"))
}