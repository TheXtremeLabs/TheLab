package com.riders.thelab.core.data.utils

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb

@Stable
sealed class UiState<out T> {
    @Stable
    data class Error<Nothing>(val error: String) : UiState<Nothing>()

    @Stable
    data class Success<T>(val data: T) : UiState<T>()

    @Stable
    @Immutable
    data object Loading : UiState<Nothing>()

    @Stable
    @Immutable
    data object Idle : UiState<Nothing>()
}