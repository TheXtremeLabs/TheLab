package com.riders.thelab.feature.palette

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.palette.PaletteUiState


class PreviewProvider : PreviewParameterProvider<PaletteUiState> {
    override val values: Sequence<PaletteUiState>
        get() = sequenceOf(
            PaletteUiState.Success(""),
            PaletteUiState.Error(""),
            PaletteUiState.Loading
        )
}
