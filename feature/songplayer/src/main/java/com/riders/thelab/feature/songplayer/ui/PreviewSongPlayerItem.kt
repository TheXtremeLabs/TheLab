package com.riders.thelab.feature.songplayer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.dynamicisland.CallWaveform
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

private val shape = RoundedCornerShape(16.dp)

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongPlayerItem(
    selectedIndex: Int,
    index: Int,
    song: SongModel,
    onSelectedIndex: (Int) -> Unit
) {
    TheLabTheme {
        Card(
            modifier = Modifier
                .heightIn(50.dp, 90.dp)
                .fillMaxWidth()
                .clip(shape)
                .selectable(
                    selected = song.id == selectedIndex,
                    onClick = {
                        song.isPlaying = song.id == selectedIndex
                        onSelectedIndex(song.id)
                    }
                )
                .border(
                    width = if (song.isPlaying) 4.dp else 0.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = shape
                ),
            shape = shape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(3f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text(modifier = Modifier.basicMarquee(), text = song.name, maxLines = 1)
                    Text(modifier = Modifier.basicMarquee(), text = song.path, maxLines = 1)
                }

                AnimatedVisibility(
                    modifier = Modifier.weight(.5f),
                    visible = if (LocalInspectionMode.current) true else song.isPlaying
                ) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CallWaveform()
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
private fun PreviewSongPlayerItem(@PreviewParameter(PreviewProviderSong::class) song: SongModel) {
    TheLabTheme {
        SongPlayerItem(2, 2, song) {}
    }
}