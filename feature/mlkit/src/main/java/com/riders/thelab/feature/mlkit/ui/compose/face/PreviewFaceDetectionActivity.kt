package com.riders.thelab.feature.mlkit.ui.compose.face

import android.graphics.Rect
import androidx.camera.core.CameraSelector
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.riders.thelab.core.camera.compose.CameraView
import com.riders.thelab.core.camera.compose.NoCameraPermissionContent
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.mlkit.ui.compose.cameratest.CameraTestActivity
import com.riders.thelab.feature.mlkit.ui.compose.textrecognition.TextRecognitionActivity
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun FaceDetectionContent(
    theme: AppTheme,
    darkTheme: Boolean,
    faceDetectionType: Byte
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    var bounds: Rect? by remember { mutableStateOf(null) }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp),
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.fillMaxHeight(),
                            onClick = { (context.findActivity() as CameraTestActivity).backPressed() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = "nav_back_icon"
                            )
                        }
                    },
                    title = {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) { Text(text = "Face Detection", textAlign = TextAlign.Center) }
                    }
                )
            }
        ) { contentPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = cameraPermissionState.status,
                    label = "camera_content_transition"
                ) { targetState: PermissionStatus ->

                    if (targetState.isGranted) {
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            CameraView(
                                theme = theme,
                                darkTheme = darkTheme,
                                modifier = Modifier.size(
                                    width = this.maxWidth,
                                    height = this.maxHeight
                                ),
                                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
                                imageAnalyzer = when (faceDetectionType) {
                                    FaceDetectionActivity.FACE_DETECTION_MESH -> FaceMeshDetectionAnalyzer(
                                        previewViewWidth = this.maxWidth.value,
                                        previewViewHeight = this.maxHeight.value,
                                        onFacesDetected = { faceMeshes ->
                                            Timber.d("Recomposition | onFacesDetected | result: ${faceMeshes.toString()}")
                                            bounds = faceMeshes?.get(0)
                                        },
                                        onMeshesBuilt = { faceMeshPoints ->
                                            Timber.d("Recomposition | onMeshesBuilt | result: ${faceMeshPoints.toString()}")

                                        })

                                    else -> FaceDetectionAnalyzer(
                                        previewViewWidth = this.maxWidth.value,
                                        previewViewHeight = this.maxHeight.value,
                                        onFaceDetected = { faceBounds ->
                                            Timber.d("Recomposition | onFaceDetected | result: ${faceBounds.toString()}")
                                            bounds = faceBounds
                                        })
                                },
                                viewReferencedBlock = {}
                            )

                            if (null == bounds) {
                                Box {}
                            } else {
                                AnimatedVisibility(
                                    visible = null != bounds,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .offset(
                                                x = bounds!!.left.dp,
                                                y = bounds!!.top.dp
                                            )
                                            .width(bounds!!.width().dp)
                                            .height(bounds!!.height().dp)
                                            .border(width = 2.dp, color = Color.Red)
                                    )
                                }
                            }
                        }
                    } else if (targetState.shouldShowRationale) {
                        Text("Camera Permission permanently denied")
                    } else {
                        SideEffect {
                            cameraPermissionState.run { launchPermissionRequest() }
                        }
                        NoCameraPermissionContent { (context.findActivity() as TextRecognitionActivity).backPressed() }
                    }
                }
            }
        }
    }

}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewFaceDetectionContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        FaceDetectionContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            faceDetectionType = 1
        )
    }
}