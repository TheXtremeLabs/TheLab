plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.riders.thelab.feature.videocall"

    defaultConfig {
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
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // StreamSDK
     implementation(platform(libs.stream.bom))
    implementation(libs.bundles.stream)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}