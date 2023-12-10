plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
    kotlin("kapt")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.feature.kat"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))


    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    // AndroidX
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)

    // Worker & concurrent
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.work.multiprocess)
    implementation(libs.androidx.concurrent)
    androidTestImplementation(libs.androidx.work.testing)

    // Firebase
//    implementation(platform(libs.firebase.bom))


    /* Hilt - We are going to use hilt.android which includes
     * support for Activity and fragment injection so we need to include
     * the following dependencies */
    // Hilt
    //implementation(Dependencies.hilt)
    //kapt(Dependencies.hiltCompiler)
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)

    // OkHttp
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)

//    androidTestImplementation(project(":core:testing"))
}