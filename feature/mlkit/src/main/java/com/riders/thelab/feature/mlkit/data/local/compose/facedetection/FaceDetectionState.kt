package com.riders.thelab.feature.mlkit.data.local.compose.facedetection

sealed interface FaceDetectionState {
    data class Recognized(val face: String) : FaceDetectionState
    data class Error(val reason: String, val throwable: Throwable? = null) : FaceDetectionState
    data object Loading : FaceDetectionState
    data object Idle : FaceDetectionState
}