package com.riders.thelab.ui.splashscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.theme.Shapes
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primary
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

///////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////
@Composable
fun NoContentFound() {
    val context = LocalContext.current
    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxWidth(.8f),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Lottie(modifier = Modifier.fillMaxSize(.5f), rawResId = R.raw.error_rolling_dark_theme)

            Text(text = "Unable to play splashscreen video.")

            Button(
                modifier = Modifier.fillMaxWidth(.6f),
                onClick = { (context.findActivity() as SplashScreenActivity).finish() }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.action_exit).uppercase(Locale.getDefault()),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoView(viewModel: SplashScreenViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val exoPlayer = ExoPlayer.Builder(LocalContext.current)
        .build()
        .also { exoPlayer ->
            val mediaItem = MediaItem.Builder()
                .setUri(viewModel.videoPath!!)
                .build()
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                exoPlayer.playWhenReady = true
                hideController()
                useController = false
                exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
                exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
//              /* https://stackoverflow.com/questions/27351784/how-to-implement-oncompletionlistener-to-detect-end-of-media-file-in-exoplayer */
                exoPlayer.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        when (playbackState) {
                            Player.STATE_READY -> {
                                Timber.d("State.READY")
                            }

                            Player.STATE_IDLE -> {
                                Timber.i("State.STATE_IDLE")
                            }

                            Player.STATE_BUFFERING -> {
                                Timber.d("State.STATE_BUFFERING")
                            }

                            Player.STATE_ENDED -> {
                                Timber.e("State.STATE_ENDED")

                                scope.launch {
                                    // viewModel.updateVideoViewVisibility(false)
                                    viewModel.updateSwitchContent(true)
                                    delay(250L)
                                    viewModel.updateStartCountDown(true)
                                }
                            }
                        }
                    }
                })
                player = exoPlayer
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }
}

@Composable
fun LoadingContent(viewModel: SplashScreenViewModel) {

    val scope = rememberCoroutineScope()
    val version by viewModel.version
    val progressBarVisibility = remember { mutableStateOf(false) }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .size(96.dp),
                    shape = Shapes.large,
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.ic_lab_twelve_background))
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        painter = painterResource(id = R.drawable.ic_the_lab_12_logo_white),
                        contentDescription = "Lab Icon"
                    )
                }

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = if (LocalInspectionMode.current) "12.0.0" else version,
                    //style = TextStyle(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                )
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp),
                visible = if (LocalInspectionMode.current) false else progressBarVisibility.value
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(2.dp),
                    color = Color.White,
                    trackColor = md_theme_dark_primary
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(500L)
            progressBarVisibility.value = true
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreenContent(viewModel: SplashScreenViewModel) {
    val context = LocalContext.current
    val startCountDown by viewModel.startCountDown

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (null == viewModel.videoPath) {
                NoContentFound()
            } else {

                AnimatedContent(
                    targetState = viewModel.switchContent,
                    transitionSpec = { fadeIn() with fadeOut() },
                    label = ""
                ) { targetState ->
                    if (!targetState) {
                        VideoView(viewModel)
                    } else {
                        LoadingContent(viewModel)
                    }
                }
            }
        }

        if (startCountDown) {
            LaunchedEffect(Unit) {
                delay(2500L)
                (context as SplashScreenActivity).goToLoginActivity()
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
private fun PreviewNoContentFound() {
    TheLabTheme {
        NoContentFound()
    }
}

@DevicePreviews
@Composable
private fun PreviewLoadingContent() {
    val viewModel: SplashScreenViewModel = hiltViewModel()
    TheLabTheme {
        LoadingContent(viewModel)
    }
}

@DevicePreviews
@Composable
private fun PreviewVideoContent() {
    val viewModel: SplashScreenViewModel = hiltViewModel()
    TheLabTheme {
        VideoView(viewModel)
    }
}

@DevicePreviews
@Composable
private fun PreviewSplashScreenContent() {
    val viewModel: SplashScreenViewModel = hiltViewModel()
    TheLabTheme {
        SplashScreenContent(viewModel = viewModel)
    }
}
