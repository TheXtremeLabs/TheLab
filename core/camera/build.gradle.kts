plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.library.compose)
}

android {
    namespace = "com.riders.thelab.core.camera"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:ui"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    implementation(libs.kotlinx.serialization.json)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    // CameraX
    api(libs.androidx.camera)
    api(libs.androidx.camera.compose)
    api(libs.androidx.camera.effects)
    api(libs.androidx.camera.extensions)
    api(libs.androidx.camera.lifecycle)
    api(libs.androidx.camera.mlkit.vision)
    api(libs.androidx.camera.view)
    api(libs.androidx.camera.video)
    // Preferences
    api(libs.androidx.preferences)

    // Google
    api(libs.google.guava)
    api(libs.google.mlkit.vision.common)
    api(libs.google.play.services.vision)
    api(libs.google.play.services.vision.common)

    ////////////////////////////////////////////
    // Tests dependencies
    ////////////////////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}