package com.riders.thelab

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType


/**
 * Configure base Timber
 */
internal fun Project.configureTimber(commonExtension: CommonExtension<*, *, *, *, *>) {

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    dependencies {
        // Timber : Logging library
        add("implementation", libs.findLibrary("timber").get())
    }
}