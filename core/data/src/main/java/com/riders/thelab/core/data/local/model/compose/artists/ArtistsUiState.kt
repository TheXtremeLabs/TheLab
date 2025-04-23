package com.riders.thelab.core.data.local.model.compose.artists

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.music.ArtistModel
import kotools.types.text.NotBlankString

@Stable
sealed interface ArtistsUiState {
    @Stable
    data class Success(val artists: List<ArtistModel>) : ArtistsUiState

    @Stable
    data class Error(
        val message: NotBlankString,
        val errorResponse: Throwable? = null
    ) : ArtistsUiState

    @Stable
    data class Loading(val message: NotBlankString) : ArtistsUiState
}
