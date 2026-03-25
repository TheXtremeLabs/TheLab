package com.riders.thelab.feature.splashscreen

import androidx.compose.runtime.Stable

@Stable
sealed interface UiEvent {
    @Stable
    data class OnUpdateSwitchContent(val isSwitchContent: Boolean) : UiEvent

    @Stable
    data object OnUpdateStartCountDown : UiEvent
}