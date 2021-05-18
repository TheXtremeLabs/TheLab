package com.riders.thelab.core.utils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.View;

import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import lombok.Getter;

/**
 * This class is responsible for animations of the application
 */
@Getter
public class LabAnimationsManager {

    private static LabAnimationsManager mInstance;

    private final int shortAnimationDuration;
    private final int mediumAnimationDuration;
    private final int longAnimationDuration;

    private LabAnimationsManager() {

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = TheLabApplication.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mediumAnimationDuration = TheLabApplication.getContext().getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
        longAnimationDuration = TheLabApplication.getContext().getResources().getInteger(
                android.R.integer.config_longAnimTime);
    }


    /**
     * Returns the class' instance
     *
     * @return
     */
    public static LabAnimationsManager getInstance() {
        if (null == mInstance)
            mInstance = new LabAnimationsManager();

        return mInstance;
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
    public void applyFadeColorAnimationToView(final View view, final int fromColor, final int toColor, final int animationDuration) {
        ObjectAnimator fadeAnimator = null;

        if (view instanceof MaterialTextView) {
            fadeAnimator = ObjectAnimator.ofObject(
                    view,
                    "textColor",
                    new ArgbEvaluator(),
                    fromColor,
                    toColor
            );
        }

        if (view instanceof MaterialButton) {
            fadeAnimator = ObjectAnimator.ofObject(
                    view,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    fromColor,
                    toColor
            );
        }

        startAnimation(fadeAnimator, animationDuration);
    }

    /**
     * Start animation of ObjectAnimator object built previously
     *
     * @param objectAnimator
     * @param duration
     */
    public void startAnimation(final ObjectAnimator objectAnimator, final int duration) {
        if (null != objectAnimator) {
            objectAnimator.setDuration(duration);
            objectAnimator.start();
        }
    }
}
