package com.riders.thelab.feature.biometric.data.biometric

import com.riders.thelab.feature.biometric.data.bean.BiometricAuthStatus
import com.riders.thelab.feature.biometric.data.bean.KeyStatus

data class BiometricInfo(
    /**
     * True if our biometric token is present, false otherwise
     */
    val biometricTokenPresent: Boolean = false,

    /**
     * Give us information about the status of our biometric authentication
     */
    val biometricAuthStatus: BiometricAuthStatus,

    /**
     * Give us the status of our cryptographic key
     */
    val keyStatus: KeyStatus
) {
    fun canAskAuthentication() =
        (biometricAuthStatus == BiometricAuthStatus.READY && keyStatus == KeyStatus.READY)
}
