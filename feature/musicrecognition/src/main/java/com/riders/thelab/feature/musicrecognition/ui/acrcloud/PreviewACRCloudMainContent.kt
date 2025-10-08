package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImagePainter
import com.riders.thelab.core.common.utils.LabPackageManager
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState
import com.riders.thelab.core.data.local.model.music.toModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.color.success
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.component.fab.PulsarFab
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.compose.utils.loadImage
import com.riders.thelab.feature.musicrecognition.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun Idle(
    theme: AppTheme, darkTheme: Boolean,
    result: String,
    canLaunchAudioRecognition: Boolean,
    onStartRecognition: () -> Unit,
    isRecognizing: Boolean
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // Text
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = this.maxHeight / 4f),
                text = "Tap on logo to start recognition",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.W700,
                fontSize = 24.sp
            )

            // Logo
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(this.maxWidth),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(160.dp),
                    onClick = onStartRecognition,
                    enabled = canLaunchAudioRecognition,
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.ic_lab_6_lab),
                            colorFilter = ColorFilter.tint(Color.White),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ACRError(
    theme: AppTheme, darkTheme: Boolean,
    canLaunchAudioRecognition: Boolean,
    onStartRecognition: () -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
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
fun Searching(theme: AppTheme, darkTheme: Boolean, result: String) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(modifier = Modifier, text = result)
            PulsarFab {
                Box(modifier = Modifier.clip(CircleShape), contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(56.dp),
                        painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.ic_lab_6_lab),
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun RecognitionError(theme: AppTheme, darkTheme: Boolean) {
    TheLabTheme(theme = theme) {
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
fun RecognitionResult(
    theme: AppTheme,
    darkTheme: Boolean,
    state: ACRUiState.RecognitionSuccessful,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val expanded = remember { mutableStateOf(false) }

    val painter: AsyncImagePainter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = state.songModel.albumThumbUrl!!,
        isSvg = false,
        placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
    )

    val painterState: AsyncImagePainter.State by painter.state.collectAsStateWithLifecycle()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
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

                                    LaunchedEffect(painterState) {
                                        scope.launch {
                                            val image: Bitmap = painterState.loadImage() ?: return@launch

                                            uiEvent.invoke(
                                                UiEvent.UpdateMusicModelImageBase64(
                                                    currentSong = state.songModel,
                                                    imageBitmap = image
                                                )
                                            )
                                        }
                                    }
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
                            text = state.songModel.title,
                            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                        )
                        Text(
                            modifier = Modifier,
                            text = state.songModel.artists,
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Text(
                            modifier = Modifier, text = state.songModel.album,
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }

                    AnimatedVisibility(
                        visible = if (LocalInspectionMode.current) true else {
                            LabPackageManager
                                .getInstance(context)
                                .isInstalled(Constants.PACKAGE_NAME_SPOTIFY)
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable(enabled = true) {
                                    uiEvent.invoke(UiEvent.OpenModelInSpotify(state.songModel))
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Open in Spotify",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            SpotifyIcon(modifier = Modifier.size(36.dp), darkTheme = darkTheme)
                        }
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
fun ACRCloudMainContent(
    theme: AppTheme,
    darkTheme: Boolean,
    contentPadding: PaddingValues,
    acrUiState: ACRUiState,
    result: String,
    canLaunchAudioRecognition: Boolean,
    onStartRecognition: () -> Unit,
    isRecognizing: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
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
                            theme = theme, darkTheme = darkTheme,
                            result = result,
                            canLaunchAudioRecognition = canLaunchAudioRecognition,
                            onStartRecognition = onStartRecognition,
                            isRecognizing = isRecognizing
                        )
                    }

                    is ACRUiState.ProcessRecognition -> {
                        Searching(theme = theme, darkTheme = darkTheme, result = result)
                    }

                    is ACRUiState.RecognitionSuccessful -> {
                        RecognitionResult(
                            theme = theme,
                            darkTheme = darkTheme,
                            state = targetState,
                            uiEvent = uiEvent
                        )
                    }

                    is ACRUiState.RecognitionError -> {
                        RecognitionError(theme = theme, darkTheme = darkTheme)
                    }

                    is ACRUiState.Error -> {
                        ACRError(
                            theme = theme, darkTheme = darkTheme,
                            canLaunchAudioRecognition = canLaunchAudioRecognition,
                            onStartRecognition = onStartRecognition
                        )
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
@DevicePreviewsPhoneOnly
@Composable
fun PreviewIdle(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Idle(
            theme = appTheme, darkTheme = isSystemInDarkTheme(),
            result = "Wheezy feat. Gunna",
            canLaunchAudioRecognition = true,
            onStartRecognition = {},
            isRecognizing = false
        )
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun PreviewACRError(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        ACRError(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            canLaunchAudioRecognition = true,
            onStartRecognition = {})
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun PreviewSearching(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Searching(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            result = "Wheezy feat. Gunna"
        )
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun PreviewRecognitionResult(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        RecognitionResult(
            theme = appTheme, darkTheme = isSystemInDarkTheme(),
            state = ACRUiState.RecognitionSuccessful(songModel = Song.mock.toModel())
        ) {}
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun PreviewRecognitionError(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        RecognitionError(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme()
        )
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewACRCloudMainContent(@PreviewParameter(AppThemePreviewProvider::class) appTHeme: AppTheme) {
    TheLabTheme(theme = appTHeme, darkTheme = isSystemInDarkTheme()) {
        ACRCloudMainContent(
            theme = appTHeme, darkTheme = isSystemInDarkTheme(),
            acrUiState = ACRUiState.Idle,
            contentPadding = PaddingValues(10.dp),
            result = "Wheezy feat. Gunna",
            canLaunchAudioRecognition = true,
            onStartRecognition = {},
            isRecognizing = false
        ) {}
    }
}