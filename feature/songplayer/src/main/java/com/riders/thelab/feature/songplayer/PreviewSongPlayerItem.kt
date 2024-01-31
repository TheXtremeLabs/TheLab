package com.riders.thelab.feature.songplayer

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.music.SongModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.dynamicisland.CallWaveform
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongPlayerItem(index: Int, item: SongModel) {

    var selectedIndex by remember { mutableIntStateOf(-1) }

    val modifier = Modifier.apply {
        this.heightIn(50.dp, 90.dp)
        this.fillMaxWidth()
        this.selectable(
            selected = item.id == selectedIndex,
            onClick = {
                selectedIndex = if (selectedIndex != item.id) item.id else -1
            }
        )
        if (selectedIndex == index) {
            this.border(width = 2.dp, color = MaterialTheme.colorScheme.secondaryContainer)
        }
    }

    TheLabTheme {
        Card(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(3f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text(text = item.name)
                    Text(modifier = Modifier.basicMarquee(), text = item.path)
                }

                AnimatedVisibility(visible = item.isPlaying) {
                    Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.Center) {
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
private fun PreviewSongPlayerItem() {
    TheLabTheme {
        SongPlayerItem(
            2,
            SongModel(
                0,
                "Danger",
                "/somepath/subdirectory/Danger.mp3",
                "/somepath/subdirectory/drawableUri",
                false
            )
        )
    }
}