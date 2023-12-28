package com.riders.thelab.core.common.utils

import android.content.Context
import androidx.biometric.BiometricManager

object LabBiometricManager {
    ///////////////////////////////////////////////////////
    // Biometric
    ///////////////////////////////////////////////////////
    /**
     * This method returns if the device has fingerprint hardware or not
     *
     * @return
     */
    fun hasFingerPrintHardware(context: Context): Boolean =
        when (BiometricManager.from(context).canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false

            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
}