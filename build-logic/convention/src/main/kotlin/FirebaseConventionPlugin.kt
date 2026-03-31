import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
                apply("com.google.firebase.firebase-perf")
            }
            pluginManager.findPlugin("com.google.gms:google-services").apply {
                version = target.libs.findVersion("playServicesGradlePlugin").get().toString()
            }
            pluginManager.findPlugin("com.google.firebase.crashlytics").apply {
                version = target.libs.findVersion("crashlyticsGradlePlugin").get().toString()
            }
            pluginManager.findPlugin("com.google.firebase.firebase-perf").apply {
                version = target.libs.findVersion("performancesGradlePlugin").get().toString()
            }


            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Firebase convention plugin applied to '${project.name}'")
        }
    }
}