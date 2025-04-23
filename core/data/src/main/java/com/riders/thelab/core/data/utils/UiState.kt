package com.riders.thelab.core.data.utils

import androidx.compose.runtime.Stable

@Stable
// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
sealed class UiState<T> {
    @Stable
    class Loading<T>: UiState<T>()
    @Stable
    data class Error<T>(val error: String) : UiState<T>()
    @Stable
    data class Loaded<T>(val data: T): UiState<T>()
}