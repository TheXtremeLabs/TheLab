package com.riders.thelab.feature.bluetooth

sealed interface UiEvent {

    data class OnEnabledBluetooth(val enabled:Boolean): UiEvent

    data object OnStartDiscovery: UiEvent

    data object OnStopDiscovery: UiEvent

    data object OnRefresh: UiEvent

    data object OnFetchBoundedDevices: UiEvent

}