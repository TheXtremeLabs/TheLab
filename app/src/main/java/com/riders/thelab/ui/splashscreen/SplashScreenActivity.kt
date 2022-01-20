package com.riders.thelab.ui.splashscreen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.core.animation.addListener
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabAnimationsManager
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.databinding.ActivitySplashscreenBinding
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity(),
    CoroutineScope,
    OnPreparedListener,
    OnCompletionListener {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + Job()

    companion object {
        private const val ANDROID_RES_PATH = "android.resource://"
        private const val SEPARATOR = "/"
    }

    private var _viewBinding: ActivitySplashscreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SplashScreenViewModel by viewModels()

    private val position = 0

    // Animators
    private lateinit var versionTextAnimator: ObjectAnimator
    private lateinit var fadeProgressAnimator: ObjectAnimator


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)
        _viewBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        savedInstanceState.putInt("Position", binding.splashVideo.currentPosition)
        binding.splashVideo.pause()
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)

        //we use onRestoreInstanceState in order to play the video playback from the stored position
        val position = savedInstanceState?.getInt("Position")
        if (position != null) {
            binding.splashVideo.seekTo(position)
            binding.splashVideo.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")

        _viewBinding = null
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    @SuppressLint("SetTextI18n")
    private fun initViewModelsObservers() {
        mViewModel
            .getAppVersion()
            .observe(this, { appVersion ->
                Timber.d("Version : %s", appVersion)

                binding.tvAppVersion.text =
                    this.getString(R.string.version_placeholder) + appVersion
            })

        mViewModel.getOnVideoEnd().observe(this, { finished ->
            Timber.d("getOnVideoEnd : %s", finished)
            crossFadeViews(binding.clSplashContent)
        })
    }


    private fun startVideo() {
        Timber.i("startVideo()")
        try {
            val videoPath: String =
                ANDROID_RES_PATH +
                        packageName.toString() +
                        SEPARATOR +
                        //Smartphone portrait video or Tablet landscape video
                        if (!LabCompatibilityManager.isTablet(this)) R.raw.splash_intro_testing_sound_2 else R.raw.splash_intro_testing_no_sound_tablet

            //set the uri of the video to be played
            binding.splashVideo.setVideoURI(Uri.parse((videoPath)))
        } catch (e: Exception) {
            Timber.e(e)
        }

        binding.splashVideo.requestFocus()
        binding.splashVideo.setOnPreparedListener(this)
        binding.splashVideo.setOnCompletionListener(this)
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
            .setDuration(LabAnimationsManager.getInstance().mediumAnimationDuration.toLong())
            .setListener(null)

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        binding.splashVideo.animate()
            .alpha(0f)
            .setDuration(LabAnimationsManager.getInstance().shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    Timber.e("onAnimationEnd()")
                    binding.splashVideo.visibility = View.GONE
                    displayAppVersion()
                }
            })
    }

    fun displayAppVersion() {
        Timber.e("displayAppVersion()")

        versionTextAnimator =
            ObjectAnimator
                .ofFloat(binding.tvAppVersion, "alpha", 0f, 1f)
                .apply {
                    duration =
                        LabAnimationsManager.getInstance().longAnimationDuration.toLong()

                    addListener(onEnd = {
                        Timber.e("onAnimationEnd()")
                        binding.tvAppVersion.alpha = 1f
                        startProgressAnimation()
                    })
                    startDelay =
                        LabAnimationsManager.getInstance().longAnimationDuration.toLong()
                }

        versionTextAnimator.start()
    }

    private fun startProgressAnimation() {
        Timber.e("startProgressAnimation()")
        fadeProgressAnimator =
            ObjectAnimator
                .ofFloat(binding.progressBar, "alpha", 0f, 1f)
                .apply {
                    duration =
                        LabAnimationsManager.getInstance().mediumAnimationDuration.toLong()
                    addListener(onEnd = {
                        binding.progressBar.alpha = 1f

                        clearAllAnimations()
                    })
                }
        fadeProgressAnimator.start()
    }

    private fun clearAllAnimations() {
        Timber.d("clearAllAnimations() - Use coroutines to clear animations then launch Main Activity")

        // Use coroutines to clear animations then launch Main Activity
        lifecycleScope.launch(coroutineContext) {
            Timber.d("Use coroutines to clear animations")
            LabAnimationsManager.getInstance().clearAnimations(
                versionTextAnimator, fadeProgressAnimator
            )

            delay(TimeUnit.SECONDS.toMillis(3))
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity() {

        val intent = Intent(this, LoginActivity::class.java)

        val sePairThumb: Pair<View, String> =
            Pair.create(
                binding.cvLogo,
                getString(R.string.splash_background_transition_name)
            )

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sePairThumb)

        // Call navigator to switch activity with or without transition according
        // to the device's version running the application
        options.toBundle()?.let {
            Navigator(this).callLoginActivity(
                intent,
                it
            )
            finish()
        }
    }


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onPrepared(mp: MediaPlayer?) {

        //if we have a position on savedInstanceState, the video playback should start from here
        binding.splashVideo.seekTo(position)

        if (position == 0) {
            binding.splashVideo.start()
        } else {
            //if we come from a resumed activity, video playback will be paused
            binding.splashVideo.pause()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Timber.e("Video completed")

        mViewModel.onVideoEnd()
    }
}