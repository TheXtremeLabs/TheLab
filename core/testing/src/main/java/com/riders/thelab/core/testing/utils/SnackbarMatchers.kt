package com.riders.thelab.core.testing.utils

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object SnackbarMatchers {

    /**
     * Matches a Snackbar with the exact [text] displayed.
     */
    fun withText(text: String): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("is a Snackbar with text: $text")
            }

            override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                // The view must be a Snackbar or inside SnackbarLayout
                val snackbarTextViewId = com.google.android.material.R.id.snackbar_text
                val textView:TextView = view.findViewById(snackbarTextViewId)
                return textView.isShown && textView.text.toString() == text && parent is Snackbar.SnackbarLayout
            }
        }
    }
}

fun snackbarWithText(text: String): Matcher<View> {
    return object : BoundedMatcher<View, TextView>(TextView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("is a Snackbar with text: $text")
        }

        override fun matchesSafely(view: TextView): Boolean {
            val parent = view.parent
            return view.text.toString() == text && parent is Snackbar.SnackbarLayout
        }
    }
}