package com.riders.thelab.ui.recycler

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.ArtistsUiState
import com.riders.thelab.core.data.local.model.music.ArtistModel
import kotools.types.text.toNotBlankString

class PreviewProviderArtistUiState : PreviewParameterProvider<ArtistsUiState> {
    override val values: Sequence<ArtistsUiState>
        get() = sequenceOf(
            ArtistsUiState.Loading,
            ArtistsUiState.Success(
                listOf(
                    ArtistModel(
                        1,
                        "Pi'erre".toNotBlankString().getOrThrow().toString(),
                        "Pi'erre".toNotBlankString().getOrThrow().toString(),
                        "",
                        "Bourne".toNotBlankString().getOrThrow().toString(),
                        "12/06/1990".toNotBlankString().getOrThrow().toString(),
                        "Oregon".toNotBlankString().getOrThrow().toString(),
                        "",
                        "",
                        "http://pierrethumb.com".toNotBlankString().getOrThrow().toString(),
                        ""
                    )
                )
            ),
            ArtistsUiState.Error(),
            ArtistsUiState.None
        )
}

class PreviewProviderArtistModel : PreviewParameterProvider<ArtistModel> {
    override val values: Sequence<ArtistModel>
        get() = sequenceOf(
            ArtistModel(
                1,
                "Pi'erre".toNotBlankString().getOrThrow().toString(),
                "Pi'erre".toNotBlankString().getOrThrow().toString(),
                "",
                "Bourne".toNotBlankString().getOrThrow().toString(),
                "12/06/1990".toNotBlankString().getOrThrow().toString(),
                "Oregon".toNotBlankString().getOrThrow().toString(),
                "",
                "",
                "http://pierrethumb.com".toNotBlankString().getOrThrow().toString(),
                ""
            )
        )
}