package com.riders.thelab.feature.artists

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.artists.ArtistsUiState
import com.riders.thelab.core.data.local.model.music.ArtistModel
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

class PreviewProviderArtistUiState : PreviewParameterProvider<ArtistsUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<ArtistsUiState>
        get() = sequenceOf(
            ArtistsUiState.Loading(NotBlankString.create("Loading")),
            ArtistsUiState.Success(listOf(ArtistModel.mock)),
            ArtistsUiState.Error(NotBlankString.create("Error occurred while getting value"))
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