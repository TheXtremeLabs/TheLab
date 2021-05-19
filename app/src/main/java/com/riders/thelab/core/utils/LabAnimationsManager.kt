package com.riders.thelab.core.utils

import android.R
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.View
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.TheLabApplication
import lombok.Getter
import javax.inject.Inject

@Getter
class LabAnimationsManager private constructor() {

    companion object {
        private var mInstance: LabAnimationsManager? = null

        /**
         * Returns the class' instance
         *
         * @return
         */
        fun getInstance(): LabAnimationsManager {
            if (null == mInstance) mInstance = LabAnimationsManager()
            return mInstance as LabAnimationsManager
        }
    }

    @Inject
    lateinit var application: TheLabApplication

    var shortAnimationDuration = 0
    var mediumAnimationDuration = 0
    var longAnimationDuration = 0

    init {

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = application.resources.getInteger(R.integer.config_shortAnimTime)
        mediumAnimationDuration = application.resources.getInteger(R.integer.config_mediumAnimTime)
        longAnimationDuration = application.resources.getInteger(R.integer.config_longAnimTime)
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
    fun applyFadeColorAnimationToView(view: View?, fromColor: Int, toColor: Int, animationDuration: Int) {
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
    fun startAnimation(objectAnimator: ObjectAnimator?, duration: Int) {
        if (null != objectAnimator) {
            objectAnimator.duration = duration.toLong()
            objectAnimator.start()
        }
    }

}