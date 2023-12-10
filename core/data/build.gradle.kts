plugins {
    id("thelab.android.library")
    id("thelab.android.hilt")
    id("thelab.android.room")
    id("thelab.android.library.jacoco")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    kotlin("kapt")
    id("com.google.devtools.ksp")
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
    implementation(project(":core:testing"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // Kotlin
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.core.ktx)
    api(libs.kotlinx.serialization.json)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.tooling)

    // Room
    api(libs.room.ktx)
    api(libs.room.runtime)
    api(libs.room.paging)
    ksp(libs.room.compiler)
    androidTestImplementation(libs.room.testing)

    // Worker & concurrent
    androidTestImplementation(libs.androidx.work.testing)

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

    // api("org.apache.httpcomponents:httpcore:4.4.15")

    // Moshi
    api(libs.moshi)
    api(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)

    api(libs.kotools.types)

    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    //tesImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

    androidTestImplementation(libs.androidx.test.truth)
}