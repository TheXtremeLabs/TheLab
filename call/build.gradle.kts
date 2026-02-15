plugins {
    alias(libs.plugins.thelab.application)
    alias(libs.plugins.thelab.application.compose)
}

val versionMajor: Int = 1
val versionMinor: Int = 0
val versionPatch: Int = 0

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
    namespace = "com.riders.thelab.call"

    defaultConfig {

        manifestPlaceholders += mapOf(
            "redirectHostName" to "com.riders.thelab.call",
            "redirectSchemeName" to "com.riders.thelab.call",
            "redirectPathPattern" to "//com.riders.thelab.call"
        )
        applicationId = "com.riders.thelab.call"

        versionCode = 2000 * versionMajor + 100 * versionMinor + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true

            // Enables CODE shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = false
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
}

configurations.all {
    resolutionStrategy {
        cacheDynamicVersionsFor(4, "hours")
        cacheChangingModulesFor(10, "minutes")

        eachDependency {
            if ("com.google.protobuf" == requested.group && "protobuf-javalite" == requested.name) {
                useTarget("com.google.protobuf:protobuf-javalite:${libs.versions.protobuf.get()}")
                //because("")
            }
        }
    }

    exclude(module = "protobuf-java")
    exclude(module = "protobuf-kotlin")
    exclude(module = "protobuf-lite")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:permissions"))
    implementation(project(":core:ui"))

    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.telecom)

    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}