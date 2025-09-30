plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.library.compose)
//    alias(libs.plugins.thelab.library.jacoco)
    alias(libs.plugins.thelab.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.core.testing"
}

dependencies {
    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.test)

    // Compose
    api(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    // Room
    androidTestImplementation(libs.room.testing)

    // Worker & concurrent
    androidTestImplementation(libs.androidx.work.testing)

    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    // Junit
    testApi(libs.junit4)
    androidTestImplementation(libs.junit4)
    //AndroidX
    // Core library
    androidTestApi(libs.androidx.test.core)
    // AndroidJUnitRunner and JUnit Rules
    androidTestApi(libs.androidx.test.rules)
    androidTestApi(libs.androidx.test.runner)
    // Assertions
    androidTestApi(libs.androidx.test.ext)
    testApi(libs.androidx.test.truth)
    androidTestApi(libs.androidx.test.truth)
    androidTestApi(libs.androidx.test.uiautomator)
    // Espresso
    androidTestApi(libs.androidx.test.espresso.core)
    // Compose
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    // Kotlin
    testApi(platform(libs.kotlin.bom))
    testApi(libs.kotlin.stdlib)
    testApi(libs.kotlin.reflect)
    testApi(libs.kotlin.test)
    androidTestApi(libs.kotlin.test)
    testApi(platform(libs.kotlinx.coroutines.bom))
    testApi(libs.kotlinx.coroutines.core)
    testApi(libs.kotlinx.coroutines.test)
    androidTestApi(platform(libs.kotlinx.coroutines.bom))
    androidTestApi(libs.kotlinx.coroutines.core)
    androidTestApi(libs.kotlinx.coroutines.test)
    // Mockito
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.mockito.android)
}