plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.feature.palette"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage) {
        exclude(module = "protobuf-javalite")
        exclude(module = "protobuf-java")
        exclude(module = "proto-google-common-protos")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    androidTestImplementation(project(":core:testing"))
}