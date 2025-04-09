plugins {
    id("thelab.android.library")
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
        resources{
            excludes += "META-INF/DEPENDENCIES"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/INDEX.LIST"
            excludes += "google/protobuf/*.proto"
        }

        jniLibs.pickFirsts.add("protobuf.meta")
    }

    configurations.all {
        exclude(module = "protobuf-javalite")
        exclude(module = "protobuf-java")
        exclude(module = "proto-google-common-protos")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    namespace = "com.riders.thelab.core.analytics"
}

configurations.forEach { configuration ->
    configuration.exclude("protolite-well-known-types")
    configuration.exclude("protobuf-javalite")
    configuration.exclude("protobuf-java")
    configuration.exclude("protobuf-java-util")
}

dependencies {
    /////////////////////////////
    // General Dependencies
    /////////////////////////////
    // Firebase
    api(platform(libs.firebase.bom))
//    api(libs.firebase.ads)
    api(libs.firebase.analytics) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    api(libs.firebase.auth)
    api(libs.firebase.crashlytics)
    api(libs.firebase.database)
    api(libs.firebase.firestore) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    api(libs.firebase.messaging)
    api(libs.firebase.perf) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    api(libs.firebase.storage) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
}
