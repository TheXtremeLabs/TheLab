import com.riders.thelab.TheLabBuildType

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
    alias(libs.plugins.thelab.application)
    alias(libs.plugins.thelab.application.compose)
    alias(libs.plugins.thelab.application.jacoco)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.thelab.firebase)
}

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
    /**
     * Defined in AndroidApplicationConventionPlugin class
     */

    ndkVersion = "21.3.6528147"

    defaultConfig {

        manifestPlaceholders += mapOf(
            "redirectHostName" to "com.riders.thelab",
            "redirectSchemeName" to "com.riders.thelab",
            "redirectPathPattern" to "//com.riders.thelab"
        )
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


    sourceSets {
        // Encapsulates configurations for the main source set.
        getByName("main") {
            // For each source set, you can specify only one Android manifest.
            // By default, Android Studio creates a manifest for your main source
            // set in the src/main/ directory.
            manifest.srcFile("src/main/AndroidManifest.xml")
        }

        // Encapsulates configurations for the main source set.
        getByName("release") {
            // For each source set, you can specify only one Android manifest.
            // By default, Android Studio creates a manifest for your main source
            // set in the src/main/ directory.
            manifest.srcFile("src/main/AndroidManifest.xml")
        }

        getByName("androidTest") {
            // For each source set, you can specify only one Android manifest.
            // By default, Android Studio creates a manifest for your main source
            // set in the src/main/ directory.
            manifest.srcFile("${project.rootDir}/AndroidManifest.xml")
        }
    }


    buildTypes {
        getByName("debug") {
            isDebuggable = true

            // Enables CODE shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = false

            // applicationIdSuffix = TheLabBuildType.DEBUG.applicationIdSuffix
        }

        getByName("release") {
            isDebuggable = false

            // Enables CODE shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true  // Enables CODE shrinking for the release build type.

            // Disables PNG crunching for the "release" build type.
            isCrunchPngs = false

            // Enables CODE shrinking, obfuscation, and optimization for only
            // your project's release build type.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationIdSuffix = TheLabBuildType.RELEASE.applicationIdSuffix
        }
    }


    buildFeatures {
        // Determines whether to support Data Binding.
        // Note that the dataBinding.enabled property is now deprecated.
        dataBinding = true
    }

    lint {
        // Turns off checks for the issue IDs you specify.
        disable += "TypographyFractions" + "TypographyQuotes"
        // Turns on checks for the issue IDs you specify. These checks are in
        // addition to the default lint checks.
        enable += "RtlHardcoded" + "RtlCompat" + "RtlEnabled"
        // To enable checks for only a subset of issue IDs and ignore all others,
        // list the issue IDs with the 'check' property instead. This property overrides
        // any issue IDs you enable or disable using the properties above.
        checkOnly += "NewApi" + "InlinedApi"
        // If set to true, turns off analysis progress reporting by lint.
        quiet = true
        // If set to true (default), stops the build if errors are found.
        abortOnError = true
        // If set to true, lint only reports errors.
        ignoreWarnings = true
        // If set to true, lint also checks all dependencies as part of its analysis.
        // Recommended for projects consisting of an app with library dependencies.
        checkDependencies = true

        checkReleaseBuilds = false
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "google/protobuf/*.proto"
            excludes += "google/type/color.proto"
            excludes += "src/google/protobuf/any.proto"
            excludes += "src/google/protobuf/descriptor.proto"
            excludes += "src/google/protobuf/duration.proto"
            excludes += "src/google/protobuf/empty.proto"
            excludes += "src/google/protobuf/field_mask.proto"
            excludes += "src/google/protobuf/source_context.proto"
            excludes += "src/google/protobuf/type.proto"
            excludes += "src/google/protobuf/timestamp.proto"
            excludes += "src/google/protobuf/wrappers.proto"
        }

        jniLibs.pickFirsts.add("protobuf.meta")
    }

    testOptions {
        unitTests {
            animationsDisabled = true
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    /*
    * https://stackoverflow.com/questions/50792428/how-to-access-variant-outputfilename-in-kotlin
    */
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val printFileName = "${applicationId}_${versionName}_${variant.name}.apk"
                val fileName = "${applicationId}_$versionName.apk"
                log("Application Variants | output", "filename: $printFileName")
                output.outputFileName = fileName
            }
    }

    namespace = "com.riders.thelab"
}

