package com.riders.thelab.feature.biometric.core.exception

import com.riders.thelab.feature.biometric.data.bean.ValidationResult


class InvalidCryptoLayerException(private val validationResult: ValidationResult) : Exception() {

    fun isKeyPermanentlyInvalidated() =
        validationResult == ValidationResult.KEY_PERMANENTLY_INVALIDATED

    fun isKeyInitFailed() = validationResult == ValidationResult.KEY_INIT_FAIL
}