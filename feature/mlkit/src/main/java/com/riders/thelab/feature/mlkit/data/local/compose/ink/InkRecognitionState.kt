package com.riders.mlkitcompose.data.local.compose.ink

sealed interface InkRecognitionState {
    data class Recognized(val candidate: String) : InkRecognitionState
    data class Failed(
        val message: String,
        val throwable: Throwable? = null
    ) : InkRecognitionState
}