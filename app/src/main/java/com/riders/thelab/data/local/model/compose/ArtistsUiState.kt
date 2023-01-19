package com.riders.thelab.data.local.model.compose

import com.riders.thelab.data.remote.dto.artist.Artist

sealed class ArtistsUiState {
    data class Success(val artists: List<Artist>) : ArtistsUiState()
    data class Error(val errorResponse: Throwable? = null) : ArtistsUiState()
    object Loading : ArtistsUiState()
    object None : ArtistsUiState()
}
