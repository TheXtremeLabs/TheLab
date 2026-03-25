package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.music.MusicRecognitionModel

@Stable
sealed class ACRUiState {
    @Stable
    @Immutable
    data class ProcessRecognition(val running: Boolean = true) : ACRUiState()

    @Stable
    @Immutable
    data class RecognitionSuccessful(val songModel: MusicRecognitionModel) : ACRUiState()

    @Stable
    @Immutable
    data class RecognitionError(
        val message: String,
        val exception: Exception? = null
    ) : ACRUiState()

    @Stable
    @Immutable
    data object Idle : ACRUiState()

    @Stable
    @Immutable
    data class Error(
        val message: String,
        val exception: Exception? = null
    ) : ACRUiState()
}
