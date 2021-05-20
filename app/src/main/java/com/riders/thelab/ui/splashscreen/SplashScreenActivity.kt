package com.riders.thelab.ui.splashscreen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabAnimationsManager
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.databinding.ActivitySplashscreenBinding
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity(), OnPreparedListener,
    OnCompletionListener {

    companion object {
        private const val ANDROID_RES_PATH = "android.resource://"
        private const val SEPARATOR = "/"
    }

    private lateinit var viewBinding: ActivitySplashscreenBinding

    private val mViewModel: SplashScreenViewModel by viewModels()

    private val position = 0

    // Animators
    private lateinit var theAnimator: ObjectAnimator
    private lateinit var labAnimator: ObjectAnimator
    private lateinit var versionTextAnimator: ObjectAnimator
    private lateinit var fadeProgressAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViewModelsObservers()

        mViewModel.retrieveAppVersion(this)

        startVideo()
    }

    override fun onSaveInstanceState(
        savedInstanceState: Bundle,
        outPersistentState: PersistableBundle
    ) {
        super.onSaveInstanceState(savedInstanceState, outPersistentState)

        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", viewBinding.splashVideo.currentPosition)
        viewBinding.splashVideo.pause()
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)

        //we use onRestoreInstanceState in order to play the video playback from the stored position
        val position = savedInstanceState?.getInt("Position")
        if (position != null) {
            viewBinding.splashVideo.seekTo(position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        //if we have a position on savedInstanceState, the video playback should start from here

        //if we have a position on savedInstanceState, the video playback should start from here
        viewBinding.splashVideo.seekTo(position)
        if (position == 0) {
            viewBinding.splashVideo.start()
        } else {
            //if we come from a resumed activity, video playback will be paused
            viewBinding.splashVideo.pause()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Timber.e("Video completed")

        mViewModel.onVideoEnd()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModelsObservers() {
        mViewModel
            .getAppVersion()
            .observe(this, { appVersion ->
                Timber.d("Version : %s", appVersion)

                /*viewBinding.tvAppVersion.text =
                    this.getString(R.string.version_placeholder) + appVersion*/
            })

        mViewModel.getOnVideoEnd().observe(this, { finished ->
            Timber.d("getOnVideoEnd : %s", finished)
            crossFadeViews(viewBinding.clSplashContent)
        })
    }


    private fun startVideo() {
        Timber.i("startVideo()")
        try {
            //set the uri of the video to be played
            viewBinding.splashVideo
                .setVideoURI(
                    Uri.parse(
                        (
                                ANDROID_RES_PATH
                                        + packageName.toString()
                                        + SEPARATOR +
                                        if (!LabCompatibilityManager.isTablet(this))
                                            R.raw.splash_intro_testing_sound_2 //Smartphone portrait video
                                        else
                                            R.raw.splash_intro_testing_no_sound_tablet)
                    )
                ) //Tablet landscape video
        } catch (e: Exception) {
            Timber.e(e)
        }

        viewBinding.splashVideo.requestFocus()
        viewBinding.splashVideo.setOnPreparedListener(this)
        viewBinding.splashVideo.setOnCompletionListener(this)
    }


    private fun crossFadeViews(view: View) {
        Timber.i("crossFadeViews()")

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
//        view.alpha = 1f
        view.visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        view.animate()
            .alpha(1f)
            .setDuration(LabAnimationsManager.getInstance().shortAnimationDuration.toLong())
            .setListener(null)

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        viewBinding.splashVideo.animate()
            .alpha(0f)
            .setDuration(LabAnimationsManager.getInstance().shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    Timber.e("onAnimationEnd()")
                    viewBinding.splashVideo.visibility = View.GONE
                    startTheLabAnimation()
                }
            })
    }


    fun startTheLabAnimation() {
        Timber.e("startTheLaPartAnimation()")
        viewBinding.ivThe?.visibility = View.VISIBLE
        viewBinding.ivLab?.visibility = View.VISIBLE

        theAnimator = ObjectAnimator.ofFloat(viewBinding.ivThe, "alpha", 1f)
        theAnimator.duration = LabAnimationsManager.getInstance().longAnimationDuration.toLong()
        theAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                viewBinding.ivThe?.alpha = 1f
            }
        })
        theAnimator.start()


        labAnimator = ObjectAnimator.ofFloat(viewBinding.ivLab, "alpha", 1f)
        labAnimator.duration = LabAnimationsManager.getInstance().longAnimationDuration.toLong()
        labAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                viewBinding.ivLab?.alpha = 1f
                displayAppVersion()
            }
        })
        labAnimator.start()
    }

    fun displayAppVersion() {
        Timber.e("displayAppVersion()")
        versionTextAnimator = ObjectAnimator.ofFloat(viewBinding.tvAppVersion, "alpha", 1f)
        versionTextAnimator.duration =
            LabAnimationsManager.getInstance().longAnimationDuration.toLong()
        versionTextAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                viewBinding.tvAppVersion?.alpha = 1f
                startProgressAnimation()
            }
        })
        versionTextAnimator.start()
    }

    fun startProgressAnimation() {
        Timber.e("startProgressAnimation()")
        fadeProgressAnimator = ObjectAnimator.ofFloat(viewBinding.progressBar, "alpha", 0f, 1f)
        fadeProgressAnimator.duration =
            LabAnimationsManager.getInstance().shortAnimationDuration.toLong()
        fadeProgressAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                viewBinding.progressBar.alpha = 1f

                goToMainActivity()
            }
        })
        fadeProgressAnimator.start()
    }

    private fun goToMainActivity() {
        Timber.i("goToMainActivity()")

        Completable
            .complete()
            .delay(3, TimeUnit.SECONDS)
            .doOnComplete {
                Navigator(this).callMainActivity()
                finish()
            }
            .doOnError { t: Throwable? -> Timber.e(t) }
            .subscribeOn(Schedulers.io())
            //Caused by: android.util.AndroidRuntimeException:
            // Animators may only be run on Looper threads
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}