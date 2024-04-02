plugins {
    id("thelab.android.library")
}

android {
    namespace = "com.riders.thelab.core.analytics"
}

dependencies {
    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    // Firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.ads)
    api(libs.firebase.analytics)
    api(libs.firebase.auth)
    api(libs.firebase.crashlytics)
    api(libs.firebase.database)
    api(libs.firebase.firestore)
    api(libs.firebase.messaging)
    api(libs.firebase.perf)
    api(libs.firebase.storage)
}
