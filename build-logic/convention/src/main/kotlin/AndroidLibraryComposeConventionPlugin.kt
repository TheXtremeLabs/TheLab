import com.android.build.api.dsl.LibraryExtension
import com.riders.thelab.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(libraryExtension = extension)

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Library Compose convention plugin applied to '${project.name}'")
        }
    }
}