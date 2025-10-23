package com.riders.thelab.feature.songplayer.data

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.music.SongModel

@Stable
sealed class CardPlayerState {
    @Stable
    data object Idle : CardPlayerState()

    @Stable
    data class Visible(val songModel: SongModel) : CardPlayerState()

    @Stable
    data object Hidden : CardPlayerState()
}
