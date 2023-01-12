import org.gradle.kotlin.dsl.provideDelegate

/**
 * To define plugins
 */
object BuildPlugins {
    // Gradle plugin
    val gradle by lazy { "com.android.tools.build:gradle:${GradlePluginVersions.gradle}" }

    // Kotlin plugin
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${GradlePluginVersions.kotlin}" }
    val kotlinSerialization by lazy { "org.jetbrains.kotlin:kotlin-serialization:${GradlePluginVersions.kotlin}" }

    // Hilt plugin
    val hilt by lazy { "com.google.dagger:hilt-android-gradle-plugin:${GradlePluginVersions.hilt}" }

    // Google Services
    val playServices by lazy { "com.google.gms:google-services:${GradlePluginVersions.playServices}" }

    // Add the Crashlytics Gradle plugin (be sure to add version
    // 2.0.0 or later if you built your app with Android Studio 4.1).
    val crashlytics by lazy { "com.google.firebase:firebase-crashlytics-gradle:${GradlePluginVersions.crashlytics}" }

    // Performance Monitoring plugin
    val performances by lazy { "com.google.firebase:perf-plugin:${GradlePluginVersions.perf}" }

    // Navigation
    val navigation by lazy { "androidx.navigation:navigation-safe-args-gradle-plugin:${GradlePluginVersions.navigation}" }
}

/**
 * To define dependencies
 */
object KotlinDependencies {
    // Kotlin
    val kotlinStdLib by lazy { "org.jetbrains.kotlin:kotlin-stdlib:${DependenciesVersions.kotlin}" }
    val kotlinStdJdk by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${DependenciesVersions.kotlin}" }
    val coroutinesCore by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependenciesVersions.coroutines}" }
    val coroutinesAndroid by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${DependenciesVersions.coroutines}" }
    val kotlinParcelize by lazy { "org.jetbrains.kotlin:kotlin-parcelize-runtime:${DependenciesVersions.kotlin}" }
    val kotlinPlaySevices by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${DependenciesVersions.kotlinPlayServices}" }
}

/**
 * To define dependencies
 */
object Dependencies {

    // General Dependencies
    // AndroidX
    val multiDex by lazy { "androidx.multidex:multidex:${DependenciesVersions.multiDex}" }
    val coreKtx by lazy { "androidx.core:core-ktx:${DependenciesVersions.core}" }
    val activityKtx by lazy { "androidx.activity:activity-ktx:${DependenciesVersions.activity}" }
    val fragmentKtx by lazy { "androidx.fragment:fragment-ktx:${DependenciesVersions.fragment}" }
    val appcompat by lazy { "androidx.appcompat:appcompat:${DependenciesVersions.appCompat}" }
    val material by lazy { "com.google.android.material:material:${DependenciesVersions.material}" }
    val palette by lazy { "androidx.palette:palette-ktx:${DependenciesVersions.palette}" }
    val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${DependenciesVersions.constraintLayout}" }
    val cardView by lazy { "androidx.cardview:cardview:${DependenciesVersions.cardView}" }
    val recyclerView by lazy { "androidx.recyclerview:recyclerview:${DependenciesVersions.recyclerView}" }
    val springForce by lazy { "androidx.dynamicanimation:dynamicanimation:${DependenciesVersions.dynamicAnimation}" }
    val viewPager2 by lazy { "androidx.viewpager2:viewpager2:${DependenciesVersions.viewPager2}" }

    // Compose
    val composeBom by lazy { "androidx.compose:compose-bom:${DependenciesVersions.composeBom}" }
    val composeCompiler by lazy { "androidx.compose.compiler:compiler:${DependenciesVersions.composeCompiler}" }
    val composeRuntime by lazy { "androidx.compose.runtime:runtime:${DependenciesVersions.compose}" }
    val composeRuntimeLiveData by lazy { "androidx.compose.runtime:runtime-livedata:${DependenciesVersions.compose}" }

    // Material Design
    val composeMaterial by lazy { "androidx.compose.material:material" }
    val composeMaterial3 by lazy { "androidx.compose.material3:material3" }

