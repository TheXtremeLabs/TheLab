package com.riders.thelab.feature.splashscreen

sealed interface UiEvent {
    data class OnUpdateSwitchContent(val isSwitchContent: Boolean) : UiEvent
    data class OnUpdateStartCountDown(val activity: SplashScreenActivity) : UiEvent
}