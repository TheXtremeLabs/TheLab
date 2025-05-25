plugins {
    alias(libs.plugins.thelab.library)
}

android {
    namespace = "com.riders.thelab.core.location"
    compileSdk = 36

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
}

dependencies {

    implementation(project(":core:common"))

    api(libs.location)
    api(libs.maps)
    api(libs.maps.compose)
    api(libs.maps.compose.utils)
    api(libs.maps.compose.widgets)
    api(libs.maps.utils)

    implementation(libs.timber)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}