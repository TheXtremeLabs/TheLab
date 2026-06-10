package com.riders.thelab

import com.riders.thelab.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val Project.libs
    get() : VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.jdkVersion get() : Int = libs.findVersion("javaVersion")
    .get()
    .toString()
    .toInt()

/**
 * Retrieves the Java version from the `libs` version catalog.
 *
 * This property reads the "javaVersion" from the catalog and converts it into
 * a [JavaVersion], which is used to configure Java compatibility.
 */
val Project.javaVersion get(): JavaVersion = JavaVersion.toVersion(jdkVersion)

/**
 * Retrieves the Java language version from the `libs` version catalog.
 *
 * This property reads the "javaVersion" from the catalog and converts it into
 * a [JavaLanguageVersion], which is used to configure the Java toolchain.
 */
val Project.javaLanguageVersion get(): JavaLanguageVersion = JavaLanguageVersion.of(jdkVersion)

/**
 * Retrieves the JVM target version from the `libs` version catalog.
 *
 * This property reads the "javaVersion" from the catalog and converts it into
 * a [JvmTarget], which is used to configure the Kotlin compiler.
 */
val Project.jvmTarget
    get(): JvmTarget = JvmTarget.fromTarget(
        libs.findVersion("javaVersion")
            .get()
            .toString()
    )