configurations.all {
    resolutionStrategy {
        cacheDynamicVersionsFor(4, "hours")
        cacheChangingModulesFor(10, "minutes")

        eachDependency {
            if ("com.google.protobuf" == requested.group && "protobuf-lite" == requested.name) {
                useTarget("com.google.protobuf:protobuf-javalite:${libs.versions.protobuf.get()}")
                //because("")
            }
        }

        // In configurations.all -> resolutionStrategy
        //force("${libs.protobuf.javalite.get()}:${libs.versions.protobuf.get()}")
        //force("${libs.protobuf.kotlin.lite.get()}:${libs.versions.protobuf.get()}")

        /*force("com.google.protobuf:protobuf-java:4.33.0")
        force("com.google.protobuf:protobuf-javalite:4.33.0")
        force("com.google.protobuf:protobuf-kotlin:4.33.0")
        force("com.google.protobuf:protobuf-kotlin-lite:4.33.0")*/

        //force("com.jakewharton.threetenabp:threetenabp:1.4.9") // Force a specific version
    }

    exclude(module = "protobuf-java")
    exclude(module = "protobuf-kotlin")
    exclude(module = "protobuf-lite")
    // exclude(module = "protolite-well-known-types")
    // exclude(group = "org.threeten", module = "threetenbp")
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:camera"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:data-di"))
    implementation(project(":core:datastore"))
    implementation(project(":core:google"))
    implementation(project(":core:permissions"))
    implementation(project(":core:player"))
    //implementation(project(":core:speechtotext"))
    implementation(project(":core:ui"))
    implementation(project(":core:testing"))

    implementation(project(":feature:home"))

    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    //Kotlin
    implementation(libs.kotlin.reflect)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.parcelize)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.play.services)

    // AndroidX
    implementation(libs.androidx.startup)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.palette)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.dynamicanimation)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.window.manager)
    implementation(libs.androidx.window.extensions)
    implementation(libs.material)

    // Compose: provided by ui module
    // The others dependencies has been added into the AndroidCompose plugin convention class

    // CameraX : provided by camera module
    /*implementation(libs.androidx.camera)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)*/

    // Media
    implementation(libs.androidx.media)
    implementation(libs.androidx.media.router)
    implementation(libs.androidx.media.exoplayer)
    implementation(libs.androidx.media.ui)

    // Auto fill
    implementation(libs.androidx.autofill)

    // Worker & concurrent: implemented in weather feature

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewModel.ktx)
    implementation(libs.androidx.lifecycle.viewModel.savedState)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    ksp(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.service)

    // Datastore and Preferences: provided by data module


    /* Hilt - We are going to use hilt.android which includes
     * support for Activity and fragment injection so we need to include
     * the following dependencies */
    // Hilt
    // The others dependencies has been added into the Hilt plugin convention class
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.ext.compiler)

    // Google Location (Maps / Places)
    implementation(libs.maps)
    implementation(libs.maps.utils)
    implementation(libs.location)
    implementation(libs.places)

    // Google API
    implementation(libs.google.play.services.base)
    implementation(libs.google.play.services.auth)
    implementation(libs.google.api.drive) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "com.google.protobuf", module = "protobuf-kotlin")
    }
    implementation(platform(libs.google.http.client.bom))
    implementation(libs.google.http.client)
    implementation(libs.google.api.client)
    implementation(libs.google.api.client.android)
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-jetty
    implementation(libs.google.api.client.jetty) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "com.google.protobuf", module = "protobuf-kotlin")
    }
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-java6
    implementation(libs.google.api.client.oauth.java6) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "com.google.protobuf", module = "protobuf-kotlin")
    }
    implementation(libs.google.api.client.oauth.jackson2) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "com.google.protobuf", module = "protobuf-kotlin")
    }

    // Google Cloud Speech API
    /*compileOnly(platform(libs.google.cloud.bom))
    runtimeOnly(libs.google.cloud.speech)
    // Protobuf
    compileOnly(platform(libs.protobuf.bom))
    runtimeOnly(libs.protobuf.javalite)
    runtimeOnly(libs.protobuf.kotlin.lite)*/

    // Google ML Kit
    implementation(libs.google.mlkit.barcode.scanning)
    implementation(libs.google.mlkit.objectdetection)
    implementation(libs.google.mlkit.objectdetection.custom)

    // Firebase BOM and Dependencies: provided by analytics module
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.appcheck.playintegrity)

    /* Retrofit using RxJava3, Okhttp, Okhttp logging interceptor, Moshi  */
    // Retrofit: provided by data module

    // OkHttp: provided by data module

    // MPAndroidChart
    implementation(libs.mpandroidchart)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    implementation(libs.glide.blurry)
    implementation(libs.glide.transformation)
    implementation(libs.glide.landscapist)

    //ThreeTen : Alternative to Android Calendar API
    /*implementation(libs.threeten) {
        exclude(group = "org.threeten", module = "threetenbp")
    }*/

    // Kotools Types: provided by data module

    // Fast Fourier Transform (FFT)
    implementation(libs.jtransforms)


    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.mockito.android)
    /*
        androidTestImplementation(libs.androidx.compose.ui.test)
        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.testManifest)*/
}

// Aggregating Task
// The Hilt Gradle plugin offers an option for performing Hilt’s classpath aggregation in a dedicated Gradle task.
hilt {
    enableAggregatingTask = true
}

/*
 * Use incremental compilation
 */
tasks.withType<JavaCompile>().configureEach {
    options.isFork = true
    options.isIncremental = true
}

/*
 * Disable reports
 * Gradle automatically creates test reports regardless of whether you want to look at them. That report generation slows down the overall build. You may not need reports if:
 *      you only care if the tests succeeded (rather than why)
 *      you use build scans, which provide more information than a local report
 */
tasks.withType<Test>().configureEach {
    // Run tests in parallel
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)

    // Disable test reports
    reports.html.required = false
    reports.junitXml.required = false

    /*
    Fork tests into multiple processes

    By default, Gradle runs all tests in a single forked JVM process.
    This is efficient for small test suites, but large or memory-intensive test suites can suffer from long execution times and GC pauses.
    You can reduce memory pressure and isolate problematic tests by forking a new JVM
    after a specified number of tests using the forkEvery setting:
     */
    forkEvery = 100
}

tasks.named("build") {
    doLast {
        print(rootProject.version)
    }
}