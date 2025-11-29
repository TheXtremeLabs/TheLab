package com.riders.thelab.feature.nfc

sealed interface UiEvent {

    data object StartNFCScanning : UiEvent

    data object StopNFCScanning : UiEvent

    data object OpenSettings : UiEvent
}