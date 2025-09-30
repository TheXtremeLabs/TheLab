object AndroidConfiguration {

    object Sdk {
        const val COMPILATION: Int = 36
        const val MIN: Int = 24
        const val TARGET: Int = COMPILATION
    }

    object Application {
        const val CODE: Int = 1
        val version: Version = Version(major = 14, minor = 0, patch = 1)
    }
}

data class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) {
    override fun toString(): String = "$major.$minor.$patch"
}