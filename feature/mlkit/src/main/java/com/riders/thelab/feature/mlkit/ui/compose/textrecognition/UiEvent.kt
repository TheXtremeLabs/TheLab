package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import com.google.mlkit.vision.common.InputImage
import com.riders.mlkitcompose.data.local.model.TextRecognitionModel

sealed interface UiEvent {
    data class OnTextRecognition(val model: TextRecognitionModel) : UiEvent
    data class StartTextRecognition(val inputImage: InputImage) : UiEvent

    data object OnAssetImageClicked : UiEvent
    data object OnDismissBottomSheet : UiEvent
}