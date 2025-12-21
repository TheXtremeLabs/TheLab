package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

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
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.mlkit.ui.compose.base.BaseCameraActivity
import com.riders.thelab.feature.mlkit.ui.compose.utils.MLKitComposeNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TextRecognitionActivity : BaseCameraActivity() {
    private val mViewModel: TextRecognitionViewModel by viewModels<TextRecognitionViewModel>()
    private lateinit var mNavigator: MLKitComposeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        mNavigator = MLKitComposeNavigator(this@TextRecognitionActivity)

        mViewModel.updateShowCamera(hasCameraPermission())
        mViewModel.initRecognitionManager(this)

        enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val textRecognitionState by mViewModel.textRecognitionState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            TextRecognitionContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                state = textRecognitionState,
                                uiEvent = { event ->
                                    when (event) {
                                        is UiEvent.OnAssetImageClicked -> {
                                            mNavigator.launchTextRecognitionAssetsActivity()
                                        }

                                        else -> mViewModel.onEvent(event)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    override fun onCameraPermissionGranted(granted: Boolean) {
        mViewModel.updateShowCamera(granted)
    }
}