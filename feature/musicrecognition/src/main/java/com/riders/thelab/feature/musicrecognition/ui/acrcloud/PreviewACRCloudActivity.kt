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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SignalWifiConnectedNoInternet4
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.fab.PulsarFab
import com.riders.thelab.core.ui.compose.component.network.NoNetworkConnection
import com.riders.thelab.core.ui.compose.component.toast.Toast
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_onBackground
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_onError
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_onBackground
import com.riders.thelab.core.ui.compose.theme.md_theme_light_onPrimaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primaryContainer
import com.riders.thelab.core.ui.compose.theme.success
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.feature.musicrecognition.R
import kotlinx.coroutines.delay
import timber.log.Timber


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun Idle(
    result: String,
    canLaunchAudioRecognition: Boolean,
    onStartRecognition: () -> Unit,
    isRecognizing: Boolean
) {
    TheLabTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(modifier = Modifier, text = result ?: "")
            Button(
                modifier = Modifier,
                onClick = onStartRecognition,
                enabled = canLaunchAudioRecognition
            ) {
                Text(
                    text = if (!isRecognizing) stringResource(id = R.string.msg_start_recognition) else stringResource(
                        id = R.string.msg_stop_recognition
                    )
                )
            }
        }
    }
}

@Composable
fun ACRError(
    canLaunchAudioRecognition: Boolean,
    onStartRecognition: () -> Unit
) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {

            Box(modifier = Modifier.fillMaxWidth(.75f), contentAlignment = Alignment.Center) {
                Lottie(
                    modifier = Modifier.fillMaxSize(),
                    rawResId = com.riders.thelab.core.ui.R.raw.lottie_hot_coffee_loading
                )
            }

            Text(
                modifier = Modifier,
                text = "An error occurred. please verify your internet connection or maybe retry to get the playing song"
            )
            Button(
                modifier = Modifier,
                onClick = onStartRecognition,
                enabled = canLaunchAudioRecognition
            ) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun Searching(result: String) {
    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(modifier = Modifier, text = result ?: "")
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
fun RecognitionError() {
    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "An error occurred while processing audio data. Please retry.")
        }
    }
}

@Composable
fun RecognitionResult(state: ACRUiState.RecognitionSuccessful) {
    val expanded = remember { mutableStateOf(false) }

    val painter: AsyncImagePainter = getCoilAsyncImagePainter(
        context = LocalContext.current,
        dataUrl = state.songFetched.albumThumbUrl
    )

    val painterState: AsyncImagePainter.State = painter.state

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
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            when (painterState) {
                                is AsyncImagePainter.State.Loading -> {
                                    CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                                }

                                is AsyncImagePainter.State.Success -> {
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

                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(.5f)
                                            .fillMaxHeight(.4f), contentAlignment = Alignment.Center
                                    ) {
                                        Lottie(
                                            modifier = Modifier,
                                            rawResId = com.riders.thelab.core.ui.R.raw.lottie_hot_coffee_loading
                                        )
                                    }
                                }
                            }
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


