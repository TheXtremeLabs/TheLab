package com.riders.thelab.vision.ui.vision

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.riders.thelab.core.camera.compose.CameraView
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun VisionCameraScreen(
    theme: AppTheme,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    cameraSelector: CameraSelector,
    imageAnalyzer: ImageAnalysis.Analyzer? = null
) {
    TheLabTheme(theme = theme, darkTheme = isDarkTheme) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CameraView(
                    theme = theme,
                    darkTheme = isDarkTheme,
                    modifier = Modifier.size(this.maxWidth, this.maxHeight),
                    onViewReferenced = { },
                    cameraSelector = cameraSelector,
                    imageAnalyzer = imageAnalyzer,
                    galleryContent = null
                )
            }
        }
    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewVisionCameraScreen() {
    TheLabTheme(theme = AppTheme.Default) {
        VisionCameraScreen(
            theme = AppTheme.Default,
            isDarkTheme = isSystemInDarkTheme(),
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        )
    }
}