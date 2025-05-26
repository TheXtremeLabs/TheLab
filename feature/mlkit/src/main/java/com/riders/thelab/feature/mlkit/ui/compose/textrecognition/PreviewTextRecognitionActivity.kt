package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.riders.mlkitcompose.data.local.compose.textrecognition.TextRecognitionState
import com.riders.thelab.core.camera.compose.CameraView
import com.riders.thelab.core.camera.compose.NoCameraPermissionContent
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.bottomsheet.BottomSheetContent
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.mlkit.data.local.model.OverlayDataFrameInfo
import kotlinx.coroutines.launch
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TextRecognitionSuccessState(
    theme: AppTheme,
    darkTheme: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    var textDetectedBoundingBoxes: List<OverlayDataFrameInfo>? by remember { mutableStateOf(null) }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            CameraView(
                theme = theme, darkTheme = darkTheme,
                modifier = Modifier.size(
                    width = this.maxWidth,
                    height = this.maxHeight
                ),
                imageAnalyzer = TextRecognitionAnalyzer(
                    previewViewWidth = this.maxWidth.value,
                    previewViewHeight = this.maxHeight.value,
                    onTextDetected = { model ->
                        Timber.d("Recomposition | onTextDetected | result: ${model.toString()}")
                        model?.let { UiEvent.OnTextRecognition(it) }
                    },
                    onTextDetectedBoundingBoxes = { list -> textDetectedBoundingBoxes = list },
                    onCallback = { visionResultImage ->
                        Timber.d("Recomposition | onCallback | result: ${visionResultImage.toString()}")

                        uiEvent.invoke(
                            UiEvent.StartTextRecognition(visionResultImage)
                        )
                    }
                ),
                onViewReferenced = {}
            )

            if (!textDetectedBoundingBoxes.isNullOrEmpty()) {
                repeat(textDetectedBoundingBoxes!!.size) {
                    Box(
                        modifier = Modifier
                            .zIndex(5f)
                            .offset(
                                x = textDetectedBoundingBoxes!![it].barcodeFrameX.dp,
                                y = textDetectedBoundingBoxes!![it].barcodeFrameY.dp
                            )
                            .width(textDetectedBoundingBoxes!![it].barcodeFrameWidth.dp)
                            .height(textDetectedBoundingBoxes!![it].barcodeFrameHeight.dp)
                            .border(width = 2.dp, color = Color.Red)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(48.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Card(
                    modifier = Modifier.size(56.dp),
                    onClick = { uiEvent.invoke(UiEvent.OnAssetImageClicked) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.asset_text_recognition),
                        contentDescription = "asset_test_image"
                    )
                }
            }
        }
    }
}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun TextRecognitionContent(
    theme: AppTheme,
    darkTheme: Boolean,
    state: TextRecognitionState,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            scaffoldState = bottomSheetScaffoldState,
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp),
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.fillMaxHeight(),
                            onClick = { (context.findActivity() as TextRecognitionActivity).backPressed() }
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
                        ) { Text(text = "Text Recognition", textAlign = TextAlign.Center) }
                    }
                )
            },
            sheetContent = {
                if (state is TextRecognitionState.Recognized && state.recognitionModel.blocks.isNotEmpty()) {
                    if (!bottomSheetScaffoldState.bottomSheetState.isVisible) {
                        LaunchedEffect(Unit) {
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    }

                    ModalBottomSheet(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .heightIn(56.dp, 250.dp),
                        sheetState = bottomSheetScaffoldState.bottomSheetState,
                        onDismissRequest = {
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.hide()
                            }
                        }
                    ) {
                        BottomSheetContent(theme = theme, darkTheme = darkTheme) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.QrCode2,
                                    contentDescription = null
                                )
                                Text(
                                    text = state.recognitionModel.text,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Text(
                                text = "Result blocks found ${state.recognitionModel.blocks.size}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Box(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            },
            sheetPeekHeight = 70.dp,
            containerColor = Color.Transparent
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
                        TextRecognitionSuccessState(
                            theme = theme,
                            darkTheme = darkTheme,
                            uiEvent = uiEvent
                        )
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
private fun PreviewTextRecognitionSuccessState(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        TextRecognitionSuccessState(theme = appTheme, darkTheme = isSystemInDarkTheme()) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewTextRecognitionContent(@PreviewParameter(PreviewProvider::class) state: TextRecognitionState) {
    TheLabTheme(theme = AppTheme.Default) {
        TextRecognitionContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            state = state
        ) {}
    }
}