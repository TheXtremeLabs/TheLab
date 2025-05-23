package com.riders.thelab.core.data.utils

import com.riders.thelab.core.data.utils.ErrorType
import retrofit2.HttpException
import java.io.IOException

// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
fun Throwable.toErrorType(): ErrorType = when (this) {
    is IOException -> ErrorType.Api.Network
    is HttpException -> when (code()) {
        ErrorCodes.Http.ResourceNotFound -> ErrorType.Api.NotFound
        ErrorCodes.Http.InternalServer -> ErrorType.Api.Server
        ErrorCodes.Http.ServiceUnavailable -> ErrorType.Api.ServiceUnavailable
        else -> ErrorType.Unknown
    }

    else -> ErrorType.Unknown
}