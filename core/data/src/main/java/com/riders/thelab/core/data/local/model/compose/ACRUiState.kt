package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.music.MusicRecognitionModel

@Stable
sealed class ACRUiState {
    @Stable
    data class ProcessRecognition(val running: Boolean = true) : ACRUiState()

    @Stable
    data class RecognitionSuccessful(val songModel: MusicRecognitionModel) : ACRUiState()

    @Stable
    data class RecognitionError(
        val message: String,
        val exception: Exception? = null
    ) : ACRUiState()

    @Stable
    data object Idle : ACRUiState()

    @Stable
    data class Error(
        val message: String,
        val exception: Exception? = null
    ) : ACRUiState()
}
