package com.riders.thelab.core.ui.compose.component.network

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.common.network.NetworkState

class PreviewProviderNetworkState : PreviewParameterProvider<NetworkState> {
    override val values: Sequence<NetworkState>
        get() = sequenceOf(
            NetworkState.Available,
            NetworkState.Losing,
            NetworkState.Lost,
            NetworkState.Unavailable,
            NetworkState.Undefined
        )
}