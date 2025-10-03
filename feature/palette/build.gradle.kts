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

    sourceSets {
        getByName("androidTest") {
            java.srcDirs("$projectDir/core/testing/src/androidTest/java/")
        }
    }
    namespace = "com.riders.thelab.feature.palette"
}

dependencies {
    ///////////////////////////////////
    // Project
    ///////////////////////////////////
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:testing"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)

    ///////////////////////////////////
    // General Dependencies
    ///////////////////////////////////
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

    androidTestImplementation(libs.androidx.test.runner)
}