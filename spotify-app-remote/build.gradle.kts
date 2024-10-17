plugins {
    id("thelab.android.library")
}

android {
    namespace = "com.riders.spotify_app_remote"
    compileSdk = 35

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    /*
     * https://stackoverflow.com/questions/60878599/error-building-android-library-direct-local-aar-file-dependencies-are-not-supp
     */
    configurations.maybeCreate("default")
    // To fix build with CI we must use library as we can't implement aar directly in project
    artifacts.add("default", file("spotify-app-remote-release-0.8.0.aar"))
}