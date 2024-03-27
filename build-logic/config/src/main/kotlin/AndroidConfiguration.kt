object AndroidConfiguration {

    object Sdk {
        const val COMPILATION: Int = 34
        const val MIN: Int = 23
        const val TARGET: Int = COMPILATION
    }

    object Application {
        const val CODE: Int = 1
        val version: Version = Version(major = 12, minor = 14, patch = 4)
    }
}

data class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) {
    override fun toString(): String = "$major.$minor.$patch"
}