import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidKtorConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            /*pluginManager.findPlugin("io.ktor.plugin").apply {
                version = libs.findVersion("ktorGradlePlugin").get()
            }*/
            with(pluginManager) {
                apply("io.ktor.plugin")
            }

            // val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                "implementation"(libs.findLibrary("ktor.client.android").get())
                "implementation"(libs.findLibrary("ktor.client.core").get())
                "implementation"(libs.findLibrary("ktor.client.content.negotiation").get())
                "implementation"(libs.findLibrary("ktor.client.cio").get())
                "implementation"(libs.findLibrary("ktor.client.logging").get())
                "implementation"(libs.findLibrary("ktor.client.okhttp").get())
                "implementation"(libs.findLibrary("ktor.client.serialization.kotlinx.json").get())
                "implementation"(libs.findLibrary("slf4j.android").get())
                "implementation"(libs.findLibrary("napier").get())
            }
        }
    }
}