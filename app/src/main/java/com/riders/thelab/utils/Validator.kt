package com.riders.thelab.utils

class Validator {

    companion object {

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
         * Verify if List is null or empty
         *
         * @param list List to verify
         * @param <T>  Generic list Object
         * @return The result of test
        </T> */
        fun <T> isNullOrEmpty(list: Collection<T>?): Boolean {
            return list == null || list.isEmpty()
        }

        /**
         * Verify if String is full numeric
         *
         * @param s String to verify
         * @return The result of test
         */
        fun isNumeric(s: String?): Boolean {
            return s != null && s.trim { it <= ' ' }.isNotEmpty() && s.matches(Regex(REGEX_CODE_NUMERIC))
        }

        /**
         * Verify if String is full Alpha
         *
         * @param s String to verify
         * @return The result of test
         */
        fun isAlpha(s: String?): Boolean {
            return s != null && s.trim { it <= ' ' }.isNotEmpty() && s.matches(Regex(REGEX_CODE_ALPHA))
        }
    }
}