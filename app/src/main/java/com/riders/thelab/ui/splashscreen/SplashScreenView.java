package com.riders.thelab.ui.splashscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.TheLabApplication;
import com.riders.thelab.core.utils.LabAnimationsManager;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.riders.thelab.R.string.version_placeholder;

@SuppressLint("NonConstantResourceId")
public class SplashScreenView extends BaseViewImpl<SplashScreenPresenter>
        implements SplashScreenContract.View, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private SplashScreenActivity context;

    private static final String ANDROID_RES_PATH = "android.resource://";
    private static final String SEPARATOR = "/";

    @BindView(R.id.cl_splash_content)
    ConstraintLayout clContent;
    @BindView(R.id.splash_video)
    VideoView splashVideoView;
    @BindView(R.id.iv_the_la_part)
    ShapeableImageView ivTheLaPart;
    @BindView(R.id.iv_five_number)
    ShapeableImageView ivFiveNumber;
    @BindView(R.id.tv_app_version)
    MaterialTextView tvAppVersion;
    @BindView(R.id.progressBar)
    LinearProgressIndicator progressBar;

    private int position = 0;

    // Animators
    private ObjectAnimator logoColorAnimator;
    private ObjectAnimator slideNumberAnim;
    private ObjectAnimator slideTextAnim;
    private ObjectAnimator versionTextAnimator;
    private ObjectAnimator fadeProgressAnimator;


    @Inject
    SplashScreenView(SplashScreenActivity context) {
        this.context = context;
    }


    /////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////
    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));
        TheLabApplication.getInstance();

        progressBar.setAlpha(0f);
        getPresenter().hasPermissions(context);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", splashVideoView.getCurrentPosition());
        splashVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        position = savedInstanceState.getInt("Position");
        splashVideoView.seekTo(position);
    }

    @Override
    public void onPause() {
        Timber.e("onPause()");

    }

    @Override
    public void onResume() {
        Timber.d("onResume()");

    }

    @Override
    public void startFiveAnimation() {
        Timber.e("startFiveAnimation()");
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.25f, 360f * 1.5f);
        Keyframe kf2 = Keyframe.ofFloat(.35f, 360f * 3.0f);
        Keyframe kf3 = Keyframe.ofFloat(.5f, 360f * 5.0f);
        Keyframe kf5 = Keyframe.ofFloat(.75f, 360.0f * 7.0f);
        Keyframe kf6 = Keyframe.ofFloat(1f, 360f * 9.0f);
        PropertyValuesHolder pvhRotation =
                PropertyValuesHolder.ofKeyframe("rotationY", kf0, kf1, kf2, kf3, kf5, kf6);
        logoColorAnimator = ObjectAnimator.ofPropertyValuesHolder(ivFiveNumber, pvhRotation);
        logoColorAnimator.setDuration(3500);
        logoColorAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        logoColorAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                slideNumber();
            }
        });
        logoColorAnimator.start();
    }

    @Override
    public void startTheLaPartAnimation() {
        Timber.e("startTheLaPartAnimation()");
        ivTheLaPart.setVisibility(View.VISIBLE);

        slideTextAnim = ObjectAnimator.ofFloat(ivTheLaPart, "alpha", 1f);
        slideTextAnim.setDuration(LabAnimationsManager.getInstance().getShortAnimationDuration());
        slideTextAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                ivTheLaPart.setAlpha(1f);
                displayAppVersion();

            }
        });
        slideTextAnim.start();
    }

    @Override
    public void displayAppVersion() {
        Timber.e("displayAppVersion()");
        versionTextAnimator = ObjectAnimator.ofFloat(tvAppVersion, "alpha", 1f);
        versionTextAnimator.setDuration(LabAnimationsManager.getInstance().getLongAnimationDuration());
        versionTextAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                tvAppVersion.setAlpha(1f);
                startProgressAnimation();
            }
        });
        versionTextAnimator.start();
    }


    @Override
    public void startProgressAnimation() {
        Timber.e("startProgressAnimation()");
        fadeProgressAnimator = ObjectAnimator.ofFloat(progressBar, "alpha", 0f, 1f);
        fadeProgressAnimator.setDuration(LabAnimationsManager.getInstance().getShortAnimationDuration());
        fadeProgressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                progressBar.setAlpha(1f);
                getPresenter().goToMainActivity();
            }
        });
        fadeProgressAnimator.start();
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }

    /////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////
    @Override
    public void onPermissionsGranted() {
        if (null != clContent) { // For tablet
            // Initially hide the content view.
            clContent.setVisibility(View.GONE);
        }

        getPresenter().getAppVersion();

        startVideo();
    }

    @Override
    public void onPermissionsDenied() {
        Timber.e("onPermissionsDenied()");
        closeApp();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setAppVersion(String appVersion) {
        Timber.e("setAppVersion()");
        tvAppVersion.setText(context.getString(version_placeholder) + appVersion);
    }

    @Override
    public void closeApp() {
        context.finish();
    }


    /////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////
    @Override
    public void onPrepared(MediaPlayer mp) {
        //if we have a position on savedInstanceState, the video playback should start from here
        splashVideoView.seekTo(position);
        if (position == 0) {
            splashVideoView.start();
        } else {
            //if we come from a resumed activity, video playback will be paused
            splashVideoView.pause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Timber.e("Video completed");
        crossFadeViews(clContent);
    }


    /////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////
    private void startVideo() {
        Timber.i("startVideo()");

        try {
            //set the uri of the video to be played
            splashVideoView
                    .setVideoURI(
                            Uri.parse(
                                    ANDROID_RES_PATH +
                                            context.getPackageName() +
                                            SEPARATOR +
                                            (
                                                    !LabCompatibilityManager.isTablet(context)
                                                            ? R.raw.splash_intro_testing_sound_2 //Smartphone portrait video
                                                            : R.raw.splash_intro_testing_no_sound_tablet)));//Tablet landscape video

        } catch (Exception e) {
            Timber.e(e);
        }

        splashVideoView.requestFocus();
        splashVideoView.setOnPreparedListener(this);
        splashVideoView.setOnCompletionListener(this);
    }


    private void crossFadeViews(View view) {
        Timber.i("crossFadeViews()");

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        view.setAlpha(1f);
        view.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        view.animate()
                .alpha(1f)
                .setDuration(LabAnimationsManager.getInstance().getShortAnimationDuration())
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        splashVideoView.animate()
                .alpha(0f)
                .setDuration(LabAnimationsManager.getInstance().getShortAnimationDuration())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Timber.e("onAnimationEnd()");
                        splashVideoView.setVisibility(View.GONE);

                        if (!LabCompatibilityManager.isOreo()) {
                            startProgressAnimation();
                            getPresenter().goToMainActivity();

                        } else {
                            startFiveAnimation();
                        }
                    }
                });
    }

    private void slideNumber() {
        Timber.e("slideNumber()");
        slideNumberAnim = ObjectAnimator.ofFloat(ivFiveNumber, "translationX", 0f, 25f, 50f, 0f);
        slideNumberAnim.setDuration(LabAnimationsManager.getInstance().getMediumAnimationDuration());
        slideNumberAnim.setInterpolator(new AccelerateInterpolator());
        slideNumberAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                startTheLaPartAnimation();
            }
        });
        slideNumberAnim.start();
    }
}
