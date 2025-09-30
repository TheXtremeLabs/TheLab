plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.library.compose)
//    alias(libs.plugins.thelab.library.jacoco)
    alias(libs.plugins.thelab.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.riders.thelab.core.testing.TheLabHiltRunner"
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

    // AndroidX
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.test.runner)

    // Compose
    api(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    // Room
    androidTestImplementation(libs.room.testing)

    // Worker & concurrent
    androidTestImplementation(libs.androidx.work.testing)

    // Hilt
    implementation(libs.hilt.android.testing)
    
    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    // Junit
    testApi(libs.junit4)
    androidTestApi(libs.junit4)
    //AndroidX
    // Core library
    androidTestApi(libs.androidx.test.core)
    androidTestApi(libs.androidx.test.ext)
    // AndroidJUnitRunner and JUnit Rules
    androidTestApi(libs.androidx.test.rules)
    androidTestApi(libs.androidx.test.runner)
    // Assertions
    testApi(libs.androidx.test.truth)
    androidTestApi(libs.androidx.test.truth)
    androidTestApi(libs.androidx.test.uiautomator)
    // Espresso
    androidTestApi(libs.androidx.test.espresso.core)
    androidTestApi(libs.androidx.test.espresso.contrib)
    androidTestApi(libs.androidx.test.espresso.idling.concurrent)
    androidTestApi(libs.androidx.test.espresso.idling.resources)
    androidTestApi(libs.androidx.test.espresso.intents)
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