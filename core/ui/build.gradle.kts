import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.thelab.library.jacoco)
    alias(libs.plugins.protobuf)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "google/protobuf/*.proto"
            excludes += "google/type/color.proto"
            excludes += "src/google/protobuf/duration.proto"
            excludes += "src/google/protobuf/empty.proto"
            excludes += "src/google/protobuf/type.proto"
            excludes += "src/google/protobuf/timestamp.proto"
        }
    }

    namespace = "com.riders.thelab.core.ui"
}

configurations.all {
    exclude(group = "com.google.protobuf", module = "protobuf-lite")
}


protobuf {
    protoc {
        //artifact = "com.google.protobuf:protoc:4.33.0"
        artifact = "${libs.protobuf.protoc.get()}"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }

                create("kotlin"){
                    option("lite")
                }
            }
        }
    }
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
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.android)
    api(libs.androidx.compose.material3.adaptative)
    api(libs.androidx.compose.material3.adaptative.layout)
    api(libs.androidx.compose.material3.adaptative.navigation)
    api(libs.androidx.compose.material3.windowSizeClass)
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

    // Coil
    api(libs.coil.kt)
    api(libs.coil.kt.compose)
    api(libs.coil.kt.network.cache.control)
    api(libs.coil.kt.network.okhttp)
    api(libs.coil.kt.svg)

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

    // Protobuf
    implementation(platform(libs.protobuf.bom))
    implementation(libs.protobuf.javalite)
    implementation(libs.protobuf.kotlin.lite)


    api(libs.kotools.types)


    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    androidTestImplementation(project(":core:testing"))

    androidTestApi(libs.androidx.compose.ui.test)
    androidTestDebugApi(libs.androidx.compose.ui.tooling)
    androidTestDebugApi(libs.androidx.compose.ui.testManifest)
}