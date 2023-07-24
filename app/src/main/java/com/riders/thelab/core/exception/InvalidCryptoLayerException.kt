package com.riders.thelab.core.exception

import com.riders.thelab.data.local.bean.ValidationResult

class InvalidCryptoLayerException(private val validationResult: ValidationResult) : Exception() {

    fun isKeyPermanentlyInvalidated() =
        validationResult == ValidationResult.KEY_PERMANENTLY_INVALIDATED

    fun isKeyInitFailed() = validationResult == ValidationResult.KEY_INIT_FAIL
}