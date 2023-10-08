package com.riders.thelab.core.common.network

import android.net.Network
import android.net.NetworkCapabilities

sealed class NetworkConnectionState {

    data object NONE : NetworkConnectionState()
    data object Unavailable : NetworkConnectionState()
    data class Lost(val network: Network) : NetworkConnectionState()
    data class OnLosing(val network: Network, val maxMsToLive: Int) : NetworkConnectionState()
    data class OnCapabilitiesChanged(
        val network: Network,
        val networkCapabilities: NetworkCapabilities
    ) : NetworkConnectionState()

    data class Connected(val connectionModel: ConnectionModel, val network: Network) :
        NetworkConnectionState()
}