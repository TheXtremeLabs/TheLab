package com.riders.thelab.core.utils


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.AndroidRuntimeException
import android.view.View
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.TheLabApplication
import timber.log.Timber

object LabAnimationsManager {

    private var shortAnimationDuration = 0
    private var mediumAnimationDuration = 0
    private var longAnimationDuration = 0

    init {

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration =
            TheLabApplication.getInstance().resources.getInteger(android.R.integer.config_shortAnimTime)
        mediumAnimationDuration =
            TheLabApplication.getInstance().resources.getInteger(android.R.integer.config_mediumAnimTime)
        longAnimationDuration =
            TheLabApplication.getInstance().resources.getInteger(android.R.integer.config_longAnimTime)
    }


    /**
     * Apply the fade color animation to the specified view in paramters
     *
     * @param view
     * @param fromColor
     * @param toColor
     * @param animationDuration
     */
    @SuppressLint("RestrictedApi")
    fun applyFadeColorAnimationToView(
        view: View?,
        fromColor: Int,
        toColor: Int,
        animationDuration: Int
    ) {
        var fadeAnimator: ObjectAnimator? = null
        if (view is MaterialTextView) {
            fadeAnimator = ObjectAnimator.ofObject(
                view,
                "textColor",
                ArgbEvaluator(),
                fromColor,
                toColor
            )
        }
        if (view is MaterialButton) {
            fadeAnimator = ObjectAnimator.ofObject(
                view,
                "backgroundColor",
                ArgbEvaluator(),
                fromColor,
                toColor
            )
        }
        startAnimation(fadeAnimator, animationDuration)
    }

    /**
     * Start animation of ObjectAnimator object built previously
     *
     * @param objectAnimator
     * @param duration
     */
    private fun startAnimation(objectAnimator: ObjectAnimator?, duration: Int) {
        if (null != objectAnimator) {
            objectAnimator.duration = duration.toLong()
            objectAnimator.start()
        }
    }

    fun clearAnimations(vararg objectAnimators: ObjectAnimator) {
        Timber.d("clearAnimations()")
        try {
            for (element in objectAnimators) {
                element.removeAllListeners()
                element.end()
                element.cancel()
            }
        } catch (exception: AndroidRuntimeException) {
            Timber.e(exception)
        }
    }
}