package com.riders.thelab.core.common.extensions

sealed class ErrorCodes {
    sealed class Http {
        companion object {
            val ServiceUnavailable: Int get() = 401
            val ResourceNotFound: Int get() = 404
            val InternalServer: Int get() = 500
        }
    }
}