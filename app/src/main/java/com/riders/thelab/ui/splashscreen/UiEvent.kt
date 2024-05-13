package com.riders.thelab.ui.splashscreen

sealed interface UiEvent {
    data class OnUpdateSwitchContent(val isSwitchContent:Boolean) : UiEvent
    data class OnUpdateStartCountDown(val isStarted:Boolean) : UiEvent
}