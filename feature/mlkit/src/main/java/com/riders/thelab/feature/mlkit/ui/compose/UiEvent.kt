package com.riders.thelab.feature.mlkit.ui.compose

sealed interface UiEvent {
    data object OnBarcodeScannerClicked : UiEvent
    data object OnDocumentScannerClicked : UiEvent
    data object OnInkRecognitionClicked : UiEvent
    data object OnFaceDetectionClicked : UiEvent
    data object OnTextRecognitionClicked : UiEvent
    data object OnTranslateClicked : UiEvent
    data object None : UiEvent
}