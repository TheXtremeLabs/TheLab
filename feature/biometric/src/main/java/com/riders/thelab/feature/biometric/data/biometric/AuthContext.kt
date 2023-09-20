package com.riders.thelab.feature.biometric.data.biometric

import androidx.biometric.BiometricPrompt
import com.riders.thelab.feature.biometric.data.bean.CryptoPurpose

data class AuthContext(val purpose: CryptoPurpose, val cryptoObject: BiometricPrompt.CryptoObject)
