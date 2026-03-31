import com.android.build.api.dsl.ApplicationExtension
import com.riders.thelab.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.cc.base.logger
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(applicationExtension = extension)

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Android Application Compose convention plugin applied to '${project.name}'")
        }
    }
}