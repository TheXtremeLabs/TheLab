import org.gradle.api.Plugin
import org.gradle.api.Project

class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
//                apply("com.google.firebase.firebase-perf")
            }
            pluginManager.findPlugin("com.google.gms:google-services").apply {
                version = "4.4.2"
            }
            pluginManager.findPlugin("com.google.firebase.crashlytics").apply {
                version = "3.0.3"
            }
            pluginManager.findPlugin("com.google.firebase.firebase-perf").apply {
                version = "1.4.2"
            }
        }
    }
}