package com.riders.thelab.core.nfc

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
sealed class NFCUiState {
    @Stable
    @Immutable
    data object Enabled : NFCUiState()

    @Stable
    @Immutable
    data object Disabled : NFCUiState()

    @Stable
    @Immutable
    data object NotSupported : NFCUiState()

    @Stable
    @Immutable
    data object Idle : NFCUiState()
}