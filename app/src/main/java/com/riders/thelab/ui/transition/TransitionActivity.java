package com.riders.thelab.ui.transition;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LabCompatibilityManager.isLollipop()) {
            Window w = getWindow();
            w.setAllowEnterTransitionOverlap(true);
        }

        setContentView(R.layout.activity_transition);

        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getString(R.string.activity_title_transition));
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.button_next_activity)
    public void onNextActivityClicked() {

        // Variables
        Intent intent = new Intent(this, TransitionDetailActivity.class);
        ActivityOptionsCompat options = null;

        Pair<View, String> sePairThumb;
        Pair<View, String> sePairButton;

        // Check if we're running on Android 5.0 or higher
        if (LabCompatibilityManager.isLollipop()) {

            sePairThumb = Pair.create(imageView, getString(R.string.logo_transition_name));
            sePairButton = Pair.create(materialButton, getString(R.string.button_transition_name));

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
