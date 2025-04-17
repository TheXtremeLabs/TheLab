plugins {
    alias(libs.plugins.thelab.feature)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    namespace = "com.riders.thelab.feature.mlkit"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:camera"))
    implementation(project(":core:permissions"))
    implementation(project(":core:ui"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.google.guava)
    
    // ML Kit
    implementation(libs.google.mlkit.barcode.scanning)
    implementation(libs.google.mlkit.objectdetection)
    implementation(libs.google.mlkit.objectdetection.custom)

    implementation(libs.volley)
}