package com.riders.thelab.core.common.extensions

sealed class ErrorType {

    sealed class Api : ErrorType() {

        data object Network : Api()

        data object ServiceUnavailable : Api()

        data object NotFound : Api()

        data object Server : Api()
    }

    data object Unknown : ErrorType()


    // other categories of Error
}