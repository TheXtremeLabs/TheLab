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
    namespace = "com.riders.thelab.feature.streaming"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:data"))
    implementation(project(":core:permissions"))
    implementation(project(":core:player"))
    implementation(project(":core:ui"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(libs.kotlinx.coroutines.android)

    // AndroidX
    api(libs.androidx.core.ktx)

    androidTestImplementation(project(":core:testing"))
}