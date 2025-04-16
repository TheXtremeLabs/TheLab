package com.riders.thelab.core.data.utils

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