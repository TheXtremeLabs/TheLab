package com.riders.thelab.feature.mlkit.ui.compose.textrecognition

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.feature.mlkit.data.local.compose.textrecognition.TextRecognitionState

class PreviewProvider : PreviewParameterProvider<TextRecognitionState> {
    override val values: Sequence<TextRecognitionState>
        get() = sequenceOf(
            TextRecognitionState.Idle,
            TextRecognitionState.Loading,
            TextRecognitionState.Error("Error")
        )
}