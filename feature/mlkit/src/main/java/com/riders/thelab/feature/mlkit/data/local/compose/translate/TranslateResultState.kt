package com.riders.thelab.feature.mlkit.data.local.compose.translate

sealed interface TranslateResultState {
    data class Translated(val translation: String) : TranslateResultState
    data class Error(val reason: String, val throwable: Throwable? = null) : TranslateResultState
    data object Loading : TranslateResultState
    data object Idle : TranslateResultState
}