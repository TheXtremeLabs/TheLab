package com.riders.thelab.ui.splashscreen

import androidx.compose.runtime.Stable

@Stable
sealed interface UiEvent {
    @Stable
    data class OnUpdateSwitchContent(val isSwitchContent: Boolean) : UiEvent

    @Stable
    data class OnUpdateStartCountDown(val isStarted: Boolean) : UiEvent

    @Stable
    data object OnGoToLoginActivity : UiEvent
}