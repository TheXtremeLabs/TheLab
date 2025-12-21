package com.riders.thelab.feature.mlkit.ui.compose.translate

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TranslateActivity : BaseComponentActivity() {
    private val mViewModel: TranslateViewModel by viewModels<TranslateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        // enableEdgeToEdge()

        mViewModel.initTranslateManager(this@TranslateActivity)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    // Register lifecycle events
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val inputToTranslateFlow by mViewModel.inputToTranslateFlow.collectAsStateWithLifecycle()
                    val translatedResult by mViewModel.translationResults.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            TranslateContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                translatedResult = translatedResult,
                                inputToTranslate = mViewModel.inputToTranslate,
                                options = mViewModel.mLanguageOptions,
                                fromSelectedValue = mViewModel.mSourceLanguageSelected,
                                toSelectedValue = mViewModel.mTargetLanguageSelected,
                                uiEvent = { mViewModel.onEvent(it) }
                            )
                        }
                    }

                    LaunchedEffect(inputToTranslateFlow) {
                        Timber.d("LaunchedEffect | input Flow value: $inputToTranslateFlow | coroutineContext: ${this.coroutineContext}")
                        mViewModel.onEvent(UiEvent.OnTranslate)
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }
}