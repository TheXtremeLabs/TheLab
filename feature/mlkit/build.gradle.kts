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

    // Accompanist
    implementation(libs.accompanist.adaptive)
    implementation(libs.accompanist.drawablepainter)
    implementation(libs.accompanist.permissions)

    // Coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)

    implementation(libs.google.guava)

    // ML Kit
    implementation(libs.google.mlkit.barcode.scanning)
    implementation(libs.google.mlkit.common)
    implementation(libs.google.mlkit.digital.ink.recognition)
    implementation(libs.google.mlkit.document.scanner)
    implementation(libs.google.mlkit.face.detection)
    implementation(libs.google.mlkit.face.mesh.detection)
    implementation(libs.google.mlkit.objectdetection)
    implementation(libs.google.mlkit.objectdetection.custom)
    implementation(libs.google.mlkit.pose.detection)
    implementation(libs.google.mlkit.segmentation.selfie)
    implementation(libs.google.mlkit.text.recognition)
    implementation(libs.google.mlkit.text.recognition.bundled)
    implementation(libs.google.play.services.text.recognition)
    implementation(libs.google.play.services.text.recognition.common)
    implementation(libs.google.mlkit.translate)
    implementation(libs.google.mlkit.vision.common)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewModel.ktx)
    implementation(libs.androidx.lifecycle.viewModel.savedState)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    ksp(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.lifecycle.process)

    // Worker & concurrent
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.work.multiprocess)
    implementation(libs.androidx.concurrent)
    androidTestImplementation(libs.androidx.work.testing)

    implementation(libs.volley)


    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}