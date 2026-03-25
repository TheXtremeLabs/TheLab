plugins {
    alias(libs.plugins.thelab.feature)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.riders.thelab.feature.splashscreen"

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
    implementation(project(":core:ui"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Media
    implementation(libs.androidx.media)
    implementation(libs.androidx.media.router)
    implementation(libs.androidx.media.exoplayer)
    implementation(libs.androidx.media.ui)

    ///////////////////////////////////
    // Tests Dependencies
    ///////////////////////////////////
    androidTestImplementation(project(":core:testing"))
}