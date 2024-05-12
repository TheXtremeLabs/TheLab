plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    namespace = "com.riders.thelab.feature.youtube"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:permissions"))
    implementation(project(":core:ui"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // AndroidX
    implementation(libs.androidx.core.ktx)


    /* Hilt - We are going to use hilt.android which includes
     * support for Activity and fragment injection so we need to include
     * the following dependencies */
    // Hilt
    // The others dependencies has been added into the Hilt plugin convention class
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    androidTestImplementation(project(":core:testing"))
}