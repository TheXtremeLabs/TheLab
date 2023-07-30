plugins {
    id("thelab.android.feature")
    id("thelab.android.hilt")
    id("thelab.android.firebase")
    id("thelab.android.library.jacoco")
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
    // implementation(project(":core:analytics"))
    // implementation(project(":core:data"))


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

    // androidTestImplementation(project(":core:testing"))
}