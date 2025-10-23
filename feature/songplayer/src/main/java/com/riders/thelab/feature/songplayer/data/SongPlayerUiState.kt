package com.riders.thelab.feature.songplayer.data

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.music.SongModel

@Stable
sealed class SongPlayerUiState {
    @Stable
    data object Loading : SongPlayerUiState()

    @Stable
    data class Loaded(val songs: List<SongModel>) : SongPlayerUiState()

    @Stable
    data object Empty : SongPlayerUiState()

    @Stable
    data object Error : SongPlayerUiState()
}
