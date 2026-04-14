package com.riders.thelab

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope


fun DependencyHandlerScope.implementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("implementation", dependencyNotation)

fun DependencyHandlerScope.implementationPlatform(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("implementation", platform(dependencyNotation))

fun DependencyHandlerScope.ksp(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("ksp", dependencyNotation)

fun DependencyHandlerScope.androidTestImplementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("androidTestImplementation", dependencyNotation)

fun DependencyHandlerScope.androidTestImplementationPlatform(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("androidTestImplementation", platform(dependencyNotation))

fun DependencyHandlerScope.kspAndroidTest(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("kspAndroidTest", dependencyNotation)

fun DependencyHandlerScope.androidTestDebugImplementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("androidTestDebugImplementation", dependencyNotation)

fun DependencyHandlerScope.testImplementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("testImplementation", dependencyNotation)

fun DependencyHandlerScope.testImplementationPlatform(dependencyNotation: Provider<MinimalExternalModuleDependency>) =
    addDependency("testImplementation", platform(dependencyNotation))


private fun DependencyHandlerScope.addDependency(
    configurationName: String,
    dependencyNotation: Provider<MinimalExternalModuleDependency>
) = configurationName(dependencyNotation = dependencyNotation)