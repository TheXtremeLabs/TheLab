package com.riders.thelab.feature.artists

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.feature.artists.data.local.compose.ArtistUiState
import com.riders.thelab.feature.artists.data.local.model.ArtistUi
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi

private val mock = ArtistUi(
    artistName = "Pi'erre".toNotBlankString().getOrThrow().toString(),
    firstName = "Pi'erre".toNotBlankString().getOrThrow().toString(),
    secondName = "",
    lastName = "Bourne".toNotBlankString().getOrThrow().toString(),
    dateOfBirth = "12/06/1990".toNotBlankString().getOrThrow().toString(),
    origin = "Oregon".toNotBlankString().getOrThrow().toString(),
    debutes = "2013",
    urlThumb = "http://pierrethumb.com".toNotBlankString().getOrThrow()
        .toString(),
    description = ""
)


class PreviewProviderArtistUiState : PreviewParameterProvider<ArtistUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<ArtistUiState>
        get() = sequenceOf(
            ArtistUiState.Loading(message = "Loading".toNotBlankString().getOrThrow()),
            ArtistUiState.Success(listOf(mock)),
            ArtistUiState.Error(
                message = "Error occurred while getting value"
                    .toNotBlankString()
                    .getOrThrow()
            )
        )
}

class PreviewProviderArtists : PreviewParameterProvider<List<ArtistUi>> {
    override val values: Sequence<List<ArtistUi>>
        get() = sequenceOf(listOf(mock))
}

class PreviewProviderArtist : PreviewParameterProvider<ArtistUi> {
    override val values: Sequence<ArtistUi>
        get() = sequenceOf(mock)
}