object AndroidConfiguration {

    val ndk: Version = Version(major = 21, minor = 0, patch = 6113669)

    object Sdk {
        const val COMPILATION: Int = 36
        const val MIN: Int = 26
        const val TARGET: Int = COMPILATION
    }

    object Application {
        const val CODE: Int = 1
        val version: Version = Version(major = 14, minor = 1, patch = 1)
    }
}

data class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) {
    override fun toString(): String = "$major.$minor.$patch"
}