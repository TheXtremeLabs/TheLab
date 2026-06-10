plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
        getByName("release") {
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
        getByName("androidTest") {
            manifest.srcFile("${project.rootDir}/AndroidManifest.xml")
        }
    }

    namespace = "com.riders.thelab.core.common"
}

dependencies {
    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(libs.kotlinx.serialization.json)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // AndroidX
    api(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.biometric)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)

    // Media
    api(libs.androidx.media)
    api(libs.androidx.media.router)
    implementation(libs.androidx.media.session)
    // Worker
    implementation(libs.androidx.work.ktx)

    // OkHttp
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)



    ///////////////////////////////////
    // Tests Dependencies
    ///////////////////////////////////
    androidTestImplementation(project(":core:testing"))

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.truth)
}