package com.riders.thelab.feature.songplayer

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter

@Composable
fun CardPlayerActions(
    modifier: Modifier,
    isPlaying: Boolean,
    onPreviousClicked: (Boolean) -> Unit,
    onPlayPauseClicked: (Boolean) -> Unit,
    onNextClicked: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.Top
    ) {
        IconButton(modifier = Modifier.weight(1f), onClick = { onPreviousClicked(true) }) {
            Icon(
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = "previous icon"
            )
        }

        IconButton(modifier = Modifier.weight(1f), onClick = { onPlayPauseClicked(true) }) {
            Icon(
                imageVector = if (!isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "previous icon"
            )
        }

        IconButton(modifier = Modifier.weight(1f), onClick = { onNextClicked(true) }) {
            Icon(
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = "previous icon"
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardPlayer(
    song: SongModel,
    songProgress: Float,
    isCardExpanded: Boolean,
    onCardViewClicked: (Boolean) -> Unit,
    onPreviousClicked: (Boolean) -> Unit,
    onPlayPauseClicked: (Boolean) -> Unit,
    onNextClicked: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val orientation = LocalConfiguration.current.orientation

    Card(
        modifier = if (!isCardExpanded) Modifier
            .fillMaxWidth()
            .heightIn(50.dp, 120.dp)
            .padding(16.dp) else Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)
            .zIndex(5f)
            .shadow(
                elevation = if (song.isPlaying) 4.dp else 0.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        onClick = { onCardViewClicked(!isCardExpanded) },
        shape = if (!isCardExpanded) RoundedCornerShape(12.dp) else RoundedCornerShape(
            topStartPercent = 10,
            topEndPercent = 10,
            bottomStartPercent = 0,
            bottomEndPercent = 0
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer)
    ) {
        AnimatedContent(targetState = isCardExpanded, label = "") { targetState ->
            if (!targetState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Song Info
                        Column(
                            modifier = Modifier.weight(2f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Text(modifier = Modifier.basicMarquee(), text = song.name, maxLines = 1)
                            Text(modifier = Modifier.basicMarquee(), text = song.path, maxLines = 1)
                        }

                        // Actions
                        CardPlayerActions(
                            modifier = Modifier.weight(1f),
                            isPlaying = song.isPlaying,
                            onPreviousClicked = { onPreviousClicked(it) },
                            onPlayPauseClicked = { onPlayPauseClicked(it) },
                            onNextClicked = { onNextClicked(it) }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            progress = songProgress
                        )
                    }
                }

            } else {

                val imagePainter =
                    getCoilAsyncImagePainter(context = context, dataUrl = song.drawableUri)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Box(modifier = Modifier.weight(1.5f), contentAlignment = Alignment.Center) {
                        Image(painter = imagePainter, contentDescription = null)
                    }

                    // Song Info
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        )
                    ) {
                        Text(modifier = Modifier.basicMarquee(), text = song.name, maxLines = 1)
                        Text(modifier = Modifier.basicMarquee(), text = song.path, maxLines = 2)
                    }

                    // Actions
                    CardPlayerActions(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 72.dp, end = 72.dp, bottom = when (orientation) {
                                    Configuration.ORIENTATION_PORTRAIT -> {
                                        96.dp
                                    }

                                    Configuration.ORIENTATION_LANDSCAPE -> {
                                        56.dp
                                    }

                                    else -> {
                                        56.dp
                                    }
                                }
                            ),
                        isPlaying = song.isPlaying,
                        onPreviousClicked = { onPreviousClicked(it) },
                        onPlayPauseClicked = { onPlayPauseClicked(it) },
                        onNextClicked = { onNextClicked(it) }
                    )
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewCardPlayerCollapsed(@PreviewParameter(PreviewProviderSong::class) item: SongModel) {
    TheLabTheme {
        CardPlayer(item, .4f, false, {}, {}, {}, {})
    }
}

@DevicePreviews
@Composable
private fun PreviewCardPlayerExpanded(@PreviewParameter(PreviewProviderSong::class) item: SongModel) {
    TheLabTheme {
        CardPlayer(item, .93f, true, {}, {}, {}, {})
    }
}