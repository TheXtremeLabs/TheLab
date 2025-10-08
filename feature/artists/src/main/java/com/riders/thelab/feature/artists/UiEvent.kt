package com.riders.thelab.feature.artists

import com.riders.thelab.core.data.local.model.music.ArtistModel

sealed interface UiEvent {

    data class OnArtistClicked(val artist: ArtistModel) : UiEvent

    data class OnUpdateArtistWithImage(val artist: ArtistModel) : UiEvent
}