package com.riders.thelab.call.ui.call

sealed interface UiEvent {
    data class OnHold(val hold: Boolean) : UiEvent
    data class OnMute(val mute: Boolean) : UiEvent
    data class OnSpeaker(val speaker: Boolean) : UiEvent
    data class OnKeypad(val keypad: Boolean) : UiEvent

    data class OnHangUp(val phoneNumber:String,val hangUp: Boolean = true) : UiEvent
}