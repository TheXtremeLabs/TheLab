plugins {
    alias(libs.plugins.thelab.feature)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.thelab.hilt)
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

    // Hilt
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    // OkHttp
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)

//    androidTestImplementation(project(":core:testing"))
}