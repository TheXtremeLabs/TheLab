package com.riders.thelab.core.ui.data.local.bean

/**
 * Represents the strength level of a password.
 * Each level has a corresponding label.
 *
 * @property label The human-readable label for the strength level.
 */
enum class StrengthLevel(val label: String) {
    /**
     * The password is very weak.
     */
    VERY_WEAK("Very Weak"),

    /**
     * The password is weak.
     */
    WEAK("Weak"),

    /**
     * The password is of moderate strength.
     */
    MODERATE("Moderate"),

    /**
     * The password is strong.
     */
    STRONG("Strong"),

    /**
     * The password is very strong.
     */
    VERY_STRONG("Very Strong");


    companion object {
        /**
         * Returns a [StrengthLevel] based on a given score.
         *
         * @param score The score to evaluate.
         * @return The corresponding [StrengthLevel].
         */
        fun getStrengthLevel(score: Int): StrengthLevel = when (score) {
            0, 1 -> VERY_WEAK
            2 -> WEAK
            3 -> MODERATE
            4 -> STRONG
            else -> VERY_STRONG
        }
    }
}
