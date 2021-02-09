package com.riders.thelab.ui.transition;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint({"NonConstantResourceId", "NewApi"})
public class TransitionDetailActivity extends SimpleActivity {

    @BindView(R.id.iv_logo)
    ShapeableImageView ivTarget;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Enable Activity Transitions. Optionally enable Activity transitions in your
        // theme with <item name=”android:windowActivityTransitions”>true</item>.
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        // Set the transition name, which matches Activity A’s start view transition name, on
        // the root view.
        this.findViewById(android.R.id.content).setTransitionName(getString(R.string.logo_transition_name));

        // Attach a callback used to receive the shared elements from Activity A to be
        // used by the container transform transition.
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        /*// Set this Activity’s enter and return transition to a MaterialContainerTransform
        getWindow().setSharedElementEnterTransition(
                new MaterialContainerTransform()
//                        .addTarget(android.R.id.content)
                        .addTarget(R.id.iv_logo)
                        .setDuration(300L));

        getWindow().setSharedElementReturnTransition(
                new MaterialContainerTransform()
//                        .addTarget(android.R.id.content)
                        .addTarget(R.id.iv_logo)
                        .setDuration(250L));*/


        super.onCreate(savedInstanceState);
        // Tell the framework to wait.
        postponeEnterTransition();
        setContentView(R.layout.activity_transition_detail);

        ButterKnife.bind(this);
/*
        supportPostponeEnterTransition();

        Glide.with(this)
                .load(R.drawable.logo_colors)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //scheduleStartPostponedTransition(ivTarget);

                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(ivTarget);*/
        supportPostponeEnterTransition();
        ivTarget
                .getViewTreeObserver()
                .addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                ivTarget
                                        .getViewTreeObserver()
                                        .removeOnPreDrawListener(this);
                                supportStartPostponedEnterTransition();
                                return true;
                            }
                        }
                );
    }


    /**
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy. Some common places where it might make
     * sense to call this method are:
     * <p>
     * (1) Inside a Fragment's onCreateView() method (if the shared element
     * lives inside a Fragment hosted by the called Activity).
     * <p>
     * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
     * asynchronously load/scale a bitmap before the transition can begin).
     **/
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAfterTransition();
    }
}
