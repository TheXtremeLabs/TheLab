plugins {
    id("thelab.android.library")
    id("thelab.android.library.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    namespace = "com.riders.thelab.core.player"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:ui"))

    api(libs.androidx.media)
    api(libs.androidx.media.common)
    implementation(libs.androidx.media.cast)
    implementation(libs.androidx.media.database)
    implementation(libs.androidx.media.decoder)
    api(libs.androidx.media.exoplayer)
    implementation(libs.androidx.media.exoplayer.dash)
    implementation(libs.androidx.media.exoplayer.hls)
    implementation(libs.androidx.media.exoplayer.rtsp)
    implementation(libs.androidx.media.extractor)
    api(libs.androidx.media.session)
    api(libs.androidx.media.ui)
    implementation(libs.youtube.player)

    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    androidTestImplementation(project(":core:testing"))
}