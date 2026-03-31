import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKtorConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libs.findLibrary("ktor.client.android").get())
                add("implementation", libs.findLibrary("ktor.client.core").get())
                add("implementation", libs.findLibrary("ktor.client.content.negotiation").get())
                add("implementation", libs.findLibrary("ktor.client.cio").get())
                add("implementation", libs.findLibrary("ktor.client.logging").get())
                add("implementation", libs.findLibrary("ktor.client.okhttp").get())
                // add("implementation", libs.findLibrary("ktor.client.timeout").get())
                add("implementation", libs.findLibrary("ktor.serialization.kotlinx.json").get())
                add("implementation", libs.findLibrary("slf4j.android").get())
                add("implementation", libs.findLibrary("napier").get())
            }

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Ktor convention plugin applied to '${project.name}'")
        }
    }
}