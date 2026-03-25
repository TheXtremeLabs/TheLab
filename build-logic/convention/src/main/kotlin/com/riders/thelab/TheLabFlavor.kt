package com.riders.thelab

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName")
enum class TheLabFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType),
    prod(FlavorDimension.contentType, ".prod")
}

fun Project.configureFlavors(
    applicationExtension: ApplicationExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: TheLabFlavor) -> Unit = {}
) {
    applicationExtension.apply {
        configureFlavorsCommons(
            commonExtension = applicationExtension,
            flavorConfigurationBlock = flavorConfigurationBlock
        )
    }
}

fun Project.configureFlavors(
    libraryExtension: LibraryExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: TheLabFlavor) -> Unit = {}
) {
    libraryExtension.apply {
        configureFlavorsCommons(
            commonExtension = libraryExtension,
            flavorConfigurationBlock = flavorConfigurationBlock
        )
    }
}

fun Project.configureFlavorsCommons(
    commonExtension: CommonExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: TheLabFlavor) -> Unit = {}
) {
    commonExtension.apply {
        flavorDimensions += FlavorDimension.contentType.name

        TheLabFlavor.entries.forEach {
            productFlavors.create(it.name) {
                dimension = it.dimension.name
                flavorConfigurationBlock(this, it)
                if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                    if (it.applicationIdSuffix != null) {
                        this.applicationIdSuffix = it.applicationIdSuffix
                    }

                    if (TheLabFlavor.demo.name == it.name) {
                        isDefault = true
                        androidResources.localeFilters += listOf("en", /*"xxhdpi"*/)
                    }
                }
            }
        }
    }
}
