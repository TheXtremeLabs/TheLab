package com.riders.thelab

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class TheLabBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE
}
