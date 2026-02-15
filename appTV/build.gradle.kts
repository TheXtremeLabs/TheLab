plugins {
    /**
     * Defined in build-logic/convention/build.gradle.kts class
     */
    alias(libs.plugins.thelab.application)
    alias(libs.plugins.thelab.application.compose)
    alias(libs.plugins.thelab.firebase)
    alias(libs.plugins.thelab.hilt)
}

val versionMajor: Int = 0
val versionMinor: Int = 0
val versionPatch: Int = 1

/**
 * Log events in console
 *
 * @param tag
 * @param message
 * @return
 */
fun log(tag: String, message: String) {
    println("---> KotlinDSL script logs | $tag | $message")
}

android {
    namespace = "com.riders.thelab.tv"

    defaultConfig {

        manifestPlaceholders += mapOf(
            "redirectHostName" to "com.riders.thelab.tv",
            "redirectSchemeName" to "com.riders.thelab.tv",
            "redirectPathPattern" to "//com.riders.thelab.tv"
        )
        applicationId = "com.riders.thelab.tv"

        versionCode = 2000 * versionMajor + 100 * versionMinor + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"

        // Enabling multidex support.
        multiDexEnabled = true

        ndk {
            abiFilters.clear()
            //noinspection ChromeOsAbiSupport
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }
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

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(project(":feature:login"))
    implementation(project(":feature:home"))

    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.multidex)

    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    // Firebase App Check
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.appcheck.playintegrity)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}