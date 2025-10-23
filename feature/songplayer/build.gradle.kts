plugins {
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.thelab.feature)
    alias(libs.plugins.thelab.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.feature.songplayer"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:permissions"))
    implementation(project(":core:player"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(libs.kotlinx.coroutines.android)

    // AndroidX
    implementation(libs.androidx.core.ktx)

    androidTestImplementation(project(":core:testing"))
}