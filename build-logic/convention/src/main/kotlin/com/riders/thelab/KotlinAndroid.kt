package com.riders.thelab

import AndroidConfiguration
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = AndroidConfiguration.Sdk.COMPILATION

        defaultConfig {
            minSdk = AndroidConfiguration.Sdk.MIN
        }

        buildFeatures {
            buildConfig = true
            // Determines whether to support View Binding.
            // Note that the viewBinding.enabled property is now deprecated.
            viewBinding = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }

        tasks.withType<KotlinJvmCompile>() {
            compilerOptions {

                // Treat all Kotlin warnings as errors (disabled by default)
                // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
                val warningsAsErrors: String? by project
                allWarningsAsErrors.set(warningsAsErrors.toBoolean())

                freeCompilerArgs.addAll(
                    freeCompilerArgs.get() + listOf(
                        "-opt-in=kotlin.RequiresOptIn",
                        // Enable experimental coroutines APIs, including Flow
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-opt-in=kotlinx.coroutines.FlowPreview",
                        "-opt-in=kotlin.Experimental",
                    )
                )

                // Set JVM target to 1.8
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val kotlinBom = libs.findLibrary("kotlin-bom").get()

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())

        // Kotlin bom
        add("implementation", platform(kotlinBom))

        // Timber : Logging library
        add("implementation", libs.findLibrary("timber").get())
    }
}
