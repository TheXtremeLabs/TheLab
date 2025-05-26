package com.riders.mlkitcompose.data.local.compose.textrecognition

import androidx.compose.runtime.Stable
import com.riders.thelab.feature.mlkit.data.local.model.TextRecognitionModel

@Stable
sealed interface TextRecognitionState {
    data class Recognized(val recognitionModel: TextRecognitionModel) : TextRecognitionState
    data class Error(val reason: String, val throwable: Throwable? = null) : TextRecognitionState
    data object Loading : TextRecognitionState
    data object Idle : TextRecognitionState
}