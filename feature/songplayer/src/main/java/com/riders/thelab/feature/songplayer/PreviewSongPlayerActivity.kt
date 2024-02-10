package com.riders.thelab.feature.songplayer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.NoItemFound
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun SongPlayerContent(
    songList: List<SongModel>,
    currentSongIndex: Int,
    isSongPlaying: Boolean,
    isCardExpanded: Boolean,
    songProgress: Float,
    onItemClicked: (Int) -> Unit,
    onCardViewClicked: (Boolean) -> Unit,
    onPreviousClicked: (Boolean) -> Unit,
    onPlayPauseClicked: (Boolean) -> Unit,
    onNextClicked: (Boolean) -> Unit
) {
    val darkModeForced = true
    val lazyListState = rememberLazyListState()

    TheLabTheme(darkModeForced) {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    title = stringResource(id = com.riders.thelab.core.ui.R.string.activity_title_music),
                    isDarkThemeForced = darkModeForced,
                    navigationIcon = {}
                )
            }
        ) { contentPadding ->
            AnimatedContent(
                targetState = songList.isNotEmpty(),
                label = ""
            ) { targetState: Boolean ->
                if (!targetState) {
                    NoItemFound("No song item found")
                } else {
                    val animatedBottomPadding = animateDpAsState(
                        targetValue = if (!isSongPlaying) 0.dp else 120.dp,
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
                            itemsIndexed(items = songList) { index: Int, item: SongModel ->
                                SongPlayerItem(
                                    selectedIndex = currentSongIndex,
                                    index = index,
                                    song = item
                                ) {
                                    //selectedIndex = if (selectedIndex != it) it else -1
//                                    onItemClicked(selectedIndex)
                                    onItemClicked(it)
                                }
                            }
                        }

                        AnimatedVisibility(
                            modifier = Modifier.zIndex(5f),
                            visible = currentSongIndex != -1,
                            enter = slideInVertically() + fadeIn(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            CardPlayer(
                                song = songList.first { it.id == currentSongIndex },
                                songProgress = songProgress,
                                isCardExpanded = isCardExpanded,
                                onCardViewClicked = { expanded -> onCardViewClicked(expanded) },
                                onPreviousClicked = { previousClicked ->
                                    onPreviousClicked(
                                        previousClicked
                                    )
                                },
                                onPlayPauseClicked = { playPauseClicked ->
                                    onPlayPauseClicked(
                                        playPauseClicked
                                    )
                                },
                                onNextClicked = { nextClicked -> onNextClicked(nextClicked) }
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
private fun PreviewSongPlayerContentEmpty() {
    TheLabTheme(true) {
        SongPlayerContent(
            songList = emptyList(),
            currentSongIndex = -1,
            isSongPlaying = false,
            isCardExpanded = false,
            songProgress = .4f, onItemClicked = {},
            onCardViewClicked = { },
            onPreviousClicked = { },
            onPlayPauseClicked = { },
            onNextClicked = { }
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewSongPlayerContentIdle(@PreviewParameter(PreviewProviderSongList::class) songs: List<SongModel>) {
    TheLabTheme(true) {
        SongPlayerContent(
            songList = songs,
            currentSongIndex = -1,
            isSongPlaying = false,
            isCardExpanded = false,
            songProgress = .4f, onItemClicked = {},
            onCardViewClicked = { },
            onPreviousClicked = { },
            onPlayPauseClicked = { },
            onNextClicked = { }
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewSongPlayerContentPlaying(@PreviewParameter(PreviewProviderSongList::class) songs: List<SongModel>) {
    TheLabTheme(true) {
        SongPlayerContent(
            songList = songs,
            currentSongIndex = 2,
            isSongPlaying = true,
            isCardExpanded = false,
            songProgress = .3f,
            onItemClicked = {},
            onCardViewClicked = { },
            onPreviousClicked = { },
            onPlayPauseClicked = { },
            onNextClicked = { }
        )
    }
}