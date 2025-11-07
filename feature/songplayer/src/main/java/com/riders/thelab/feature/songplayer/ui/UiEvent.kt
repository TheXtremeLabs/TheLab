package com.riders.thelab.feature.songplayer.ui

sealed interface UiEvent {
    data class OnSongItemClicked(val songId: Int) : UiEvent
    data class OnPlayerCardClicked(val expanded: Boolean) : UiEvent

    data object OnNextClicked : UiEvent
    data object OnPlayPauseClicked : UiEvent
    data object OnPreviousClicked : UiEvent
}