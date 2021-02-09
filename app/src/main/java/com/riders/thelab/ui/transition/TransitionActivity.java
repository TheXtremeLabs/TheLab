package com.riders.thelab.ui.transition;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.transition.TransitionSet;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * https://guides.codepath.com/android/shared-element-activity-transition
 */
@SuppressLint("NonConstantResourceId")
public class TransitionActivity extends SimpleActivity {

    @BindView(R.id.iv_logo)
    ShapeableImageView imageView;

    @BindView(R.id.button_next_activity)
    MaterialButton materialButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Enable Activity Transitions. Optionally enable Activity transitions in your
        // theme with <item name=”android:windowActivityTransitions”>true</item>.
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        // Attach a callback used to capture the shared elements from this Activity to be used
        // by the container transform transition
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        // Keep system bars (status bar, navigation bar) persistent throughout the transition.
        getWindow().setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        ButterKnife.bind(this);
/*
        supportPostponeEnterTransition();
        imageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                }
        );*/
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.button_next_activity)
    public void onNextActivityClicked() {

        Intent intent = new Intent(this, TransitionDetailActivity.class);

        if (LabCompatibilityManager.isLollipop()) {

            String transitionName = getString(R.string.logo_transition_name);

            Pair<View, String> p1 = Pair.create((View) imageView, transitionName);

            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTarget(imageView);
            // create the transition animation - the images in the layouts
            // of both activities are defined with android:transitionName="robot"
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, p1);
            // start the new activity
            startActivity(intent, options.toBundle());

        } else {
            startActivity(intent);
        }
    }
}
