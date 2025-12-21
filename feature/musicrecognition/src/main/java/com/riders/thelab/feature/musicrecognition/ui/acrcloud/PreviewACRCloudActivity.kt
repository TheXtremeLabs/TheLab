package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState
import com.riders.thelab.core.data.local.model.music.MusicRecognitionModel
import com.riders.thelab.core.data.local.model.music.toModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.color.md_theme_light_onPrimaryContainer
import com.riders.thelab.core.ui.compose.component.LabHorizontalViewPagerGeneric
import com.riders.thelab.core.ui.compose.component.network.NoNetworkConnection
import com.riders.thelab.core.ui.compose.component.tab.LabTabRow
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun Modifier.shimmerLoading(durationMillis: Int = 1_000): Modifier {
    val transition = rememberInfiniteTransition(label = "infinite_shimmer_transition")

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "infinite_shimmer_translate_animation"
    )

    return drawBehind {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.LightGray.copy(alpha = .2f),
                    Color.LightGray.copy(alpha = 1f),
                    Color.LightGray.copy(alpha = .2f)
                ),
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 100f, y = translateAnimation + 100f)
            )
        )
    }
}

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun SpotifyIcon(darkTheme: Boolean, modifier: Modifier = Modifier) {
    AnimatedContent(targetState = darkTheme) { targetState: Boolean ->
        if (!targetState) {
            Image(
                modifier = modifier,
                painter = painterResource(com.riders.thelab.core.ui.R.drawable.ic_spotify),
                contentDescription = "spotify_icon"
            )
        } else {
            Image(
                modifier = modifier,
                painter = painterResource(com.riders.thelab.core.ui.R.drawable.ic_spotify_black),
                contentDescription = "spotify_icon",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }
}

@Composable
fun ACRCloudToolbar(
    theme: AppTheme,
    darkTheme: Boolean,
    tabItems: List<String>,
    currentPageIndex: Int,
    onTabItemClicked: (index: Int) -> Unit
) {
    val context = LocalContext.current
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Row(
            modifier = Modifier
                .heightIn(min = 56.dp, max = 96.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent), contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { (context.findActivity() as ACRCloudActivity).backPressed() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = "back_button"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(5f)
                    .background(Color.Transparent)
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                LabTabRow(
                    theme = theme,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    items = tabItems,
                    selectedItemIndex = currentPageIndex,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = if (!darkTheme) Color.DarkGray else MaterialTheme.colorScheme.inversePrimary,
                    backgroundColor = Color.Transparent,
                    onClick = onTabItemClicked
                )
            }
        }
    }
}

@Composable
fun ACRCloudActivityContent(
    theme: AppTheme,
    darkTheme: Boolean,
    acrUiState: ACRUiState,
    hasNetworkConnection: Boolean,
    currentPageIndex: Int,
    result: String,
    canLaunchAudioRecognition: Boolean,
    onStartRecognition: () -> Unit,
    isRecognizing: Boolean,
    items: List<MusicRecognitionModel>,
    uiEvent: (UiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val tabItems by remember { mutableStateOf(listOf("ACR", "Library")) }
    val pagerState = rememberPagerState { tabItems.size }

    var currentCapabilityChangedCount by remember { mutableIntStateOf(0) }
    //  val maxCapabilitiesCountTaken = 1

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

    fun scrollPagerToIndex(index: Int) {
        uiEvent.invoke(UiEvent.UpdateCurrentPageIndex(index))
        scope.launch { pagerState.animateScrollToPage(index) }
    }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        androidx.compose.material.Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            floatingActionButton = {
                AnimatedVisibility(visible = hasNetworkConnection) {
                    AnimatedContent(
                        targetState = isRecognizing,
                        label = "something"
                    ) { target ->
                        if (!target) {
                            androidx.compose.material.FloatingActionButton(
                                backgroundColor = if (!isRecognizing) {
                                    md_theme_light_onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.background
                                },
                                onClick = {
                                    if (0 != currentPageIndex) {
                                        scrollPagerToIndex(0)
                                    }

                                    if (!isRecognizing) {
                                        onStartRecognition()
                                    } else {
                                        Timber.e("FloatingActionButton | onClick | recognition is already running")
                                    }
                                },
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    painter = painterResource(id = com.riders.thelab.core.ui.R.drawable.ic_lab_6_lab),
                                    contentDescription = "app_icon_the_lab_logo",
                                    tint = Color.White
                                )
                            }
                        } else {
                            Box(modifier = Modifier)
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            bottomBar = {
                AnimatedVisibility(visible = hasNetworkConnection) {
                    androidx.compose.material.BottomAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(animatedHeight)
                            .background(MaterialTheme.colorScheme.background),
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        cutoutShape = CircleShape
                    ) {}
                }
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(targetState = hasNetworkConnection) { targetState ->
                    if (!targetState) {
                        NoNetworkConnection()
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            // ACR Cloud Toolbar
                            ACRCloudToolbar(
                                theme = theme,
                                darkTheme = darkTheme,
                                tabItems = tabItems,
                                currentPageIndex = currentPageIndex,
                                onTabItemClicked = { index -> scrollPagerToIndex(index) }
                            )

                            LabHorizontalViewPagerGeneric(
                                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                theme = theme,
                                pagerState = pagerState,
                                items = tabItems,
                                autoScroll = false,
                                userScrollEnabled = false,
                                onCurrentPageChanged = {}
                            ) { page: Int, _: Float ->
                                when (page) {
                                    0 -> ACRCloudMainContent(
                                        theme = theme,
                                        darkTheme = darkTheme,
                                        contentPadding = contentPadding,
                                        acrUiState = acrUiState,
                                        result,
                                        canLaunchAudioRecognition,
                                        onStartRecognition,
                                        isRecognizing,
                                        uiEvent = uiEvent
                                    )

                                    1 -> ACRCloudLibraryContent(
                                        theme = theme,
                                        darkTheme = darkTheme,
                                        hasInternetConnection = hasNetworkConnection,
                                        songs = items,
                                        uiEvent = uiEvent
                                    )
                                }
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
@DevicePreviewsPhoneOnly
@Composable
fun PreviewMainActivityContentWithoutInternetConnection(@PreviewParameter(PreviewProviderACRCloud::class) acrUiState: ACRUiState) {
    TheLabTheme(theme = AppTheme.Default) {
        ACRCloudActivityContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            acrUiState = acrUiState,
            hasNetworkConnection = false,
            currentPageIndex = 1,
            result = "Wheezy feat. Gunna",
            canLaunchAudioRecognition = true,
            onStartRecognition = {},
            isRecognizing = false,
            items = listOf(Song.mock.toModel(), Song.mock.toModel(), Song.mock.toModel())
        ) {}
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun PreviewMainActivityContentWithInternetConnection(@PreviewParameter(PreviewProviderACRCloud::class) acrUiState: ACRUiState) {
    TheLabTheme(theme = AppTheme.Default) {
        ACRCloudActivityContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            acrUiState = acrUiState,
            hasNetworkConnection = true,
            currentPageIndex = 0,
            result = "Wheezy feat. Gunna",
            canLaunchAudioRecognition = true,
            onStartRecognition = {},
            isRecognizing = false,
            items = listOf(Song.mock.toModel())
        ) {}
    }
}