plugins {
    alias(libs.plugins.thelab.library)
}

android {
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
            //excludes += "google/protobuf/*.proto"
        }

        jniLibs.pickFirsts.add("protobuf.meta")
    }

    namespace = "com.riders.thelab.core.analytics"
}

configurations.all {
    resolutionStrategy {
        // In configurations.all -> resolutionStrategy
        force("${libs.protobuf.javalite.get()}:${libs.versions.protobuf.get()}")
        force("${libs.protobuf.kotlin.lite.get()}:${libs.versions.protobuf.get()}")
    }

    exclude(module = "protobuf-java")
    exclude(module = "protobuf-kotlin")
    exclude(module = "protobuf-lite")
    exclude(module = "proto-google-common-protos")
    exclude(module = "protolite-well-known-types")
    exclude(group = "com.google.firebase", module = "protolite-well-known-types")
}

dependencies {
    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    // Firebase
    api(platform(libs.firebase.bom))
//    api(libs.firebase.ads)
    api(libs.firebase.analytics)
    api(libs.firebase.auth)
    api(libs.firebase.crashlytics)
    api(libs.firebase.database)
    api(libs.firebase.firestore)
    api(libs.firebase.messaging)
    api(libs.firebase.perf)
    api(libs.firebase.storage)

    // App Check
    api(libs.firebase.appcheck)
    api(libs.firebase.appcheck.debug)
    api(libs.firebase.appcheck.playintegrity)
}
