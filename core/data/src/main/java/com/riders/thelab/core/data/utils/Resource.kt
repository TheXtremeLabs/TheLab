package com.riders.thelab.core.data.utils

import kotools.types.text.NotBlankString

// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
sealed class Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<Nothing>(
        val message: NotBlankString,
        val throwable: Throwable? = null
    ) : Resource<Nothing>()

    data class ErrorWithType<T>(val error: ErrorType) : Resource<T>()
}