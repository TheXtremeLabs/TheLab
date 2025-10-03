plugins {
    alias(libs.plugins.thelab.library)
}

android {
    namespace = "com.riders.thelab.core.location"
    compileSdk = 36

    defaultConfig {
        minSdk = AndroidConfiguration.Sdk.MIN

        testApplicationId = "com.riders.thelab.core.location.test"

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
    implementation(project(":core:testing"))

    // AndroidX
    implementation(libs.androidx.concurrent)
    // Worker
    implementation(libs.androidx.work.ktx)
    androidTestImplementation(libs.androidx.work.testing)

    // Google Play Location & Maps
    api(libs.location)
    api(libs.maps)
    api(libs.maps.compose)
    api(libs.maps.compose.utils)
    api(libs.maps.compose.widgets)
    api(libs.maps.utils)

    implementation(libs.google.guava)
    implementation(libs.google.guava.listenablefuture)


    ////////////////////////////////////////////
    // Tests dependencies
    ////////////////////////////////////////////
    androidTestImplementation(project(":core:testing"))

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.uiautomator)
}