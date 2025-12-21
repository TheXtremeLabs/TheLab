package com.riders.thelab.feature.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseComponentActivity() {

    private val mViewModel: SplashScreenViewModel by viewModels<SplashScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        initViewModel()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val version by mViewModel.version.collectAsStateWithLifecycle()


                    TheLabTheme(
                        theme = AppTheme.Default,
                        darkTheme = isDarkTheme ?: isSystemInDarkTheme()
                    ) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SplashScreenContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                version = version,
                                videoPath = mViewModel.videoPath,
                                switchContent = mViewModel.switchContent,
                                startCountDown = mViewModel.startCountDown,
                                uiEvent = mViewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }

    fun goToMainActivity(withError: Boolean = false, throwable: Exception? = null) {
        Timber.d("goToMainActivity() | throwable: $throwable")

        if (withError) {
            setResult(
                RESULT_CANCELED,
                Intent().apply {
                    putExtra("ERROR_MESSAGE", throwable?.message)
                }
            )
        } else {
            setResult(RESULT_OK)
        }

        finish()
    }

    override fun backPressed() {
        return
    }


    fun initViewModel() {
        Timber.d("initViewModel()")
        mViewModel.initWeakReference(activity = this)
        mViewModel.getAppVersion()
        mViewModel.getVideoPath(activity = this)
    }
}