package com.riders.thelab

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val Project.libs
    get() : VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")


val Project.javaVersion
    get(): JavaVersion = JavaVersion.toVersion(
        libs.findVersion("javaVersion")
            .get()
            .toString()
            .toInt()
    )

val Project.javaLanguageVersion
    get(): JavaLanguageVersion = JavaLanguageVersion.of(
        libs.findVersion("javaVersion")
            .get()
            .toString()
            .toInt()
    )

val Project.jvmTarget
    get(): JvmTarget = JvmTarget.fromTarget(
        libs.findVersion("javaVersion")
            .get()
            .toString()
    )
