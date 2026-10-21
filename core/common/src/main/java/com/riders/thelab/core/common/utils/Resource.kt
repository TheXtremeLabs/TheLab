package com.riders.thelab.core.common.utils

import com.riders.thelab.core.common.extensions.ErrorType
import kotools.types.text.NotBlankString


// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
sealed class Resource<out T> {

    data class Success<T>(val data: T) : Resource<T>()

    open class Error<Nothing>(
        open val message: String,
        open val throwable: Throwable? = null
    ) : Resource<Nothing>() {

        data class ErrorAsNotBlankString<Nothing>(
            val errorMessage: NotBlankString,
            override val throwable: Throwable? = null
        ) : Resource.Error<Nothing>(message = errorMessage.toString(), throwable = throwable)

        data class ErrorWithType<T>(
            val error: ErrorType
        ) : Resource.Error<T>(message = error.toString())
    }
}