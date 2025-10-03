package com.riders.thelab.core.common.utils

// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
sealed class Resource<T> {

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<Nothing>(
        val message: String,
        val throwable: Throwable? = null
    ) : Resource<Nothing>()
}