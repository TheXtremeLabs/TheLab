plugins {
    alias(libs.plugins.thelab.feature)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.thelab.hilt)
}

android {
    namespace = "com.riders.thelab.feature.login"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:google"))
    implementation(project(":core:ui"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////

    ///////////////////////////////////
    // Tests Dependencies
    ///////////////////////////////////
    androidTestImplementation(project(":core:testing"))
}