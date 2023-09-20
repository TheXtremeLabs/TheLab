plugins {
    id("thelab.android.library")
    id("thelab.android.hilt")
    id("thelab.android.room")
    id("thelab.android.library.jacoco")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    kotlin("kapt")
}

android {

    useLibrary("org.apache.http.legacy")

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    namespace = "com.riders.thelab.core.data"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.core.ktx)
    api(libs.room.ktx)
    api(libs.kotlinx.serialization.json)

    // Compose
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui.tooling)

    // Room
    /*api(libs.room.ktx)
    api(libs.room.runtime)
    kapt(libs.room.compiler)*/

    // Firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.auth)
    api(libs.firebase.crashlytics)
    api(libs.firebase.database)
    api(libs.firebase.messaging)
    api(libs.firebase.storage)

    /* Retrofit using RxJava3, Okhttp, Okhttp logging interceptor, Moshi  */
    api(libs.retrofit.core)
    api(libs.retrofit.moshi)
    api(libs.retrofit.kotlin.serialization)

    // OkHttp
    // define a BOM and its version
    api(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    api(libs.okhttp)
    api(libs.okhttp.logging)

    api("org.apache.httpcomponents:httpcore:4.4.15")

    // Moshi
    api(libs.moshi)
    api(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)

    api(libs.kotools.types)

    androidTestImplementation(project(":core:testing"))
}