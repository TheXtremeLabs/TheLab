plugins {
    `kotlin-dsl`
//    alias(libs.plugins.android.lint)
}

group = "com.riders.thelab.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.javaVersion.get().toInt()))
    }
}

kotlin {
    jvmToolchain(libs.versions.javaVersion.get().toInt())
}

dependencies {
    implementation(project(":config"))

    compileOnly(gradleApi())

    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.plugins.android.lint.toDep())
    compileOnly(libs.android.tools.common.gradlePlugin)
    compileOnly(libs.androidx.room.gradlePlugin)
    compileOnly(libs.plugins.compose.compiler.toDep())
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performances.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.plugins.kotlin.android.toDep())
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.ktor.gradlePlugin)
//    lintChecks(libs.androidx.lint.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = libs.plugins.thelab.application.compose.get().pluginId
//            id = "thelab.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "thelab.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationJacoco") {
            id = "thelab.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "thelab.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "thelab.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "thelab.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidLibraryJacoco") {
            id = "thelab.android.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }
        register("androidTest") {
            id = "thelab.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidHilt") {
            id = "thelab.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidKtor") {
            id = "thelab.android.ktor"
            implementationClass = "AndroidKtorConventionPlugin"
        }
        register("androidRoom") {
            id = "thelab.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("firebase") {
            id = "thelab.firebase"
            implementationClass = "FirebaseConventionPlugin"
        }
    }
}


fun Provider<PluginDependency>.toDep(): Provider<String> = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}