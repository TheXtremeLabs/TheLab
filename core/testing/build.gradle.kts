plugins {
    id(libs.plugins.thelab.library)
    id(libs.plugins.thelab.library.compose)
    id(libs.plugins.thelab.library.jacoco)
    id(libs.plugins.thelab.hilt)
    id(libs.plugins.thelab.test)
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
    //Kotlin
    api(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

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
    testApi(libs.junit4)
    // Assertions
    testApi(libs.androidx.test.truth)

    // Core library
    androidTestApi(libs.androidx.test.core)

    // AndroidJUnitRunner and JUnit Rules
    androidTestApi(libs.androidx.test.rules)
    androidTestApi(libs.androidx.test.runner)

    // Assertions
    androidTestApi(libs.androidx.test.ext)
    androidTestApi(libs.androidx.test.truth)
    androidTestApi(libs.androidx.test.uiautomator)

    androidTestApi(libs.androidx.test.ext)
    androidTestApi(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.mockito.android)

    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}