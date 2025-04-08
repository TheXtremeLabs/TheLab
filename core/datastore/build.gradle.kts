plugins {
    alias(libs.plugins.thelab.library)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    namespace = "com.riders.thelab.core.datastore"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    implementation(libs.kotlinx.coroutines.android)

    // Datastore and Preferences
    api(libs.androidx.datastore.core)
    api(libs.androidx.datastore.preferences)
    api(libs.androidx.preferences)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.truth)

    // Kotlin
    androidTestImplementation(libs.kotlinx.coroutines.test)
}