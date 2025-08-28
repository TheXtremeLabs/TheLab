package com.riders.thelab

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


/**
 * Configure base Timber
 */
internal fun Project.configureTimber() {
    dependencies {
        // Timber : Logging library
        add("implementation", libs.findLibrary("timber").get())
    }
}