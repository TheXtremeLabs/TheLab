package com.riders.thelab.core.ui.data.local.model

/**
 * Represents the strength of a password, calculated from a set of rules.
 *
 * @property score The calculated score based on the rules met by the password.
 * @property maxScore The maximum possible score.
 */
data class PasswordStrength(
    val score: Int,
    val maxScore: Int
) {
    /**
     * The password strength as a percentage.
     */
    val percentage: Float get() = score.toFloat() / maxScore.toFloat()


    companion object {
        /**
         * Calculates the strength of a given password based on a list of rules.
         *
         * @param password The password to evaluate.
         * @param rules The list of [PasswordRule]s to check against. Defaults to [PasswordRule.passwordRules].
         * @return A [PasswordStrength] object representing the calculated strength.
         */
        fun calculatePasswordStrength(
            password: String,
            rules: List<PasswordRule> = PasswordRule.passwordRules
        ): PasswordStrength {
            val score = rules.sumOf { rule ->
                if (rule.regex.containsMatchIn(password)) rule.points else 0
            }

            return PasswordStrength(
                score = score,
                maxScore = rules.sumOf { it.points }
            )
        }
    }
}
