import com.android.build.api.dsl.LibraryExtension
import com.riders.thelab.configureAndroidCompose
import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
                apply(libs.findPlugin("thelab-library").get().get().pluginId)
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Library Compose convention plugin applied to '${project.name}'")
        }
    }
}