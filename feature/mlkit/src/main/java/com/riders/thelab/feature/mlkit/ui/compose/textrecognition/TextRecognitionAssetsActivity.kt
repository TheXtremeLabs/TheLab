package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
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

@AndroidEntryPoint
class TextRecognitionAssetsActivity : BaseComponentActivity() {

    private val mViewModel: TextRecognitionAssetsViewModel by viewModels<TextRecognitionAssetsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        enableEdgeToEdge()

        mViewModel.initRecognitionManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val theme: AppTheme by mViewModel.uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)
                    val isDarkTheme: Boolean by mViewModel.uiRepository
                        .isThemeDarkMode()
                        .collectAsStateWithLifecycle(initialValue = false)

                    val textRecognitionState by mViewModel.textRecognitionState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme) {

                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            TextRecognitionAssetsContent(
                                theme = theme,
                                darkTheme = isDarkTheme,
                                state = textRecognitionState
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val bitmap = ContextCompat.getDrawable(
            this@TextRecognitionAssetsActivity,
            com.riders.thelab.core.ui.R.drawable.asset_text_recognition
        )?.toBitmap()

        bitmap?.let { mViewModel.recognizeText(it) }
    }


    override fun backPressed() {
        finish()
    }
}