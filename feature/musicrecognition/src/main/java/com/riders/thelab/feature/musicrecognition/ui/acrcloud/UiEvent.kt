package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import com.riders.thelab.core.data.local.model.Song

sealed interface UiEvent {

    data class OpenInSpotify(val song: Song) : UiEvent
}