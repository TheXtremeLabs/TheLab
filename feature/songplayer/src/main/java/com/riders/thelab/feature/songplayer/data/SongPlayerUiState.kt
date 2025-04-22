package com.riders.thelab.feature.songplayer.data

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.music.SongModel

@Stable
sealed class SongPlayerUiState {
    data object Loading : SongPlayerUiState()
    data class Loaded(val songs: List<SongModel>) : SongPlayerUiState()
    data object Empty : SongPlayerUiState()
    data object Error : SongPlayerUiState()
}
