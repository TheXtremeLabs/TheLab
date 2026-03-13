package com.riders.thelab

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.io.File

/**
 * Configure Compose-specific options
 */

internal fun Project.configureAndroidCompose(applicationExtension: ApplicationExtension) {
    applicationExtension.apply {
        configureAndroidCompose(commonExtension = this)
    }
}

internal fun Project.configureAndroidCompose(libraryExtension: LibraryExtension) {
    libraryExtension.apply {
        configureAndroidCompose(commonExtension = this)
    }
}

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension) {
    commonExtension.apply {
        buildFeatures.buildConfig = true
        // Enables Jetpack Compose for this module
        buildFeatures.compose = true

        tasks.withType<KotlinJvmCompile>() {
            compilerOptions {
                freeCompilerArgs.addAll(
                    freeCompilerArgs.get() +
                            buildComposeMetricsParameters() +
                            buildComposeKotlinCompatibilityCheck()
                )
            }
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }
    }
}

private fun Project.buildComposeMetricsParameters(): List<String> {

    val projectBuildDir = "$projectDir/build"
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    val enableMetrics = (enableMetricsProvider.orNull == "true")

    if (enableMetrics) {
        val metricsFolder = File(projectBuildDir, "compose-metrics")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = File(projectBuildDir, "compose-reports")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
        )
    }
    return metricParameters.toList()
}

private fun Project.buildComposeKotlinCompatibilityCheck(): List<String> {
    val suppressCheck = mutableListOf<String>()

    suppressCheck.add("-P")
    suppressCheck.add(
        "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
    )
    return suppressCheck.toList()
}
