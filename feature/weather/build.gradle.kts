plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    id("thelab.android.library.jacoco")
    kotlin("kapt")
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


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    // implementation(libs.kotli)

    // Compose
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.metrics)
    api(libs.androidx.tracing.ktx)

    debugApi(libs.androidx.compose.ui.tooling)

    // AndroidX
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)

    // Coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

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