package com.riders.thelab.ui.transition;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.transition.TransitionSet;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.ui.base.SimpleActivity;
import com.riders.thelab.ui.youtubelike.YoutubeLikeDetailActivity;

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

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Activity Transitions. Optionally enable Activity transitions in your
        // theme with <item name=”android:windowActivityTransitions”>true</item>.
        /*getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        // Attach a callback used to capture the shared elements from this Activity to be used
        // by the container transform transition
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        // Keep system bars (status bar, navigation bar) persistent throughout the transition.
        getWindow().setSharedElementsUseOverlay(false);*/

        Window w = getWindow();
        w.setAllowEnterTransitionOverlap(true);
        setContentView(R.layout.activity_transition);

        ButterKnife.bind(this);
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.button_next_activity)
    public void onNextActivityClicked() {

        // Variables
        Intent intent = new Intent(this, TransitionDetailActivity.class);
        ActivityOptionsCompat options = null;

        androidx.core.util.Pair<View, String> sePairThumb;
        androidx.core.util.Pair<View, String> sePairButton;

        // Check if we're running on Android 5.0 or higher
        if (LabCompatibilityManager.isLollipop()) {

            sePairThumb = androidx.core.util.Pair.create(imageView, getString(R.string.logo_transition_name));
            sePairButton = androidx.core.util.Pair.create(materialButton, getString(R.string.button_transition_name));

            // create the transition animation - the images in the layouts
            // of both activities are defined with android:transitionName="robot"
            options =
                    ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                                    this,
                                    sePairThumb, sePairButton);

            // start the new activity
            startActivity(intent, options.toBundle());

        } else {
            startActivity(intent);
        }
    }
}
