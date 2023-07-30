plugins {
    id("thelab.android.library")
    id("thelab.android.library.compose")
    id("thelab.android.library.jacoco")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.core.common"
}

dependencies {
    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(platform(libs.kotlin.bom))
}