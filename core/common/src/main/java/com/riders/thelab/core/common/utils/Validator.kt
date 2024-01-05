package com.riders.thelab.core.common.utils

import android.text.TextUtils
import android.util.Patterns

object Validator {

    private const val REGEX_CODE_NUMERIC = "\\d+(?:\\.\\d+)?"
    private const val REGEX_CODE_ALPHA = "[a-zA-Z]+"
    private const val REGEX_CODE_ALPHA_NUMERIC = "[A-Za-z0-9]+"

    /**
     * Verify if String is null or empty
     *
     * @param s String to verify
     * @return The result of test
     */
    fun isEmpty(s: String?): Boolean {
        var empty = true
        if (s != null) {
            if (s.trim { it <= ' ' }.isNotEmpty()) {
                empty = false
            }
        }
        return empty
    }

    /**
     * Verify if String is full numeric
     *
     * @param s String to verify
     * @return The result of test
     */
    fun isNumeric(s: String?): Boolean {
        return s != null && s.trim { it <= ' ' }.isNotEmpty() && s.matches(
            Regex(
                REGEX_CODE_NUMERIC
            )
        )
    }

    /**
     * Verify if String is full Alpha
     *
     * @param s String to verify
     * @return The result of test
     */
    fun isAlpha(s: String?): Boolean {
        return s != null && s.trim { it <= ' ' }.isNotEmpty() && s.matches(
            Regex(
                REGEX_CODE_ALPHA
            )
        )
    }

    fun isValidEmail(email: String): Boolean {
        return (!TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }
}