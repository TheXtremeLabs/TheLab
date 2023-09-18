plugins {
    id("thelab.android.library")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    id("thelab.android.library.jacoco")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.core.ui"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    //api(project(":feature:weather"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    implementation(libs.kotlinx.datetime)

    // AndroidX
    api(libs.androidx.core.ktx)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.appcompat)

    // Compose
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.animation)
    api(libs.androidx.compose.fonts)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.icons)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3.windowSizeClass)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.lifecycle.viewModelCompose)
    api(libs.androidx.metrics)
    api(libs.androidx.tracing.ktx)

    debugApi(libs.androidx.compose.ui.tooling)

    // AndroidX
    implementation(libs.androidx.browser)
    api(libs.androidx.core.ktx)
    api(libs.material)

    // Dexter
    api(libs.dexter)

    // Glide
    api(libs.glide)
    kapt(libs.glide.compiler)
    api(libs.glide.blurry)
    api(libs.glide.transformation)
    api(libs.glide.landscapist)

    // Lottie
    api(libs.lottie)
    api(libs.lottie.compose)

    // Coil
    api(libs.coil.kt)
    api(libs.coil.kt.compose)
    api(libs.coil.kt.svg)

    api(libs.kotools.types)

    androidTestImplementation(project(":core:testing"))
}