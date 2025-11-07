package com.riders.thelab.feature.songplayer.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.NoItemFound
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.songplayer.data.CardPlayerState
import com.riders.thelab.feature.songplayer.data.SongPlayerUiState


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun SongPlayerContent(
    theme: AppTheme,
    darkTheme: Boolean,
    songPlayerUiState: SongPlayerUiState,
    cardPlayerState: CardPlayerState,
    currentSongIndex: Int,
    isSongPlaying: Boolean,
    isCardExpanded: Boolean,
    songProgress: Float,
    uiEvent: (UiEvent) -> Unit
) {
    val density = LocalDensity.current
    val darkModeForced = true
    val lazyListState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    title = stringResource(id = R.string.activity_title_music),
                    isDarkThemeForced = darkModeForced,
                    navigationIcon = {}
                )
            }
        ) { contentPadding ->
            AnimatedContent(
                targetState = songPlayerUiState,
                label = ""
            ) { targetState ->

                when (targetState) {
                    is SongPlayerUiState.Loading -> {
                        NoItemFound(
                            theme = theme,
                            darkTheme = darkTheme,
                            message = "Loading..."
                        )
                    }

                    is SongPlayerUiState.Empty -> {
                        NoItemFound(
                            theme = theme,
                            darkTheme = darkTheme,
                            message = "No song item found"
                        )
                    }

                    is SongPlayerUiState.Loaded -> {
                        val animatedBottomPadding = animateDpAsState(
                            targetValue = if (currentSongIndex == -1) 0.dp else 120.dp,
                            label = "bottom animation"
                        ).value

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(contentPadding),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = animatedBottomPadding
                                    )
                                    .zIndex(1f),
                                state = lazyListState,
                                userScrollEnabled = !isCardExpanded,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(items = targetState.songs) { index: Int, item: SongModel ->
                                    SongPlayerItem(
                                        theme = theme, darkTheme = darkTheme,
                                        selectedIndex = currentSongIndex,
                                        index = index,
                                        song = item
                                    ) { uiEvent.invoke(UiEvent.OnSongItemClicked(it)) }
                                }
                            }

                            AnimatedContent(
                                modifier = Modifier.zIndex(5f),
                                targetState = cardPlayerState,
                                transitionSpec = {
                                    slideInVertically {
                                        // Slide in from 40 dp from the top.
                                        with(density) { 40.dp.roundToPx() }
                                    } + fadeIn(
                                        // Fade in with the initial alpha of 0.3f.
                                        initialAlpha = 0.3f
                                    ) togetherWith slideOutVertically {
                                        // Slide in from 40 dp from the top.
                                        with(density) { -40.dp.roundToPx() }
                                    } + fadeOut()
                                }
                            ) { targetState: CardPlayerState ->
                                when (targetState) {
                                    is CardPlayerState.Idle -> {}
                                    is CardPlayerState.Hidden -> {}
                                    is CardPlayerState.Visible -> {
                                        CardPlayer(
                                            song = targetState.songModel,
                                            songProgress = songProgress,
                                            isSongPlaying = isSongPlaying,
                                            isCardExpanded = isCardExpanded,
                                            uiEvent = uiEvent
                                        )
                                    }
                                }
                            }

                            /*AnimatedVisibility(
                                modifier = Modifier.zIndex(5f),
                                visible = currentSongIndex != -1,
                                enter = slideInVertically {
                                    // Slide in from 40 dp from the top.
                                    with(density) { 40.dp.roundToPx() }
                                } + fadeIn(
                                    // Fade in with the initial alpha of 0.3f.
                                    initialAlpha = 0.3f
                                ),
                                exit = slideOutVertically {
                                    // Slide in from 40 dp from the top.
                                    with(density) { -40.dp.roundToPx() }
                                } + fadeOut()
                            ) {
                                CardPlayer(
                                    song = targetState.songs[currentSongIndex],
                                    songProgress = songProgress,
                                    isCardExpanded = isCardExpanded,
                                    onCardViewClicked = { expanded -> onCardViewClicked(expanded) },
                                    onPreviousClicked = { previousClicked ->
                                        onPreviousClicked(previousClicked)
                                    },
                                    onPlayPauseClicked = { playPauseClicked ->
                                        onPlayPauseClicked(playPauseClicked)
                                    },
                                    onNextClicked = { nextClicked -> onNextClicked(nextClicked) }
                                )
                            }*/
                        }
                    }

                    is SongPlayerUiState.Error -> {

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
private fun PreviewSongPlayerContentEmpty(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        SongPlayerContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            songPlayerUiState = SongPlayerUiState.Loaded(mutableListOf()),
            cardPlayerState = CardPlayerState.Hidden,
            currentSongIndex = -1,
            isSongPlaying = false,
            isCardExpanded = false,
            songProgress = .4f
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewSongPlayerContentIdle(@PreviewParameter(PreviewProviderSongList::class) songs: List<SongModel>) {
    TheLabTheme(theme = AppTheme.Default) {
        SongPlayerContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            songPlayerUiState = SongPlayerUiState.Loaded(songs.toMutableList()),
            cardPlayerState = CardPlayerState.Hidden,
            currentSongIndex = -1,
            isSongPlaying = false,
            isCardExpanded = false,
            songProgress = .4f
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewSongPlayerContentPlaying(@PreviewParameter(PreviewProviderSongList::class) songs: List<SongModel>) {
    TheLabTheme(theme = AppTheme.Default) {
        SongPlayerContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            songPlayerUiState = SongPlayerUiState.Loaded(songs.toMutableList()),
            cardPlayerState = CardPlayerState.Visible(songs[2]),
            currentSongIndex = 2,
            isSongPlaying = true,
            isCardExpanded = false,
            songProgress = .3f
        ) {}
    }
}