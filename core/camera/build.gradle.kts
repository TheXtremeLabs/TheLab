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
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    api(libs.androidx.camera)
    api(libs.androidx.camera.compose)
    api(libs.androidx.camera.effects)
    api(libs.androidx.camera.extensions)
    api(libs.androidx.camera.lifecycle)
    api(libs.androidx.camera.view)
    api(libs.androidx.camera.video)

    // Google
    api(libs.google.guava)
    implementation(libs.google.mlkit.vision.common)
    implementation(libs.google.play.services.vision)
    implementation(libs.google.play.services.vision.common)

    ////////////////////////////////////////////
    // Tests dependencies
    ////////////////////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}