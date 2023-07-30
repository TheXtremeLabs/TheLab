plugins {
    id("thelab.android.library")
    id("thelab.android.hilt")
    id("thelab.android.library.jacoco")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
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

    // Room
    api(libs.room.ktx)
    api(libs.room.runtime)
    kapt(libs.room.compiler)

    // Firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.auth)
    api(libs.firebase.crashlytics)
    api(libs.firebase.database)
    api(libs.firebase.messaging)
    api(libs.firebase.storage)

    /* Retrofit using RxJava3, Okhttp, Okhttp logging interceptor, Moshi  */
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.kotlin.serialization)

    // OkHttp
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)

     androidTestImplementation(project(":core:testing"))
}