    // Import other Compose libraries without version numbers
    val composeFoundation by lazy { "androidx.compose.foundation:foundation" }

    // such as input and measurement/layout
    val composeUi by lazy { "androidx.compose.ui:ui" }
    val composeAnimation by lazy { "androidx.compose.animation:animation:${DependenciesVersions.composeAnimation}" }

    // Android Studio Preview support
    val composeUiToolingPreview by lazy { "androidx.compose.ui:ui-tooling-preview" }
    val composeUiTooling by lazy { "androidx.compose.ui:ui-tooling" }

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    val composeMaterialIcons by lazy { "androidx.compose.material:material-icons-core" }
    val composeMaterialIconsExtended by lazy { "androidx.compose.material:material-icons-extended" }
    val composeMaterialWindowSizeClass by lazy { "androidx.compose.material3:material3-window-size-class" }
    val composeFonts by lazy { "androidx.compose.ui:ui-text-google-fonts:${DependenciesVersions.compose}" }
    val composeActivity by lazy { "androidx.activity:activity-compose:${DependenciesVersions.activity}" }
    val composeLifecycleViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha03" }
    val composeLiveData by lazy { "androidx.compose.runtime:runtime-livedata" }
    val composeNavigation by lazy { "androidx.navigation:navigation-compose:2.5.3" }

    // Navigation
    val navigationKTX by lazy { "androidx.navigation:navigation-ui-ktx:${DependenciesVersions.navigationUI}" }
    val navigationFragmentKTX by lazy { "androidx.navigation:navigation-fragment-ktx:${DependenciesVersions.navigationFragment}" }

    // Camera
    val camera2 by lazy { "androidx.camera:camera-camera2:${DependenciesVersions.camera}" }
    val cameraLifecyle by lazy { "androidx.camera:camera-lifecycle:${DependenciesVersions.camera}" }
    val cameraView by lazy { "androidx.camera:camera-view:${DependenciesVersions.cameraAlpha}" }
    val cameraExtensions by lazy { "androidx.camera:camera-extensions:${DependenciesVersions.cameraAlpha}" }

    // Media
    val media by lazy { "androidx.media:media:${DependenciesVersions.media}" }
    val mediaRouter by lazy { "androidx.mediarouter:mediarouter:${DependenciesVersions.mediaRouter}" }
    val media3exoPlayer by lazy { "androidx.media3:media3-exoplayer:${DependenciesVersions.media3}" }
    val media3UI by lazy { "androidx.media3:media3-ui:${DependenciesVersions.media3}" }

    // Auto fill
    val autoFill by lazy { "androidx.autofill:autofill:${DependenciesVersions.autoFill}" }

    /* Android Architecture Component - Room Persistence Lib  */
    // Room
    val roomKtx by lazy { "androidx.room:room-ktx:${DependenciesVersions.room}" }
    val roomRuntime by lazy { "androidx.room:room-runtime:${DependenciesVersions.room}" }
    val roomCompiler by lazy { "androidx.room:room-compiler:${DependenciesVersions.room}" }
    val roomRxJava2 by lazy { "androidx.room:room-rxjava2:${DependenciesVersions.room}" }
    val roomGuava by lazy { "androidx.room:room-guava:${DependenciesVersions.room}" }
    val roomTesting by lazy { "androidx.room:room-testing:${DependenciesVersions.room}" }

    // Worker & concurrent
    val workerRuntime by lazy { "androidx.work:work-runtime-ktx:${DependenciesVersions.worker}" }
    val workerMultiprocess by lazy { "androidx.work:work-multiprocess:${DependenciesVersions.worker}" }
    val concurrent by lazy { "androidx.concurrent:concurrent-futures:${DependenciesVersions.concurrent}" }

