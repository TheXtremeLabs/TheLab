plugins {
    id("thelab.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.core.common"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:data"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(platform(libs.kotlin.bom))
    // Moshi
    api(libs.moshi)
    api(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)
}