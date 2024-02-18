plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.riders.thelab.feature.mlkit"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:ui"))

    implementation(libs.androidx.camera)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation(libs.google.barcode.scanning)


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
}