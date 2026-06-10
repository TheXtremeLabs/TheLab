import com.riders.thelab.implementation
import com.riders.thelab.implementationPlatform
import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKtorConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            dependencies {
                val ktorBom = libs.findLibrary("ktor.bom").get()
                implementationPlatform(ktorBom)
                implementation(libs.findLibrary("ktor.client.android").get())
                implementation(libs.findLibrary("ktor.client.core").get())
                implementation(libs.findLibrary("ktor.client.content.negotiation").get())
                implementation(libs.findLibrary("ktor.client.cio").get())
                implementation(libs.findLibrary("ktor.client.logging").get())
                implementation(libs.findLibrary("ktor.client.okhttp").get())
                //    implementation(  libs.findLibrary("ktor.client.timeout").get())
                implementation(libs.findLibrary("ktor.serialization.kotlinx.json").get())
                implementation(libs.findLibrary("slf4j.android").get())
                implementation(libs.findLibrary("napier").get())
            }

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Ktor convention plugin applied to '${project.name}'")
        }
    }
}