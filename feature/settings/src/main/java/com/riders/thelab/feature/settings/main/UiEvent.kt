package com.riders.thelab.feature.settings.main

import com.riders.thelab.core.data.local.model.DeviceInformation
import com.riders.thelab.core.data.local.model.User

sealed interface UiEvent {
    data class OnThemeSelected(val option: String) : UiEvent
    data class OnUpdateVibrationEnable(val isVibrationEnable: Boolean) : UiEvent
    data class OnUpdateActivitiesSplashScreenEnable(
        val isActivitiesSplashScreenEnable: Boolean
    ) : UiEvent

    data class OnUpdateDeviceInfo(val deviceInformation: DeviceInformation) : UiEvent
    data class OnUpdateShowMoreInfoOnDevice(val expanded: Boolean) : UiEvent
    data class OnUpdateUser(val user: User) : UiEvent
    data object OnLogoutClicked : UiEvent
}