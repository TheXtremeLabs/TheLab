import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.thelab.library.jacoco)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.core.ui"
}

composeCompiler {
    featureFlags.addAll(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.datetime)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment)
    api(libs.androidx.appcompat)
    api(libs.androidx.palette)
    api(libs.material)
    api(libs.androidx.browser)
    api(libs.androidx.glance.appwidget)
    api(libs.androidx.glance.material3)

    // Accompanist
    api(libs.accompanist.adaptive)
    api(libs.accompanist.drawablepainter)
    api(libs.accompanist.permissions)

    // Compose
    api(libs.androidx.compose.compiler)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)
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
    api(libs.androidx.compose.material3.android)
    api(libs.androidx.compose.material3.adaptative)
    api(libs.androidx.compose.material3.adaptative.layout)
    api(libs.androidx.compose.material3.adaptative.navigation)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)
    api(libs.androidx.compose.runtime.tracing)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.lifecycle.viewModelCompose)
    api(libs.androidx.metrics)
    api(libs.androidx.tracing.ktx)

    api(libs.androidx.tv.foundation)
    api(libs.androidx.tv.material)

    // Glide
    api(libs.glide)
    ksp(libs.glide.compiler)
    api(libs.glide.compose)
    api(libs.glide.blurry)
    api(libs.glide.transformation)
    api(libs.glide.landscapist)

    // Lottie
    api(libs.lottie)
    api(libs.lottie.compose)

    // Coil
    api(libs.coil.kt)
    api(libs.coil.kt.compose)
    api(libs.coil.kt.okhttp)
    api(libs.coil.kt.svg)

    api(libs.kotools.types)


    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    androidTestImplementation(project(":core:testing"))

    androidTestApi(libs.androidx.compose.ui.test)
    androidTestDebugApi(libs.androidx.compose.ui.tooling)
    androidTestDebugApi(libs.androidx.compose.ui.testManifest)
}