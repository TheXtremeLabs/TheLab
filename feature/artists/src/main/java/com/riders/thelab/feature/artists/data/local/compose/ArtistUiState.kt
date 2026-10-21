package com.riders.thelab.feature.artists.data.local.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.feature.artists.data.local.model.ArtistUi
import kotools.types.text.NotBlankString

@Stable
@Immutable
sealed class ArtistUiState {
    @Stable
    data class Success(val artists: List<ArtistUi>) : ArtistUiState()

    @Stable
    data class Error(
        val message: NotBlankString,
        val errorResponse: Throwable? = null
    ) : ArtistUiState()

    @Stable
    data class Loading(val message: NotBlankString) : ArtistUiState()
}