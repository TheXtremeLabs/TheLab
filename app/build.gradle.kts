plugins {
    /**
     * You should use `apply false` in the top-level build.gradle file
     * to add a Gradle plugin as a build dependency, but not apply it to the
     * current (root) project. You should not use `apply false` in sub-projects.
     * For more information, see
     * Applying external plugins with same version to subprojects.
     */

    /**
     * Defined in build-logic/convention/build.gradle.kts class
     */
    id("thelab.android.application")
    id("thelab.android.application.compose")
    id("thelab.android.application.jacoco")
    id("thelab.android.hilt")
    id("jacoco")
    id("androidx.navigation.safeargs.kotlin")
    id("thelab.firebase")
}

android {

    /**
     * Defined in AndroidApplicationConventionPlugin class
     */
    // compileSdk = ConfigData.compileSdkVersion

    useLibrary("org.apache.http.legacy")

    ndkVersion = "21.3.6528147"

    defaultConfig {
        applicationId = "com.riders.thelab"

        /**
         * Defined in AndroidApplicationConventionPlugin class
         */
        /*minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName*/

        // Enabling multidex support.
        multiDexEnabled = true

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = false

            //applicationIdSuffix = LabBuildType.DEBUG.applicationIdSuffix
        }
        getByName("release") {
            isDebuggable = false

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true  // Enables code shrinking for the release build type.

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //applicationIdSuffix = LabBuildType.RELEASE.applicationIdSuffix
        }
    }

    packagingOptions {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


    buildFeatures {
        // Determines whether to support Data Binding.
        // Note that the dataBinding.enabled property is now deprecated.
        dataBinding = true
    }

    lint {
        abortOnError = false
        ignoreWarnings = true
        quiet = true
    }

    namespace = "com.riders.thelab"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    //Kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.parcelize)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.play.services)

    // AndroidX
    implementation(Dependencies.multiDex)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.activityKtx)
    implementation(Dependencies.fragmentKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.material)
    implementation(Dependencies.palette)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.cardView)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.springForce)
    implementation(Dependencies.viewPager2)
    implementation(Dependencies.window)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.compiler)
    implementation(Dependencies.composeRuntime)
    implementation(Dependencies.composeRuntimeLiveData)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeMaterial3)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeAnimation)
    debugImplementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.composeMaterialIcons)
    implementation(Dependencies.composeMaterialIconsExtended)
    implementation(Dependencies.composeMaterialWindowSizeClass)
    implementation(Dependencies.composeFonts)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeLifecycleRuntime)
    implementation(Dependencies.composeLifecycleViewModel)
    implementation(Dependencies.composeLiveData)
    implementation(Dependencies.composeNavigation)

    // Coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.svg)

    // Navigation
    implementation(Dependencies.navigationKTX)
    implementation(Dependencies.navigationFragmentKTX)

    // CameraX
    implementation(Dependencies.camera2)
    implementation(Dependencies.cameraLifecyle)
    implementation(Dependencies.cameraView)
    implementation(Dependencies.cameraExtensions)

    // Media
    implementation(Dependencies.media)
    implementation(Dependencies.mediaRouter)
    implementation(Dependencies.media3exoPlayer)
    implementation(Dependencies.media3UI)

    // Auto fill
    implementation(Dependencies.autoFill)

    // Room
    implementation(Dependencies.roomKtx)
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)

    // Worker & concurrent
    implementation(Dependencies.workerRuntime)
    implementation(Dependencies.workerMultiprocess)
    implementation(Dependencies.concurrent)

    // Lifecycle
    implementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.lifecycleLiveData)
    implementation(Dependencies.lifecycleViewModelSavedState)
    kapt(Dependencies.lifecycleCompiler)
    implementation(Dependencies.lifecycleService)
    implementation(Dependencies.lifecycleProcess)

    // Datastore and Preferences
    implementation(Dependencies.datastore)
    implementation(Dependencies.datastorePreferences)
    implementation(Dependencies.preferences)

    /* Hilt - We are going to use hilt.android which includes
     * support for Activity and fragment injection so we need to include
     * the following dependencies */
    // Hilt
    //implementation(Dependencies.hilt)
    //kapt(Dependencies.hiltCompiler)
    kapt(Dependencies.hiltAndroidXCompiler)
    implementation(Dependencies.hiltWork)
    kapt(Dependencies.hiltKaptAndroidXCompiler)
    implementation(Dependencies.hiltComposeNavigation)

    // Google Location (Maps / Places)
    implementation(Dependencies.maps)
    implementation(Dependencies.mapsUtils)
    implementation(Dependencies.location)
    implementation(Dependencies.places)

    // Google API
    implementation(Dependencies.googleBasePlayServices)
    implementation(Dependencies.googleAPIClientJackson2)
    implementation(Dependencies.googleHttpClient)
    implementation(Dependencies.googleAPIClient)
    implementation(Dependencies.googleAPIClientAndroid)
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-jetty
    implementation(Dependencies.googleAPIClientOAuthJetty)
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-java6
    implementation(Dependencies.googleAPIClientOAuthJava6)
    implementation(Dependencies.googleAPIDrive)
    implementation(Dependencies.playServicesAuth)

    // Google ML Kit
    implementation(Dependencies.barcodeScanningML)
    implementation(Dependencies.objectDetectionML)
    implementation(Dependencies.objectDetectionCustomML)

    // Firebase
    implementation(platform(Dependencies.firebasePlatform))
    implementation(Dependencies.firebaseAnalytics)
    implementation(Dependencies.firebaseAuth)
    implementation(Dependencies.firebaseCrashlytics)
    implementation(Dependencies.firebaseMessaging)
    implementation(Dependencies.firebasePerf)
    implementation(Dependencies.firebaseCloudStorage)
    implementation(Dependencies.firebaseDatabase)
    implementation(Dependencies.firebaseAds)

    /* Retrofit using RxJava3, Okhttp, Okhttp logging interceptor, Moshi  */
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitConverterMoshi)

    // OkHttp
    // define a BOM and its version
    implementation(platform(Dependencies.okHttpBom))
    // define any required OkHttp artifacts without version
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLogging)

    // Moshi
    implementation(Dependencies.moshi)
    implementation(Dependencies.moshiKotlin)
    kapt(Dependencies.moshiKotlinCodeGen)

    // MPAndroidChart
    implementation(Dependencies.androidChart)

    // Dexter
    implementation(Dependencies.dexter)

    // Glide
    implementation(Dependencies.glide)
    kapt(Dependencies.glideAnnotation)
    implementation(Dependencies.blurry)
    implementation(Dependencies.glideTransformation)
    implementation("com.github.skydoves:landscapist-glide:2.1.1")

    // Lottie
    implementation(libs.lottie)
    implementation(libs.lottie.compose)

    //ThreeTen : Alternative to Android Calendar API
    implementation(libs.threeten)

    // Kotools Types
    implementation(libs.kotools.types)


    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(TestsDependencies.junit)
    androidTestImplementation(TestsDependencies.testExtJUnit)
    androidTestImplementation(TestsDependencies.espressoCore)
    androidTestImplementation(TestsDependencies.mockitoCore)
    androidTestImplementation(TestsDependencies.mockitoAndroid)

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.3")

    androidTestImplementation(TestsDependencies.workerTest)

    androidTestImplementation(TestsDependencies.hiltAndroidTesting)
    kaptAndroidTest(TestsDependencies.hiltKaptAndroidCompiler)
    kaptAndroidTest(TestsDependencies.hiltKaptCompiler)
}