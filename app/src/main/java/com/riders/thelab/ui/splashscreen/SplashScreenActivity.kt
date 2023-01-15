package com.riders.thelab.ui.splashscreen

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.R
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
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

            repeatOnLifecycle(Lifecycle.State.CREATED) {
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
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SplashScreenContent(mViewModel, videoPath)
                        }
                    }
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