plugins {
    id("thelab.android.library")
    id("thelab.android.library.compose")
    id("thelab.android.hilt")
}

android {
    namespace = "com.riders.thelab.core.analytics"
}

dependencies {
    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    //Kotlin
    api(libs.kotlinx.coroutines.android)

    // AndroidX
    api(libs.androidx.core.ktx)
    api(libs.androidx.compose.runtime)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ads)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.storage)
}
