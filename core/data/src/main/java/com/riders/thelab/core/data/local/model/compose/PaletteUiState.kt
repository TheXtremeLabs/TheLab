package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable

@Stable
sealed class PaletteUiState {

    data class Success(val fetchedImage: String) : PaletteUiState()
    data class Error(val error: String) : PaletteUiState()
    data object Loading : PaletteUiState()
}