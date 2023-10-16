package com.riders.thelab.core.data.local.model.compose

import com.riders.thelab.core.data.local.model.Song

sealed class ACRUiState {
    data class ProcessRecognition(val running: Boolean = true) : ACRUiState()
    data class RecognitionSuccessful(val songFetched: Song) : ACRUiState()
    data class RecognitionError(
        val message: String,
        val exception: Exception? = null
    ) : ACRUiState()

    data object Idle : ACRUiState()
    data class Error(
        val message: String,
        val exception: Exception? = null
    ) : ACRUiState()
}
