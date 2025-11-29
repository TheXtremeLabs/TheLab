package com.riders.thelab.feature.nfc

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.nfc.NFCUiState

class PreviewProviderNFCUiState : PreviewParameterProvider<NFCUiState> {
    override val values: Sequence<NFCUiState>
        get() = sequenceOf(
            NFCUiState.NotSupported,
            NFCUiState.Disabled,
            NFCUiState.Enabled,
            NFCUiState.Idle
        )
}