package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.compose.ACRUiState
import com.riders.thelab.core.data.local.model.music.toModel

class PreviewProviderACRCloud : PreviewParameterProvider<ACRUiState> {
    override val values: Sequence<ACRUiState>
        get() = sequenceOf(
            ACRUiState.Idle,
            ACRUiState.RecognitionSuccessful(songModel = Song.mock.toModel()),
            ACRUiState.Error("Error message due to xyz")
        )
}