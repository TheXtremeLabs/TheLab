package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.PulsarFab
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_onPrimaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primaryContainer
import com.riders.thelab.core.ui.compose.theme.success
import com.riders.thelab.feature.musicrecognition.R
import kotlinx.coroutines.delay
import timber.log.Timber


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun Idle(viewModel: ACRCloudViewModel) {
    TheLabTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Top
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(modifier = Modifier, text = viewModel.result)
            PulsarFab {
                Image(
                    modifier = Modifier.size(72.dp),
                    painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.ic_the_lab_12_logo_white),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun RecognitionResult(viewModel: ACRCloudViewModel, state: ACRUiState.RecognitionSuccessful) {
    var expanded = remember { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(state.songFetched.albumThumbUrl)
            .apply {
                crossfade(true)
                allowHardware(false)
                transformations(RoundedCornersTransformation(16.dp.value))
            }
            .build(),
        placeholder = painterResource(com.riders.thelab.core.ui.R.drawable.logo_colors),
        onLoading = {
            Timber.i("rememberAsyncImagePainter | Loading Image...")
        },
        onSuccess = {
            Timber.d("rememberAsyncImagePainter | Image successfully loaded")
        },
        onError = {
            Timber.e("rememberAsyncImagePainter | Error while loading Image")
        }
    )
    val painterState = painter.state

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(modifier = Modifier.padding(8.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth(.8f)) {
                        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                modifier = Modifier
                                    .width(this.maxWidth - 16.dp)
                                    .height(this.maxWidth - 16.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                painter = painter,
                                contentDescription = "album thumb image",
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier,
                            text = state.songFetched.title,
                            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                        )
                        Text(
                            modifier = Modifier,
                            text = state.songFetched.artists.joinToString(","),
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Text(
                            modifier = Modifier, text = state.songFetched.album,
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
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

    val animatedHeight by animateDpAsState(
        targetValue = when (uiState) {
            is ACRUiState.ProcessRecognition -> {
                0.dp
            }

            else -> {
                56.dp
            }
        },
        animationSpec = tween(500),
        label = "heightFraction_animation"
    )


    TheLabTheme {
        androidx.compose.material.Scaffold(
            modifier = Modifier.background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            topBar = {
                TheLabTopAppBar(title = stringResource(id = R.string.acr_cloud_app_name)) { }
            },
            floatingActionButton = {
                androidx.compose.material.FloatingActionButton(
                    onClick = {
                        viewModel.startRecognition()
                    }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.ic_the_lab_12_logo_black),
                        contentDescription = "the lab logo",
                        tint = md_theme_dark_onPrimaryContainer
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            bottomBar = {
                androidx.compose.material.BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedHeight)
                        .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
                    backgroundColor = if (!isSystemInDarkTheme()) md_theme_light_primaryContainer else md_theme_dark_primaryContainer,
                    cutoutShape = CircleShape
                ) {

                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedContent(
                    targetState = uiState,
                    transitionSpec = {
                        fadeIn(
                            animationSpec = tween(
                                300,
                                300
                            )
                        ) + slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(300, 300)
                        ) togetherWith fadeOut(
                            animationSpec = tween(300, 300)
                        ) + slideOutOfContainer(
                            animationSpec = tween(300, 300),
                            towards = AnimatedContentTransitionScope.SlideDirection.Down
                        )
                    },
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