enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {

    /**
     * The pluginManagement {repositories {...}} block configures the
     * repositories Gradle uses to search or download the Gradle plugins and
     * their transitive dependencies. Gradle pre-configures support for remote
     * repositories such as JCenter, Maven Central, and Ivy. You can also use
     * local repositories or define your own remote repositories. The CODE below
     * defines the Gradle Plugin Portal, Google's Maven repository,
     * and the Maven Central Repository as the repositories Gradle should use to look for its dependencies.
     */

    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()

        maven { url = uri("https://androidx.dev/snapshots/latest/artifacts/repository") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }

        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.google.com") }
        // Ktor
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
        maven {
            url = uri("http://files.couchbase.com/maven2/")
            isAllowInsecureProtocol = true
        }
        // Kotlin plugin org
        maven { url = uri("https://plugins.gradle.org/m2/") }
        // Android Build Server
        maven { url = uri("../nowinandroid-prebuilts/m2repository") }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    // Develocity
    id("com.gradle.develocity") version "4.3.2"
}

dependencyResolutionManagement {

    /**
     * The dependencyResolutionManagement { repositories {...}}* block is where you configure the repositories and dependencies used by
     * all modules in your project, such as libraries that you are using to
     * create your application. However, you should configure module-specific
     * dependencies in each module-level build.gradle.kts file. For new projects,
     * Android Studio includes Google's Maven repository and the
     * Maven Central Repository by
     * default, but it does not configure any dependencies (unless you select a
     * template that requires some).
     */

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()

        maven { url = uri("https://androidx.dev/snapshots/latest/artifacts/repository") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }

        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.google.com") }
        // Ktor
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
        maven {
            url = uri("http://files.couchbase.com/maven2/")
            isAllowInsecureProtocol = true
        }
        // Kotlin plugin org
        maven { url = uri("https://plugins.gradle.org/m2/") }
        // Android Build Server
        maven { url = uri("../nowinandroid-prebuilts/m2repository") }
    }
}

develocity {
    // configuration
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}

/*
 * The built-in local build cache, DirectoryBuildCache, uses a directory to store build cache artifacts.
 * By default, this directory resides in the Gradle User Home, but its location is configurable.
 * Gradle will periodically clean-up the local cache directory by removing entries
 * that have not been used recently to conserve disk space.
 */
buildCache {
    // Build cache for gradle
    local {
        directory = File(rootDir, "build-cache")
    }
}

rootProject.name = "TheLab"
// Applications
include(":app")
include(":appTV")
include(":call")
include(":central")
include(":vision")
// Library modules
include(":core:analytics")
include(":core:camera")
include(":core:common")
include(":core:data")
include(":core:datastore")
include(":core:google")
include(":core:location")
include(":core:nfc")
include(":core:permissions")
include(":core:player")
//include(":core:speechtotext")
include(":core:ui")
include(":core:testing")
// Features
include(":feature:artists")
include(":feature:biometric")
include(":feature:bluetooth")
include(":feature:colors")
include(":feature:download")
include(":feature:flightaware")
include(":feature:googledrive")
include(":feature:home")
include(":feature:kat")
include(":feature:koin")
include(":feature:locationonmaps")
include(":feature:login")
include(":feature:lottie")
include(":feature:mlkit")
include(":feature:musicrecognition")
include(":feature:nfc")
include(":feature:palette")
include(":feature:schedule")
include(":feature:settings")
include(":feature:songplayer")
include(":feature:streaming")
include(":feature:splashscreen")
include(":feature:tabs")
include(":feature:theaters")
include(":feature:transitions")
include(":feature:videocall")
include(":feature:weather")
include(":feature:youtube")
//include(":shazam-kit")
include(":spotify-app-remote")
include(":spotify-auth")
include(":spotify-auth-store")
//include(":soundlab")
