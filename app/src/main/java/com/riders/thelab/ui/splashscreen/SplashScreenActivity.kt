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
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
class SplashScreenActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + Job()

    companion object {
        private const val ANDROID_RES_PATH = "android.resource://"
        private const val SEPARATOR = "/"
    }

    private val mViewModel: SplashScreenViewModel by viewModels()


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

        mViewModel.retrieveAppVersion(this)

        var videoPath: String = ""

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    videoPath =
                        ANDROID_RES_PATH +
                                packageName.toString() +
                                SEPARATOR +
                                //Smartphone portrait video or Tablet landscape video
                                if (!LabCompatibilityManager.isTablet(this@SplashScreenActivity)) R.raw.splash_intro_testing_sound_2 else R.raw.splash_intro_testing_no_sound_tablet

                } catch (e: Exception) {
                    Timber.e(e)
                }

                setContent {
                    SplashScreenContent(mViewModel, videoPath)
                }
            }
        }
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    fun goToLoginActivity() {
        Timber.d("goToLoginActivity()")
        Navigator(this).callLoginActivity()
        finish()
    }
}