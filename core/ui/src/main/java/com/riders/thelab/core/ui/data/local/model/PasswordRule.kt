package com.riders.thelab.core.ui.data.local.model

/**
 * Represents a rule for password validation.
 *
 * @property label A human-readable description of the rule.
 * @property regex The regular expression used to validate the rule.
 * @property points The number of points awarded if the password matches this rule.
 */
data class PasswordRule(
    val label: String,
    val regex: Regex,
    val points: Int = 1
) {
    companion object {
        /**
         * A predefined list of password rules for validation.
         */
        val passwordRules = listOf(
            PasswordRule("Lowercase", Regex("[a-z]")),
            PasswordRule("Uppercase", Regex("[A-Z]")),
            PasswordRule("Number", Regex("\\d")),
            PasswordRule("Special Character", Regex("[^A-Za-z0-9]")),
            PasswordRule("Min Length 8", Regex(".{8,}"))
        )
    }
}
