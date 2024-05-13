package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState

class PreviewProviderACRCloud : PreviewParameterProvider<ACRUiState> {
    override val values: Sequence<ACRUiState>
        get() = sequenceOf(
            ACRUiState.Idle,
            ACRUiState.RecognitionSuccessful(songFetched = Song.mock),
            ACRUiState.Error("Error message due to xyz")
        )
}