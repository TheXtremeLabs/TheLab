package com.riders.thelab.feature.mlkit.ui.compose.ink

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.Redo
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.bottomsheet.BottomSheetContent
import com.riders.thelab.core.ui.compose.component.draw.DrawBox
import com.riders.thelab.core.ui.compose.component.draw.rememberDrawController
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.launch
import timber.log.Timber

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InkRecognitionContent(
    theme: AppTheme,
    darkTheme: Boolean,
    inkRecognitionValue: String,
    onAddNewTouchEvent: (Offset, Int) -> Unit,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    var undoVisibility by remember { mutableStateOf(false) }
    var redoVisibility by remember { mutableStateOf(false) }
    val drawController = rememberDrawController()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = bottomSheetScaffoldState,
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp),
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.fillMaxHeight(),
                            onClick = { (context.findActivity() as InkRecognitionActivity).backPressed() }
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
                        ) { Text(text = "Ink Recognition", textAlign = TextAlign.Center) }
                    },
                    actions = {
                        // Clear all button
                        Box(
                            modifier = Modifier.padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                modifier = Modifier.padding(4.dp),
                                contentPadding = PaddingValues(end = 8.dp),
                                onClick = {
                                    uiEvent.invoke(UiEvent.OnClearAllClicked)
                                    drawController.reset()
                                },
                                shape = RoundedCornerShape(16.dp),
                                enabled = drawController.pathList.isNotEmpty()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(4.dp),
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "clear_all_icon"
                                    )

                                    Text("Clear all")
                                }
                            }
                        }

                        // Recognize circle button
                        Box(
                            modifier = Modifier.padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(4.dp),
                                contentPadding = PaddingValues(8.dp),
                                onClick = { drawController.saveBitmap() },
                                shape = CircleShape
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    imageVector = Icons.Rounded.CheckCircleOutline,
                                    contentDescription = "check_icon"
                                )
                            }
                        }
                    }
                )
            },
            sheetContent = {
                if (inkRecognitionValue.isNotEmpty()) {
                    ModalBottomSheet(
                        sheetState = bottomSheetScaffoldState.bottomSheetState,
                        onDismissRequest = {
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.hide()
                                uiEvent.invoke(UiEvent.OnDismissBottomSheet)
                            }
                        }
                    ) {
                        BottomSheetContent(theme = theme, darkTheme = darkTheme) {
                            Text(
                                text = inkRecognitionValue,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            sheetPeekHeight = 128.dp,
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f),
                    contentAlignment = Alignment.Center
                ) {
                    DrawBox(
                        modifier = Modifier.fillMaxSize(),
                        drawController = drawController,
                        bitmapCallback = { imageBitmap, error ->
                            imageBitmap?.let {
                                uiEvent.invoke(UiEvent.OnSaveBitmap(it.asAndroidBitmap()))
                            }

                            error?.let {
                                Timber.e("Recomposition | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
                            }
                        },
                        addNewTouchEvent = onAddNewTouchEvent
                    ) { undoCount, redoCount ->
                        undoVisibility = undoCount != 0
                        redoVisibility = redoCount != 0
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Undo Button
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp),
                        onClick = { drawController.unDo() },
                        contentPadding = PaddingValues(vertical = 4.dp),
                        enabled = undoVisibility
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Undo,
                                contentDescription = "undo_icon"
                            )
                            Text(text = "Undo")
                        }
                    }

                    // Redo Button
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 24.dp),
                        onClick = { drawController.reDo() },
                        contentPadding = PaddingValues(vertical = 4.dp),
                        enabled = redoVisibility
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Redo,
                                contentDescription = "undo_icon"
                            )
                            Text(text = "Redo")
                        }
                    }

                }
            }
        }
    }

    LaunchedEffect(inkRecognitionValue) {
        if (inkRecognitionValue.isEmpty()) {
            Timber.e("LaunchedEffect() | inkRecognitionValue is empty")

            bottomSheetScaffoldState.bottomSheetState.hide()
        } else {
            Timber.d("LaunchedEffect() | inkRecognitionValue: $inkRecognitionValue")

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
private fun PreviewInkRecognitionContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        InkRecognitionContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            inkRecognitionValue = "",
            onAddNewTouchEvent = { _, _ -> },
            uiEvent = {}
        )
    }
}