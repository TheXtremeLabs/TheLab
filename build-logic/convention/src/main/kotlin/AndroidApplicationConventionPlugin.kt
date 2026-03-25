import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.riders.thelab.configureFlavors
import com.riders.thelab.configureKotlinAndroid
import com.riders.thelab.configurePrintApksTask
import com.riders.thelab.configureTimber
import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
                apply("kotlinx-serialization")
                apply("com.google.devtools.ksp")
            }

            // Configure Jvm ToolChain
            with(kotlinExtension) {
                jvmToolchain(libs.findVersion("javaVersion").get().requiredVersion.toInt())
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(applicationExtension = this)

                defaultConfig.apply {
                    targetSdk = AndroidConfiguration.Sdk.TARGET

                    versionCode = AndroidConfiguration.Application.CODE
                    versionName = AndroidConfiguration.Application.version.toString()
                }

                configureFlavors(applicationExtension = this)
                configureTimber()
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
            }
        }
    }
}