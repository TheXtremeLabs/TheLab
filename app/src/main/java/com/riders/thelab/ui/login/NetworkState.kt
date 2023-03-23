package com.riders.thelab.ui.login

sealed class NetworkState {
    data class Available(val available: Boolean) : NetworkState()
    data class Disconnected(val disconnected: Boolean) : NetworkState()
}
