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

    api(project(":feature:artists"))
    api(project(":feature:biometric"))
    api(project(":feature:bluetooth"))
    api(project(":feature:colors"))
    api(project(":feature:download"))
    api(project(":feature:flightaware"))
    api(project(":feature:googledrive"))
    api(project(":feature:kat"))
    api(project(":feature:koin"))
    api(project(":feature:locationonmaps"))
    api(project(":feature:lottie"))
    api(project(":feature:lottie"))
    api(project(":feature:mlkit"))
    api(project(":feature:musicrecognition"))
    api(project(":feature:nfc"))
    api(project(":feature:palette"))
    api(project(":feature:schedule"))
    api(project(":feature:settings"))
    api(project(":feature:songplayer"))
    api(project(":feature:splashscreen"))
    api(project(":feature:streaming"))
    api(project(":feature:tabs"))
    api(project(":feature:theaters"))
    api(project(":feature:transitions"))
    api(project(":feature:videocall"))
    api(project(":feature:weather"))
    api(project(":feature:youtube"))

    ///////////////////////////////////
    // Tests Dependencies
    ///////////////////////////////////
    androidTestImplementation(project(":core:testing"))
}