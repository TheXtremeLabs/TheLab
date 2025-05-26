package com.riders.mlkitcompose.data.local.compose.translate

sealed interface TranslateDownloadModelState {
    data object Success : TranslateDownloadModelState
    data class Failed(
        val message: String,
        val throwable: Throwable? = null
    ) : TranslateDownloadModelState

    data object Idle:TranslateDownloadModelState
}