object AndroidConfiguration {
    val buildTools: Version = Version(major = 30, minor = 0, patch = 3)

    object Sdk {
        const val compilation: Int = 33
        const val min: Int = 26
        const val target: Int = compilation
    }

    object Application {
        const val code: Int = 1
        val version: Version = Version(major = 12, minor = 0, patch = 5)
    }
}

data class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) {
    override fun toString(): String = "$major.$minor.$patch"
}