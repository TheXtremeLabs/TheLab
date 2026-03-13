package com.riders.thelab

import AndroidConfiguration
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Configure base Kotlin with Android options
 */

internal fun Project.configureKotlinAndroid(applicationExtension: ApplicationExtension) {
    configureKotlinCommons(applicationExtension)
    configureKotlinJvm()
}

internal fun Project.configureKotlinAndroid(libraryExtension: LibraryExtension) {
    configureKotlinCommons(libraryExtension)
    configureKotlinJvm()
}

internal fun Project.configureKotlinAndroid(testExtension: TestExtension) {
    configureKotlinCommons(testExtension)
    configureKotlinJvm()
}

internal fun Project.configureKotlinCommons(commonExtension: CommonExtension) {

    logger.lifecycle("\uD83D\uDD53 Configuring kotlin commons....")

    commonExtension.apply {
        this@apply.compileSdk {
            version = release(AndroidConfiguration.Sdk.COMPILATION) { minorApiLevel = 1 }
        }
        this@apply.ndkVersion = AndroidConfiguration.ndk.toString()

        this@apply.defaultConfig.minSdk = AndroidConfiguration.Sdk.MIN

        this@apply.compileOptions.sourceCompatibility = this@configureKotlinCommons.javaVersion
        this@apply.compileOptions.targetCompatibility = this@configureKotlinCommons.javaVersion
        this@apply.compileOptions.isCoreLibraryDesugaringEnabled = true

        this@apply.buildFeatures.buildConfig = true
        this@apply.buildFeatures.viewBinding = true
    }

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())

        // Kotlin
        val kotlinBom = libs.findLibrary("kotlin-bom").get()
        add("implementation", platform(kotlinBom))
        add("implementation", libs.findLibrary("kotlin-reflect").get())
        add("implementation", libs.findLibrary("kotlin-stdlib").get())
        // For Android Instrumented Tests
        add("androidTestImplementation", platform(kotlinBom))
        add("androidTestImplementation", libs.findLibrary("kotlin-test").get())
        // For Unit Tests
        add("testImplementation", platform(kotlinBom))
        add("testImplementation", libs.findLibrary("kotlin-test").get())

        // Kotlin Coroutines
        val kotlinxCoroutinesBom = libs.findLibrary("kotlinx-coroutines-bom").get()
        add("implementation", platform(kotlinxCoroutinesBom))
        add("implementation", libs.findLibrary("kotlinx-coroutines-core").get())
        add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())
        // For Android Instrumented Tests
        add("androidTestImplementation", platform(kotlinxCoroutinesBom))
        add("androidTestImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
        // For Unit Tests
        add("testImplementation", platform(kotlinxCoroutinesBom))
        add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())

        // Timber
        add("implementation", libs.findLibrary("timber").get())
    }
}


/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }

        // Up to Java 11 APIs are available through desugaring
        // https://developer.android.com/studio/write/java11-minimal-support-table
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Configure base Kotlin options
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors = providers.gradleProperty("warningsAsErrors").map {
        it.toBoolean()
    }.orElse(false)
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("Unsupported project extension $this ${T::class}")
    }.apply {

        jvmToolchain(21)

        this.jvmTarget = JvmTarget.JVM_21
        this.allWarningsAsErrors = warningsAsErrors
        this.freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            // Enable experimental coroutines APIs, including Flow
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlin.Experimental",
        )
        this.freeCompilerArgs.add(
            // Enable experimental coroutines APIs, including Flow
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
        this.freeCompilerArgs.add(
            /*
             * Remove this args after Phase 3.
             * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-consistent-copy-visibility/#deprecation-timeline
             *
             * Deprecation timeline
             * Phase 3. (Supposedly Kotlin 2.2 or Kotlin 2.3).
             * The default changes.
             * Unless ExposedCopyVisibility is used, the generated 'copy' method has the same visibility as the primary constructor.
             * The binary signature changes. The error on the declaration is no longer reported.
             * '-Xconsistent-data-class-copy-visibility' compiler flag and ConsistentCopyVisibility annotation are now unnecessary.
             */

            "-Xconsistent-data-class-copy-visibility"
        )
    }
}
