plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    id("thelab.android.library.jacoco")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.feature.biometric"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(platform(libs.kotlin.bom))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.biometric)

    // Coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    androidTestImplementation(project(":core:testing"))
}