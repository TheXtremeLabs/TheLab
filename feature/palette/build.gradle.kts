plugins {
    alias(libs.plugins.thelab.feature)
    alias(libs.plugins.thelab.library.compose)
    alias(libs.plugins.thelab.hilt)
    alias(libs.plugins.kotlin.serialization)
}
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xlambdas=class")
    }
}

android {
    defaultConfig {
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.riders.thelab.core.testing.TheLabHiltTestRunner"
        testApplicationId = "com.riders.thelab.feature.palette.test"
    }

    namespace = "com.riders.thelab.feature.palette"
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
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)


    // Required for Hilt testing
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    testImplementation(project(":core:testing"))
    // Add the core:testing module to provide the Runner and other test utilities
    androidTestImplementation(project(":core:testing"))

    androidTestImplementation(libs.androidx.test.runner)
}