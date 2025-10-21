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
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.thelab.firebase)
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
    namespace = "com.riders.thelab.vision"

    defaultConfig {
        applicationId = "com.riders.thelab.vision"

        versionCode = 2000 * versionMajor + 100 * versionMinor + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Enabling multidex support.
        multiDexEnabled = true

        vectorDrawables.useSupportLibrary = true

        ndk {
            abiFilters.clear()
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        manifestPlaceholders += mapOf(
            "redirectHostName" to "com.riders.thelab.vision",
            "redirectSchemeName" to "com.riders.thelab.vision",
            "redirectPathPattern" to "//com.riders.thelab.vision"
        )
    }

    buildTypes {
        val debug by getting {
            isDebuggable = true

            // Enables CODE shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = false
        }

        release {
            isDebuggable = false

            // Enables CODE shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/INDEX.LIST"
            excludes += "google/protobuf/*.proto"
        }

        jniLibs.pickFirsts.add("protobuf.meta")
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
}

configurations.all {
    resolutionStrategy {
        cacheDynamicVersionsFor(4, "hours")
        cacheChangingModulesFor(10, "minutes")

        force("com.google.protobuf:protobuf-javalite:4.32.0")
    }

    exclude(module = "protobuf-lite")
    exclude(module = "protolite-well-known-types")
    exclude(group = "org.threeten", module = "threetenbp")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:analytics"))
    implementation(project(":core:camera"))
    implementation(project(":core:common"))
    implementation(project(":core:permissions"))
    implementation(project(":core:ui"))

    // AndroidX
    implementation(libs.androidx.multidex)


    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
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