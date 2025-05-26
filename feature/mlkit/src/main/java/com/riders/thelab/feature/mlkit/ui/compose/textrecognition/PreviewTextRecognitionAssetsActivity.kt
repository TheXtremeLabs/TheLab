package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.feature.mlkit.data.local.compose.textrecognition.TextRecognitionState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.AppTypography
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.mlkit.data.local.model.TextRecognitionModel
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecognizedText(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    recognitionModel: TextRecognitionModel
) {
    Timber.d("Recomposition | RecognizedText() | model : $recognitionModel")

    val lazyListState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        LazyColumn(
            modifier = modifier,
            state = lazyListState,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Text(modifier = Modifier.fillMaxWidth(), text = "Result found from asset")
            }

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Text",
                        style = AppTypography.titleLarge
                    )
                }
            }

            item {
                Text(text = recognitionModel.text)
            }

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Blocks",
                        style = AppTypography.titleLarge
                    )
                }
            }

            items(items = recognitionModel.blocks) { block ->
                Text(
                    text = block.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Start
                )
            }

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Lines", style = AppTypography.titleLarge
                    )
                }
            }

            items(items = recognitionModel.lines) { line ->
                repeat(line.size) {
                    val subLine = line[it]
                    Text(
                        text = subLine.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextRecognitionAssetsContent(
    theme: AppTheme,
    darkTheme: Boolean,
    state: TextRecognitionState
) {
    val context = LocalContext.current

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp),
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier.fillMaxHeight(),
                            onClick = { (context.findActivity() as TextRecognitionAssetsActivity).backPressed() }
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
                        ) {
                            Text(
                                text = "Text Recognition (from assets)",
                                textAlign = TextAlign.Center
                            )
                        }
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
                when (state) {
                    is TextRecognitionState.Loading,
                    is TextRecognitionState.Idle -> {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    }

                    is TextRecognitionState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("Error while processing file for recognition")
                            Button(onClick = { (context.findActivity() as TextRecognitionAssetsActivity).backPressed() }) {
                                Text(text = "Go Back")
                            }
                        }
                    }

                    is TextRecognitionState.Recognized -> {
                        Timber.d("Recomposition | TextRecognitionState.Recognized")

                        (context.findActivity() as TextRecognitionAssetsActivity).runOnUiThread {
                            Toast.makeText(
                                context,
                                "TextRecognitionState.Recognized",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            RecognizedText(
                                theme = theme,
                                darkTheme = darkTheme,
                                modifier = Modifier.fillMaxSize(),
                                recognitionModel = state.recognitionModel
                            )
                        }
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
private fun PreviewTextRecognitionContent(@PreviewParameter(PreviewProvider::class) state: TextRecognitionState) {

    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        TextRecognitionAssetsContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            state = state
        )
    }
}