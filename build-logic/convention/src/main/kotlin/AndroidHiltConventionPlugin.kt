import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("hilt").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            dependencies {
                // KotlinX
                "ksp"(libs.findLibrary("kotlinx.metadata.jvm").get())

                // Hilt
                "implementation"(libs.findBundle("hilt").get())
                "ksp"(libs.findBundle("hilt.compilers").get())

                // Tests
                "androidTestImplementation"(libs.findLibrary("hilt.android.testing").get())
                "kspAndroidTest"(libs.findLibrary("hilt.compiler").get())
                "kspAndroidTest"(libs.findLibrary("hilt.ext.compiler").get())
            }

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Hilt convention plugin applied to '${project.name}'")
        }
    }
}