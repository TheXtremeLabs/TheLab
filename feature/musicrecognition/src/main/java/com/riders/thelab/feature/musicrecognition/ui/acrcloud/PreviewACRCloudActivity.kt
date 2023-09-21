package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primaryContainer
import com.riders.thelab.core.ui.compose.theme.success
import com.riders.thelab.feature.musicrecognition.R
import kotlinx.coroutines.delay


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun Idle(viewModel: ACRCloudViewModel) {
    TheLabTheme {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier, text = viewModel.result)
            Button(
                modifier = Modifier,
                onClick = { viewModel.startRecognition() },
                enabled = viewModel.canLaunchAudioRecognition
            ) {
                Text(
                    text = if (!viewModel.isRecognizing) stringResource(id = R.string.msg_start_recognition) else stringResource(
                        id = R.string.msg_stop_recognition
                    )
                )
            }
        }
    }
}

@Composable
fun Searching(viewModel: ACRCloudViewModel) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(modifier = Modifier, text = viewModel.result)
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(.75f))
        }
    }
}

@Composable
fun RecognitionResult(viewModel: ACRCloudViewModel, state: ACRUiState.RecognitionSuccessful) {
    var expanded = remember { mutableStateOf(false) }

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(modifier = Modifier, text = state.songFetched.title)
                    Text(modifier = Modifier, text = state.songFetched.artists.joinToString(","))
                    Text(modifier = Modifier, text = state.songFetched.album)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(.4f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(40.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = success)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "round check icon",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = expanded.value) {
                Button(
                    modifier = Modifier,
                    onClick = { viewModel.startRecognition() },
                    enabled = viewModel.canLaunchAudioRecognition
                ) {
                    Text(
                        text = if (!viewModel.isRecognizing) stringResource(id = R.string.msg_start_recognition) else stringResource(
                            id = R.string.msg_stop_recognition
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(2_000L)
        expanded.value = true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACRCloudActivityContent(viewModel: ACRCloudViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Register lifecycle events
    viewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

    val widthFraction by animateFloatAsState(
        targetValue = when (uiState) {
            is ACRUiState.RecognitionSuccessful -> {
                .7f
            }

            else -> {
                .6f
            }
        },
        animationSpec = tween(2000),
        label = "widthFraction_animation"
    )

    val heightFraction by animateFloatAsState(
        targetValue = when (uiState) {
            is ACRUiState.RecognitionSuccessful -> {
                .7f
            }

            else -> {
                .45f
            }
        },
        animationSpec = tween(2000),
        label = "heightFraction_animation"
    )

    TheLabTheme {
        Scaffold(
            topBar = {
                TheLabTopAppBar(title = stringResource(id = R.string.acr_cloud_app_name)) { }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.Start
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.4f)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (!isSystemInDarkTheme()) md_theme_light_primaryContainer else md_theme_dark_primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        )
                    ) {
                        AnimatedContent(
                            targetState = uiState,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "music recognition content animation"
                        ) { targetState ->
                            when (targetState) {
                                is ACRUiState.Idle -> {
                                    Idle(viewModel = viewModel)
                                }

                                is ACRUiState.ProcessRecognition -> {
                                    Searching(viewModel = viewModel)
                                }

                                is ACRUiState.RecognitionSuccessful -> {
                                    RecognitionResult(viewModel = viewModel, state = targetState)
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
fun PreviewIdle() {
    val viewModel: ACRCloudViewModel = hiltViewModel()
    TheLabTheme {
        Idle(viewModel = viewModel)
    }
}

@DevicePreviews
@Composable
fun PreviewSearching() {
    val viewModel: ACRCloudViewModel = hiltViewModel()
    TheLabTheme {
        Searching(viewModel = viewModel)
    }
}

@DevicePreviews
@Composable
fun PreviewRecognitionResult() {
    val viewModel: ACRCloudViewModel = hiltViewModel()
    TheLabTheme {
        RecognitionResult(
            viewModel = viewModel,
            state = ACRUiState.RecognitionSuccessful(songFetched = Song.mock)
        )
    }
}

@DevicePreviews
@Composable
fun PreviewMainActivityContent() {
    val viewModel: ACRCloudViewModel = hiltViewModel()
    TheLabTheme {
        ACRCloudActivityContent(viewModel = viewModel)
    }
}