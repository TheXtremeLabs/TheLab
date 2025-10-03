plugins {
    alias(libs.plugins.thelab.library)
    alias(libs.plugins.thelab.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/INDEX.LIST"
        }
    }

    namespace = "com.riders.thelab.core.speechtotext"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    // AndroidX
    implementation(libs.androidx.media)
    implementation(libs.androidx.media.common)

    // Google
    api(libs.google.auth.library.oauth2.http)
    // Google Cloud Speech API
    /*api(platform(libs.google.cloud.bom))
    api(libs.google.cloud.texttospeech)*/
    api(libs.google.cloud.speech)
    api(libs.grpc.okhttp)
    api(libs.grpc.stub)
    api(libs.jflac.codec)

    /////////////////////////////
    // Tests Dependencies
    /////////////////////////////
    androidTestImplementation(project(":core:testing"))
}