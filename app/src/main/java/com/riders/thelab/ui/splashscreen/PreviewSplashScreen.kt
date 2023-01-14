package com.riders.thelab.ui.splashscreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.previewprovider.SplashScreenViewModelPreviewProvider
import com.riders.thelab.core.compose.previewprovider.VideoPathPreviewProvider
import com.riders.thelab.core.compose.ui.theme.Shapes
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoView(viewModel: SplashScreenViewModel, videoUri: String) {

    val context = LocalContext.current

    val exoPlayer = ExoPlayer.Builder(LocalContext.current)
        .build()
        .also { exoPlayer ->
            val mediaItem = MediaItem.Builder()
                .setUri(videoUri)
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
                                Log.d("MainActivity", "State.READY")
                            }
                            Player.STATE_IDLE -> {
                                Log.i("MainActivity", "State.STATE_IDLE")
                            }
                            Player.STATE_BUFFERING -> {
                                Log.d("MainActivity", "State.STATE_BUFFERING")
                            }
                            Player.STATE_ENDED -> {
                                Log.e("MainActivity", "State.STATE_ENDED")
                                viewModel.updateVideoViewVisibility(false)
                                viewModel.updateSplashLoadingContentVisibility(true)
                                viewModel.updateStartCountDown(true)
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

@DevicePreviews
@Composable
fun LoadingContent(@PreviewParameter(SplashScreenViewModelPreviewProvider::class) viewModel: SplashScreenViewModel) {

    val scope = rememberCoroutineScope()
    val version by viewModel.version
    val progressBarVisibility = remember { mutableStateOf(false) }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            /*Layout(
                modifier = Modifier.align(Alignment.Center),
                content = {
                    Card(
                        modifier = Modifier.size(72.dp),
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

                    Text(text = viewModel.appVersion.value.toString())
                },
                measurePolicy = { measurables, constraints ->
                    val icon = measurables[0].measure(constraints)
                    val appVersion = measurables[1].measure(constraints)
                    layout(
                        width = appVersion.width + icon.width,
                        height = maxOf(appVersion.height, icon.height * 2, constraints.minHeight)
                    ) {
                        icon.placeRelative(0, 0)
                        appVersion.placeRelative(0, icon.height)
                    }
                })*/
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
                    text = version,
                    style = TextStyle(color = Color.White)
                )
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp),
                visible = progressBarVisibility.value
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

@Composable
fun SplashScreenContent(
    viewModel: SplashScreenViewModel,
    videoPath: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val videoViewVisibilityState by viewModel.videoViewVisibility
    val splashLoadingContentVisibilityState by viewModel.splashLoadingContentVisibility
    val startCountDown by viewModel.startCountDown

    TheLabTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            AnimatedVisibility(
                visible = if (LocalInspectionMode.current) false else videoViewVisibilityState,
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing
                    )
                )
            ) { VideoView(viewModel, videoUri = videoPath) }

            AnimatedVisibility(
                visible = if (LocalInspectionMode.current) true else splashLoadingContentVisibilityState,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 750,
                        easing = FastOutLinearInEasing
                    )
                )
            ) {
                LoadingContent(viewModel)
            }
        }

        if (startCountDown) {
            LaunchedEffect(Unit) {
                scope.launch {
                    delay(2500L)
                    (context as SplashScreenActivity).goToLoginActivity()
                }
            }
        }
    }
}
