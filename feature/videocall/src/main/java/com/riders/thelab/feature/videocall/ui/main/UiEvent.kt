package com.riders.thelab.feature.videocall.ui.main

sealed interface UiEvent {
    data class OnNameChanged(val name: String) : UiEvent
    data object OnConnectClicked : UiEvent
    data object OnDisconnectClicked : UiEvent
}