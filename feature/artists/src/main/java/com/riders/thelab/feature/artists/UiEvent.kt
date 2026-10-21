package com.riders.thelab.feature.artists

import com.riders.thelab.feature.artists.data.local.model.ArtistUi

sealed interface UiEvent {

    data class OnArtistClicked(val artist: ArtistUi) : UiEvent

    data class OnUpdateArtistWithImage(val artist: ArtistUi) : UiEvent
}