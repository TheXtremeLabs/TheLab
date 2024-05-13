package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.music.ArtistModel

@Stable
sealed class ArtistsUiState {
    data class Success(val artists: List<ArtistModel>) : ArtistsUiState()
    data class Error(val errorResponse: Throwable? = null) : ArtistsUiState()
    data object Loading : ArtistsUiState()
    data object None : ArtistsUiState()
}
