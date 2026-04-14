import com.android.build.api.dsl.ApplicationExtension
import com.riders.thelab.configureAndroidCompose
import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android-application").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Android Application Compose convention plugin applied to '${project.name}'")
        }
    }
}