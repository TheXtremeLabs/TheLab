package com.riders.thelab.core.data.local.model.compose

import com.riders.thelab.core.data.remote.dto.artist.Artist

sealed class ArtistsUiState {
    data class Success(val artists: List<Artist>) : ArtistsUiState()
    data class Error(val errorResponse: Throwable? = null) : ArtistsUiState()
    data object Loading : ArtistsUiState()
    data object None : ArtistsUiState()
}
