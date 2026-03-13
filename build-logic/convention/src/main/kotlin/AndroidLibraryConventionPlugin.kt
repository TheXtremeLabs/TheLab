import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.riders.thelab.configureKotlinAndroid
import com.riders.thelab.configurePrintApksTask
import com.riders.thelab.configureTimber
import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            // Configure Jvm ToolChain
            with(kotlinExtension) {
                jvmToolchain(21)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(libraryExtension = this)
                defaultConfig.targetSdk = AndroidConfiguration.Sdk.TARGET
                // configureFlavors(this)
                configureTimber()
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
            }

            configurations.configureEach {
                resolutionStrategy {
                    force(libs.findLibrary("junit4").get())
                    // Temporary workaround for https://issuetracker.google.com/174733673
                    force("org.objenesis:objenesis:2.6")
                }
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}