    // Lifecycle, ViewModel, LiveData, Lifecycles only (without ViewModel or LiveData}, Annotation processor
    val lifecycleRuntime by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.lifecycle}" }
    val lifecycleViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${DependenciesVersions.lifecycle}" }
    val lifecycleLiveData by lazy { "androidx.lifecycle:lifecycle-livedata-ktx:${DependenciesVersions.lifecycle}" }
    val lifecycleViewModelSavedState by lazy { "androidx.lifecycle:lifecycle-viewmodel-savedstate:${DependenciesVersions.lifecycle}" }
    val lifecycleCompiler by lazy { "androidx.lifecycle:lifecycle-compiler:${DependenciesVersions.lifecycle}" }
    val lifecycleService by lazy { "androidx.lifecycle:lifecycle-service:${DependenciesVersions.lifecycle}" }
    val lifecycleProcess by lazy { "androidx.lifecycle:lifecycle-process:${DependenciesVersions.lifecycle}" }

    // Datastore and Preferences
    val datastore by lazy { "androidx.datastore:datastore:${DependenciesVersions.dataStore}" }
    val datastorePreferences by lazy { "androidx.datastore:datastore-preferences:${DependenciesVersions.dataStore}" }
    val preferences by lazy { "androidx.preference:preference-ktx:${DependenciesVersions.preference}" }

    /* Hilt - We are going to use hilt.android which includes
     * support for Activity and fragment injection so we need to include
     * the following dependencies */
    val hilt by lazy { "com.google.dagger:hilt-android:${DependenciesVersions.hilt}" }
    val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${DependenciesVersions.hilt}" }
    val hiltAndroidXCompiler by lazy { "androidx.hilt:hilt-compiler:${DependenciesVersions.hiltAndroidX}" }
    val hiltWork by lazy { "androidx.hilt:hilt-work:${DependenciesVersions.hiltAndroidX}" }
    val hiltKaptAndroidXCompiler by lazy { "androidx.hilt:hilt-compiler:${DependenciesVersions.hiltAndroidX}" }

    // Google Maps
    val maps by lazy { "com.google.android.gms:play-services-maps:${DependenciesVersions.maps}" }
    val mapsUtils by lazy { "com.google.maps.android:android-maps-utils:${DependenciesVersions.mapsUtils}" }
    val location by lazy { "com.google.android.gms:play-services-location:${DependenciesVersions.location}" }
    val places by lazy { "com.google.android.libraries.places:places:${DependenciesVersions.places}" }

    // Google API
    val googleBasePlayServices by lazy { "com.google.android.gms:play-services-base:${DependenciesVersions.googlePlayServicesBase}" }
    val googleAPIClientJackson2 by lazy { "com.google.api-client:google-api-client-jackson2:${DependenciesVersions.googleApiClient}" }
    val googleHttpClient by lazy { "com.google.http-client:google-http-client-android:${DependenciesVersions.googleHttpClient}" }
    val googleAPIClient by lazy { "com.google.api-client:google-api-client:${DependenciesVersions.googleApiClient}" }
    val googleAPIClientAndroid by lazy { "com.google.api-client:google-api-client-android:${DependenciesVersions.googleApiClient}" }
    val googleAPIClientOAuthJetty by lazy { "com.google.oauth-client:google-oauth-client-jetty:${DependenciesVersions.googleApiClientOAuth}" }
    val googleAPIClientOAuthJava6 by lazy { "com.google.oauth-client:google-oauth-client-java6:${DependenciesVersions.googleApiClientOAuth}" }
    val googleAPIDrive by lazy { "com.google.apis:google-api-services-drive:${DependenciesVersions.googleApiDriveV3}" }
    val playServicesAuth by lazy { "com.google.android.gms:play-services-auth:${DependenciesVersions.playServicesAuth}" }

    // Google ML Kit
    val barcodeScanningML by lazy { "com.google.mlkit:barcode-scanning:${DependenciesVersions.barcodeScanning}" }
    val objectDetectionML by lazy { "com.google.mlkit:object-detection:${DependenciesVersions.objectDetection}" }
    val objectDetectionCustomML by lazy { "com.google.mlkit:object-detection-custom:${DependenciesVersions.objectDetection}" }

    // Firebase
    val firebasePlatform by lazy { "com.google.firebase:firebase-bom:${DependenciesVersions.firebaseBom}" }
    val firebaseAnalytics by lazy { "com.google.firebase:firebase-analytics-ktx" }
    val firebaseAuth by lazy { "com.google.firebase:firebase-auth-ktx" }
    val firebaseCrashlytics by lazy { "com.google.firebase:firebase-crashlytics-ktx" }
    val firebaseMessaging by lazy { "com.google.firebase:firebase-messaging-ktx" }
    val firebasePerf by lazy { "com.google.firebase:firebase-perf-ktx" }
    val firebaseCloudStorage by lazy { "com.google.firebase:firebase-storage-ktx" }
    val firebaseDatabase by lazy { "com.google.firebase:firebase-database-ktx" }
    val firebaseAds by lazy { "com.google.android.gms:play-services-ads:${DependenciesVersions.ads}" }

    /* Retrofit, Okhttp, Okhttp logging interceptor, Moshi  */
    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${DependenciesVersions.retrofit}" }
    val retrofitConverterMoshi by lazy { "com.squareup.retrofit2:converter-moshi:${DependenciesVersions.retrofit}" }

    // OkHttp
    val okHttpBom by lazy { "com.squareup.okhttp3:okhttp-bom:${DependenciesVersions.okHttp}" }
    val okHttp by lazy { "com.squareup.okhttp3:okhttp" }
    val okHttpLogging by lazy { "com.squareup.okhttp3:logging-interceptor" }

    // Moshi
    val moshi by lazy { "com.squareup.moshi:moshi:${DependenciesVersions.moshi}" }
    val moshiKotlin by lazy { "com.squareup.moshi:moshi-kotlin:${DependenciesVersions.moshi}" }
    val moshiKotlinCodeGen by lazy { "com.squareup.moshi:moshi-kotlin-codegen:${DependenciesVersions.moshi}" }

    // EventBus
    val eventBus by lazy { "org.greenrobot:eventbus:${DependenciesVersions.eventbus}" }

    // MPAndroidChart
    val androidChart by lazy { "com.github.PhilJay:MPAndroidChart:${DependenciesVersions.mpAndroidChart}" }

    // Dexter
    val dexter by lazy { "com.karumi:dexter:${DependenciesVersions.dexter}" }

    // Glide
    val glide by lazy { "com.github.bumptech.glide:glide:${DependenciesVersions.glide}" }
    val glideAnnotation by lazy { "com.github.bumptech.glide:compiler:${DependenciesVersions.glide}" }

    // Blurry
    val blurry by lazy { "jp.wasabeef:blurry:${DependenciesVersions.blurry}" }
    val glideTransformation by lazy { "jp.wasabeef:glide-transformations:${DependenciesVersions.glideTransformation}" }

    // Lottie
    val lottie by lazy { "com.airbnb.android:lottie:${DependenciesVersions.lottie}" }

    //ThreeTen : Alternative to Android Calendar API
    val threeten by lazy { "com.jakewharton.threetenabp:threetenabp:${DependenciesVersions.threeten}" }

    // Timber
    val timber by lazy { "com.jakewharton.timber:timber:${DependenciesVersions.timber}" }

    val kotoolsTypes by lazy { "io.github.kotools:types:${DependenciesVersions.types}" }
}

object TestsDependencies {
    val junit by lazy { "junit:junit:${TestsVersions.jUnit}" }
    val testExtJUnit by lazy { "androidx.test.ext:junit:${TestsVersions.testExtJUnit}" }
    val espressoCore by lazy { "androidx.test.espresso:espresso-core:${TestsVersions.espressoCore}" }
    val mockitoCore by lazy { "org.mockito:mockito-core:${TestsVersions.mockitoCore}" }
    val mockitoAndroid by lazy { "org.mockito:mockito-android:${TestsVersions.mockitoAndroid}" }

    val workerTest by lazy { "androidx.work:work-testing:${TestsVersions.worker}" }
    val hiltAndroidTesting by lazy { "com.google.dagger:hilt-android-testing:${TestsVersions.hilt}" }
    val hiltKaptAndroidCompiler by lazy { "com.google.dagger:hilt-android-compiler:${TestsVersions.hilt}" }
    val hiltKaptCompiler by lazy { "androidx.hilt:hilt-compiler:${TestsVersions.hiltAndroidX}" }

}