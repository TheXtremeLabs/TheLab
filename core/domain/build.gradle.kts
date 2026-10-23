plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.kotlin.serialization)
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

    namespace = "com.riders.thelab.core.domain"
}

dependencies {

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore) {
        exclude(module = "protobuf-javalite")
        exclude(module = "protobuf-java")
        exclude(module = "proto-google-common-protos")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.storage) {
        exclude(module = "protobuf-javalite")
        exclude(module = "protobuf-java")
        exclude(module = "proto-google-common-protos")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    /* Retrofit using RxJava3, Okhttp, Okhttp logging interceptor, Gson, Moshi, Serialization  */
    api(platform(libs.retrofit.bom))
    api(libs.retrofit.core)
    api(libs.retrofit.kotlin.serialization)

    // OkHttp
    // define a BOM and its version
    api(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    api(libs.okhttp)
    api(libs.okhttp.logging)

    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.truth)

    // Kotlin
    androidTestImplementation(libs.kotlinx.coroutines.test)
}