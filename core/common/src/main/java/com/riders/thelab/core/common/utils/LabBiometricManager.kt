package com.riders.thelab.core.common.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators

object LabBiometricManager {
    ///////////////////////////////////////////////////////
    // Biometric
    ///////////////////////////////////////////////////////
    /**
     * This method returns if the device has fingerprint hardware or not
     *
     * @return
     */
    @Suppress("DEPRECATION")
    fun hasFingerPrintHardware(context: Context): Boolean =
        when (
            if (!LabCompatibilityManager.isAndroid10())
                BiometricManager.from(context).canAuthenticate()
            else BiometricManager.from(context).canAuthenticate(Authenticators.BIOMETRIC_WEAK)
        ) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false

            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        } && LabCompatibilityManager.isMarshmallow()
                && context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
}