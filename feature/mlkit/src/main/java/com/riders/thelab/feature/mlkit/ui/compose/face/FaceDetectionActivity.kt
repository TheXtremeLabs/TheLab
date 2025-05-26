package com.riders.thelab.feature.mlkit.ui.compose.face

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
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
import com.riders.thelab.core.ui.data.local.UiRepository
import com.riders.thelab.feature.mlkit.ui.compose.base.BaseCameraActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FaceDetectionActivity : BaseCameraActivity() {

    private var faceDetectionType = FACE_DETECTION_NORMAL

    @Inject
    lateinit var uiRepository: UiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        // enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    val theme: AppTheme by uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)
                    val isDarkTheme: Boolean by uiRepository
                        .isThemeDarkMode()
                        .collectAsStateWithLifecycle(initialValue = false)

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            FaceDetectionContent(
                                theme = theme,
                                darkTheme = isDarkTheme,
                                faceDetectionType = faceDetectionType
                            )
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        super.backPressed()

        finish()
    }


    @SuppressLint("NewApi")
    private fun getBundle() {
        faceDetectionType =
            intent.extras?.getByte(EXTRA_FACE_DETECTION_TYPE) ?: FACE_DETECTION_NORMAL
    }

    companion object {
        const val EXTRA_FACE_DETECTION_TYPE = "EXTRA_FACE_DETECTION_TYPE"
        const val FACE_DETECTION_NORMAL: Byte = 0
        const val FACE_DETECTION_MESH: Byte = 1
    }
}