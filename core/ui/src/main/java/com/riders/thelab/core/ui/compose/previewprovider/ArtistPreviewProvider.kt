package com.riders.thelab.core.ui.compose.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.music.Artist
import kotools.types.text.toNotBlankString

class ArtistModelPreviewProvider : PreviewParameterProvider<Artist> {
    override val values: Sequence<Artist>
        get() = sequenceOf(
            Artist(
                "Pi'erre".toNotBlankString().getOrThrow(),
                "Pi'erre".toNotBlankString().getOrThrow(),
                "",
                "Bourne".toNotBlankString().getOrThrow(),
                "12/06/1990".toNotBlankString().getOrThrow(),
                "Oregon".toNotBlankString().getOrThrow(),
                "",
                "",
                "http://pierrethumb.com".toNotBlankString().getOrThrow(),
                ""
            )
        )
}

class ArtistPreviewProvider :
    PreviewParameterProvider<com.riders.thelab.core.data.remote.dto.artist.Artist> {
    override val values: Sequence<com.riders.thelab.core.data.remote.dto.artist.Artist>
        get() = sequenceOf(
            com.riders.thelab.core.data.remote.dto.artist.Artist(
                "Pi'erre",
                "Pi'erre",
                "",
                "Bourne",
                "12/06/1990",
                "Oregon",
                "",
                "",
                "http://pierrethumb.com",
                ""
            )
        )
}