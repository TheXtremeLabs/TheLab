package com.riders.thelab.core.data.utils

import com.riders.thelab.core.data.utils.ErrorType

// Source : https://levelup.gitconnected.com/error-handling-in-clean-architecture-using-flow-and-jetpack-compose-b39c729a68eb
data class DatabaseError(val code: Int) {}

fun DatabaseError.toErrorType() = when (this.code) {
    /*ErrorCodes.Http.ResourceNotFound*/ 404 -> ErrorType.Api.NotFound
    /*ErrorCodes.Http.InternalServer*/ 500 -> ErrorType.Api.Server
    /*ErrorCodes.Http.ServiceUnavailable */ 401 -> ErrorType.Api.ServiceUnavailable
    else -> ErrorType.Unknown
}