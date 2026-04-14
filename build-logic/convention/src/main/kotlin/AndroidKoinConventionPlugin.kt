import com.riders.thelab.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.koin.compiler.plugin.KoinGradleExtension

class AndroidKoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("koin").get().get().pluginId)
            }

            extensions.configure<KoinGradleExtension> {
                compileSafety.set(true)
                unsafeDslChecks.set(false)
                userLogs.set(true)
            }

            dependencies {
                val koinBom = libs.findLibrary("koin.bom").get()
                add("implementation", platform(koinBom))
                add("implementation", libs.findLibrary("koin.core").get())
                add("implementation", libs.findLibrary("koin.core.viewmodel").get())
                add("implementation", libs.findLibrary("koin.core.coroutines").get())
//                add("ksp", libs.findLibrary("koin.ksp.compiler").get())
                add("implementation", libs.findLibrary("koin.android").get())
                add("implementation", libs.findLibrary("koin.androidx.compose").get())
                add("implementation", libs.findLibrary("koin.androidx.startup").get())
                add("implementation", libs.findLibrary("koin.androidx.workmanager").get())

                val koinAnnotationsBom = libs.findLibrary("koin.annotations.bom").get()
                add("implementation", platform(koinAnnotationsBom))
                add("implementation", libs.findLibrary("koin.annotations").get())

                // Koin Test features
                add("testImplementation", libs.findLibrary("koin.test").get())
                add("testImplementation", libs.findLibrary("koin.test.junit4").get())
                add("testImplementation", libs.findLibrary("koin.test.junit5").get())

                add("androidTestImplementation", libs.findLibrary("koin.test").get())
                add("androidTestImplementation", libs.findLibrary("koin.test.junit4").get())
                add("androidTestImplementation", libs.findLibrary("koin.test.junit5").get())
            }

            // Optional: Add a log message to confirm the plugin is applied
            logger.lifecycle("✅ Ktor convention plugin applied to '${project.name}'")
        }
    }
}