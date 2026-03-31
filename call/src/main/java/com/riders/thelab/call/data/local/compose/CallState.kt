package com.riders.thelab.call.data.local.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class CallState {
    @Stable
    @Immutable
    object Idle : CallState()

    @Stable
    @Immutable
    data class Incoming(val caller: String) : CallState()

    @Stable
    @Immutable
    data class Outgoing(val number: String) : CallState()

    @Stable
    data class Active(val number: String, val duration: Long) : CallState()

    @Stable
    @Immutable
    object OnHold : CallState()

    @Stable
    @Immutable
    object Disconnected : CallState()

    @Stable
    @Immutable
    object Keypad : CallState()
}