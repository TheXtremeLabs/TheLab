package com.riders.thelab.core.common.network

sealed class NetworkState {
    data object Available : NetworkState()
    data object Losing : NetworkState()
    data object Lost : NetworkState()
    data object Unavailable : NetworkState()
    data object Undefined : NetworkState()
}