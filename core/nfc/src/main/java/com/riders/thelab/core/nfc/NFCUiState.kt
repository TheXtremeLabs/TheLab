package com.riders.thelab.core.nfc

import androidx.compose.runtime.Stable

@Stable
sealed class NFCUiState {
    @Stable
    data object Enabled : NFCUiState()

    @Stable
    data object Disabled : NFCUiState()

    @Stable
    data object NotSupported : NFCUiState()

    @Stable
    data object Idle : NFCUiState()
}