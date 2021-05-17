package com.riders.thelab.utils;

import java.util.Collection;

public class Validator {

    private static final String REGEX_CODE_NUMERIC = "\\d+(?:\\.\\d+)?";
    private static final String REGEX_CODE_ALPHA = "[a-zA-Z]+";
    private static final String REGEX_CODE_ALPHA_NUMERIC = "[A-Za-z0-9]+";

    /**
     * Verify if String is null or empty
     *
     * @param s String to verify
     * @return The result of test
     */
    public static boolean isEmpty(final String s) {
        boolean empty = true;
        if (s != null) {
            if (!s.trim().isEmpty()) {
                empty = false;
            }
        }
        return empty;
    }

    /**
     * Verify if List is null or empty
     *
     * @param list List to verify
     * @param <T>  Generic list Object
     * @return The result of test
     */
    public static <T> boolean isNullOrEmpty(final Collection<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Verify if String is full numeric
     *
     * @param s String to verify
     * @return The result of test
     */
    public static boolean isNumeric(final String s) {
        return s != null && !s.trim().isEmpty() && s.matches(REGEX_CODE_NUMERIC);
    }

    /**
     * Verify if String is full Alpha
     *
     * @param s String to verify
     * @return The result of test
     */
    public static boolean isAlpha(final String s) {
        return s != null && !s.trim().isEmpty() && s.matches(REGEX_CODE_ALPHA);
    }
}
