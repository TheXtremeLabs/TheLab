package com.riders.thelab.core.data.local.model.compose.artists

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.music.ArtistModel
import kotools.types.text.NotBlankString

@Stable
sealed interface ArtistsUiState {
    data class Success(val artists: List<ArtistModel>) : ArtistsUiState
    data class Error(
        val message: NotBlankString,
        val errorResponse: Throwable? = null
    ) : ArtistsUiState

    data class Loading(val message: NotBlankString) : ArtistsUiState
}
