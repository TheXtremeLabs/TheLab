plugins {
    alias(libs.plugins.thelab.feature)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.thelab.ktor)
    alias(libs.plugins.ksp)
    //alias(libs.plugins.ktor)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    namespace = "com.riders.thelab.feature.koin"
}

ksp {
    // Compile Time Checks
    // Koin Annotations allows to check your Koin configuration at compile time.
    // This is available by using the following Gradle option:
    arg("KOIN_CONFIG_CHECK", "true")

    // Remove this warning :
    // [ksp] [Deprecation] 'defaultModule' generation is deprecated.
    // Use KSP argument arg("KOIN_DEFAULT_MODULE","true") to activate default module generation.
    arg("KOIN_DEFAULT_MODULE", "true")
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
    // Kotlin
    implementation(libs.kotlin.parcelize)
    implementation(libs.kotlinx.serialization.json)

    // AndroidX
    implementation(libs.androidx.core.ktx)

    // koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.core.viewmodel)
    implementation(libs.koin.core.coroutines)
    ksp(libs.koin.ksp)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
//    implementation(libs.koin.androidx.startup)
    implementation(libs.koin.androidx.workmanager)
    implementation(platform(libs.koin.annotations.bom))
    implementation(libs.koin.annotations)
    // Koin Test features
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.koin.test)
    // Koin for JUnit 4
    testImplementation(libs.koin.test.junit4)
    androidTestImplementation(libs.koin.test.junit4)
    // Koin for JUnit 5
    testImplementation(libs.koin.test.junit5)
    androidTestImplementation(libs.koin.test.junit5)

    // Ktor
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.slf4j.android)
    implementation(libs.napier)


    // OkHttp
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    // define any required OkHttp artifacts without version
    implementation(libs.okhttp)


    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.truth)
}