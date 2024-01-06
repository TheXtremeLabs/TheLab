plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    id("thelab.android.library.jacoco")
    kotlin("kapt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    namespace = "com.riders.thelab.feature.weather"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:permissions"))
    implementation(project(":core:ui"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    // Worker & concurrent
    api(libs.androidx.work.ktx)
    api(libs.androidx.work.multiprocess)
    api(libs.androidx.concurrent)
    androidTestApi(libs.androidx.work.testing)

    /* Hilt - We are going to use hilt.android which includes
     * support for Activity and fragment injection so we need to include
     * the following dependencies */
    // Hilt
    // The others dependencies has been added into the Hilt plugin convention class
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)

    // OkHttp: provided by data module

    // Charts
    api(libs.mpandroidchart)

    androidTestImplementation(project(":core:testing"))
}