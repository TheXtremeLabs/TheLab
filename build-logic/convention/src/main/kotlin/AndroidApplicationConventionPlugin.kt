import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.riders.thelab.configureFlavors
import com.riders.thelab.configureKotlinAndroid
import com.riders.thelab.configurePrintApksTask
import com.riders.thelab.jdkVersion
import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android-application").get().get().pluginId)
                apply(libs.findPlugin("kotlin-android").get().get().pluginId)
                apply(libs.findPlugin("kotlin-serialization").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            // Configure Jvm ToolChain
            with(kotlinExtension) {
                jvmToolchain(jdkVersion)
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)

                defaultConfig.apply {
                    targetSdk = AndroidConfiguration.Sdk.TARGET

                    versionCode = AndroidConfiguration.Application.CODE
                    versionName = AndroidConfiguration.Application.version.toString()
                }

                configureFlavors(applicationExtension = this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
            }

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Android Application convention plugin applied to '${project.name}'")
        }
    }
}