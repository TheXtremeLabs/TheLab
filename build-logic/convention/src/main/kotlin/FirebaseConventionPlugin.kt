import com.riders.thelab.implementation
import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("playservices").get().get().pluginId)
                apply(libs.findPlugin("crashlytics").get().get().pluginId)
                apply(libs.findPlugin("performances").get().get().pluginId)
            }

            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                implementation(platform(bom))
                implementation(libs.findLibrary("firebase-crashlytics").get())
            }

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Firebase convention plugin applied to '${project.name}'")
        }
    }
}