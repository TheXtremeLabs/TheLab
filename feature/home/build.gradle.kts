plugins {
    id("thelab.android.feature")
    id("thelab.android.library.compose")
    alias(libs.plugins.kotlin.serialization)
    id("thelab.android.hilt")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.riders.thelab.feature.home"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(project(":feature:artists"))
    implementation(project(":feature:biometric"))
    implementation(project(":feature:bluetooth"))
    implementation(project(":feature:colors"))
    implementation(project(":feature:download"))
    implementation(project(":feature:flightaware"))
    implementation(project(":feature:googledrive"))
    implementation(project(":feature:kat"))
    implementation(project(":feature:koin"))
    implementation(project(":feature:locationonmaps"))
    implementation(project(":feature:lottie"))
    implementation(project(":feature:lottie"))
    implementation(project(":feature:mlkit"))
    implementation(project(":feature:musicrecognition"))
    implementation(project(":feature:palette"))
    implementation(project(":feature:schedule"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:songplayer"))
    implementation(project(":feature:streaming"))
    implementation(project(":feature:tabs"))
    implementation(project(":feature:theaters"))
    implementation(project(":feature:transitions"))
    implementation(project(":feature:weather"))
    implementation(project(":feature:youtube"))

    ///////////////////////////////////
    // Tests Dependencies
    ///////////////////////////////////
    androidTestImplementation(project(":core:testing"))
}