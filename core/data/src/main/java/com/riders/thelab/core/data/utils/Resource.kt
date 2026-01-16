package com.riders.thelab.core.data.utils

import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import retrofit2.Response

// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
sealed class Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<Nothing>(
        val message: NotBlankString,
        val throwable: Throwable? = null
    ) : Resource<Nothing>()

    data class ErrorWithType<T>(val error: ErrorType) : Resource<T>()
}

fun <T> Response<T>.toResource(): Resource<T> {
    if (!isSuccessful) {
        val errorBody = errorBody()
        if (errorBody != null) {
            return Resource.Error(errorBody.toString().toNotBlankString().getOrThrow())
        }
    }
    val body: T =
        body() ?: return Resource.Error("Response body is null".toNotBlankString().getOrThrow())
    return Resource.Success(body)

}