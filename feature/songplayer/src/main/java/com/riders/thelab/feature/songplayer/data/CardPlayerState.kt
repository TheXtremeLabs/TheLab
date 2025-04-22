package com.riders.thelab.feature.songplayer.data

import com.riders.thelab.core.data.local.model.music.SongModel

sealed class CardPlayerState {
    data object Idle : CardPlayerState()
    data class Visible(val songModel: SongModel) : CardPlayerState()
    data object Hidden : CardPlayerState()
}