@Composable
fun ACRCloudActivityContent(
    acrUiState: ACRUiState,
    networkState: NetworkState,
    result: String,
    canLaunchAudioRecognition: Boolean,
    onStartRecognition: () -> Unit,
    isRecognizing: Boolean
) {

    val isConnected = networkState == NetworkState.Available
    var currentCapabilityChangedCount by remember { mutableIntStateOf(0) }
    val maxCapabilitiesCountTaken = 1

    val animatedHeight by animateDpAsState(
        targetValue = when (acrUiState) {
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
                TheLabTopAppBar(
                    title = stringResource(id = R.string.acr_cloud_app_name),
                    navigationIcon = {})
            },
            floatingActionButton = {

                AnimatedContent(
                    targetState = isRecognizing,
                    label = "something"
                ) { target ->
                    if (!target) {
                        androidx.compose.material.FloatingActionButton(
                            backgroundColor = if (!isRecognizing) {
                                md_theme_light_onPrimaryContainer
                            } else {
                                if (!isSystemInDarkTheme()) md_theme_light_onBackground else md_theme_dark_onBackground
                            },
                            onClick = {
                                if (!isRecognizing) {
                                    onStartRecognition()
                                } else {
                                    Timber.e("FloatingActionButton | onClick | recognition is already running")
                                }
                            },
                        ) {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.ic_the_lab_12_logo_white),
                                contentDescription = "the lab logo",
                                tint = Color.White
                            )
                        }
                    } else {
                        Box(modifier = Modifier)
                    }
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                if (isConnected.equals(false)) {
                    NoNetworkConnection()
                } else {
                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .fillMaxSize()
                            .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        AnimatedContent(
                            targetState = acrUiState,
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
                                    Idle(
                                        result = result,
                                        canLaunchAudioRecognition = canLaunchAudioRecognition,
                                        onStartRecognition = onStartRecognition,
                                        isRecognizing = isRecognizing
                                    )
                                }

                                is ACRUiState.ProcessRecognition -> {
                                    Searching(result = result)
                                }

                                is ACRUiState.RecognitionSuccessful -> {
                                    RecognitionResult(state = targetState)
                                }

                                is ACRUiState.RecognitionError -> {
                                    RecognitionError()
                                }

                                is ACRUiState.Error -> {
                                    ACRError(
                                        canLaunchAudioRecognition = canLaunchAudioRecognition,
                                        onStartRecognition = onStartRecognition
                                    )
                                }
                            }
                        }
                    }
                }

                AnimatedContent(
                    targetState = networkState,
                    label = "Toast animation content"
                ) { targetState ->
                    when (targetState) {
                        is NetworkState.Available -> {
                            Toast(
                                message = "You are connected to the internet",
                                imageVector = Icons.Filled.Check,
                                containerColor = success
                            )
                            currentCapabilityChangedCount = 0
                        }

                        /*is NetworkConnectionState.OnCapabilitiesChanged -> {
                            currentCapabilityChangedCount += 1

                            if (currentCapabilityChangedCount < maxCapabilitiesCountTaken) {
                                Toast(
                                    message = "Connection capabilities changes",
                                    imageVector = Icons.Filled.SyncAlt,
                                    containerColor = Orange
                                )
                            }
                        }*/

                        is NetworkState.Losing -> {
                            Toast(
                                message = "Losing Internet connection !",
                                imageVector = Icons.Filled.SignalWifiConnectedNoInternet4,
                                containerColor = md_theme_dark_onError
                            )
                        }

                        is NetworkState.Lost,
                        is NetworkState.Unavailable -> {
                            Toast(
                                message = "Internet is unavailable",
                                imageVector = Icons.Filled.AirplanemodeActive,
                                containerColor = md_theme_dark_onError
                            )
                            currentCapabilityChangedCount = 0
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
    TheLabTheme {
        Idle(
            result = "Wheezy feat. Gunna",
            canLaunchAudioRecognition = true,
            onStartRecognition = {},
            isRecognizing = false
        )
    }
}

@DevicePreviews
@Composable
fun PreviewACRError() {
    TheLabTheme {
        ACRError(canLaunchAudioRecognition = true, onStartRecognition = {})
    }
}

@DevicePreviews
@Composable
fun PreviewSearching() {
    TheLabTheme {
        Searching(result = "Wheezy feat. Gunna")
    }
}

@DevicePreviews
@Composable
fun PreviewRecognitionResult() {
    TheLabTheme {
        RecognitionResult(
            state = ACRUiState.RecognitionSuccessful(songFetched = Song.mock)
        )
    }
}

@DevicePreviews
@Composable
fun PreviewRecognitionError() {
    TheLabTheme { RecognitionError() }
}

@DevicePreviews
@Composable
fun PreviewMainActivityContent(@PreviewParameter(PreviewProviderACRCloud::class) acrUiState: ACRUiState) {

    TheLabTheme {
        ACRCloudActivityContent(
            acrUiState = acrUiState,
            networkState = NetworkState.Available,
            result = "Wheezy feat. Gunna",
            canLaunchAudioRecognition = true,
            onStartRecognition = {},
            isRecognizing = false
        )
    }
}