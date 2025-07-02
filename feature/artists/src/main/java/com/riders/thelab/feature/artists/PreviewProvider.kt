package com.riders.thelab.feature.artists

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.artists.ArtistsUiState
import com.riders.thelab.core.data.local.model.music.ArtistModel
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi

class PreviewProviderArtistUiState : PreviewParameterProvider<ArtistsUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<ArtistsUiState>
        get() = sequenceOf(
            ArtistsUiState.Loading(message = "Loading".toNotBlankString().getOrThrow()),
            ArtistsUiState.Success(listOf(ArtistModel.mock)),
            ArtistsUiState.Error(
                message = "Error occurred while getting value"
                    .toNotBlankString()
                    .getOrThrow()
            )
        )
}

class PreviewProviderArtists : PreviewParameterProvider<List<ArtistModel>> {
    override val values: Sequence<List<ArtistModel>>
        get() = sequenceOf(listOf(ArtistModel.mock))
}

class PreviewProviderArtist : PreviewParameterProvider<ArtistModel> {
    override val values: Sequence<ArtistModel>
        get() = sequenceOf(ArtistModel.mock)
}