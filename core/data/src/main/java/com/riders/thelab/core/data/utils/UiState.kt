package com.riders.thelab.core.data.utils

// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
sealed class UiState<T> {
    class Loading<T>: UiState<T>()
    data class Error<T>(val error: String) : UiState<T>()
    data class Loaded<T>(val data: T): UiState<T>()
}