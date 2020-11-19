package com.riders.thelab.ui.splashscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.riders.thelab.R;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

import static com.riders.thelab.R.string.version_placeholder;

@SuppressLint("NonConstantResourceId")
public class SplashScreenView extends BaseViewImpl<SplashScreenPresenter>
        implements SplashScreenContract.View, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    @BindView(R.id.ll_splash_content)
    RelativeLayout rlContent;

    @BindView(R.id.splash_video)
    VideoView splashVideoView;

    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @Inject
    Navigator navigator;

    private SplashScreenActivity context;

    private static final String ANDROID_RES_PATH = "android.resource://";
    private static final String SEPARATOR = "/";

    private int position = 0;

    private int shortAnimationDuration;


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

        getPresenter().getAppVersion();

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration =
                context
                        .getResources()
                        .getInteger(android.R.integer.config_shortAnimTime);

        getPresenter().hasPermissions(context);
    }

    @Override
    public void onActivityCreated() {

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
        Timber.i("onPause()");

    }

    @Override
    public void onResume() {
        Timber.i("onResume()");
    }

    @Override
    public void onDetach() {

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

        // Initially hide the content view.
        rlContent.setVisibility(View.GONE);

        startVideo();
    }

    @Override
    public void onPermissionsDenied() {
        Timber.e("onPermissionsDenied()");
        closeApp();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void displayAppVersion(String appVersion) {
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
        crossFadeViews();
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
                                            R.raw.splash_intro_testing_sound_2));

        } catch (Exception e) {
            Timber.e(e);
        }

        splashVideoView.requestFocus();
        splashVideoView.setOnPreparedListener(this);
        splashVideoView.setOnCompletionListener(this);
    }

    private void goToMainActivity() {
        Timber.i("goToMainActivity()");
        if (context != null && navigator != null) {
            navigator.callMainActivity();
            context.finish();
        }

    }


    private void crossFadeViews() {
        Timber.i("crossFadeViews()");

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        rlContent.setAlpha(1f);
        rlContent.setVisibility(View.VISIBLE);

        Timber.d("llContent.setVisibility(View.VISIBLE)");

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        rlContent.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        splashVideoView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Timber.e("onAnimationEnd()");
                        splashVideoView.setVisibility(View.GONE);

                        Completable
                                .complete()
                                .delay(3, TimeUnit.SECONDS)
                                .doOnComplete(() -> {
                                    goToMainActivity();
                                })
                                .doOnError(Timber::e)
                                .subscribeOn(Schedulers.io())
                                //Caused by: android.util.AndroidRuntimeException:
                                // Animators may only be run on Looper threads
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                });
    }
}
