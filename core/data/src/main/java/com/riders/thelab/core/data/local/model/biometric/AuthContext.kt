package com.riders.thelab.core.data.local.model.biometric

import androidx.biometric.BiometricPrompt
import com.riders.thelab.core.data.local.bean.CryptoPurpose

data class AuthContext(val purpose: CryptoPurpose, val cryptoObject: BiometricPrompt.CryptoObject) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthContext

        if (purpose != other.purpose) return false
        return cryptoObject == other.cryptoObject
    }

    override fun hashCode(): Int {
        var result = purpose.hashCode()
        result = 31 * result + cryptoObject.hashCode()
        return result
    }
}
