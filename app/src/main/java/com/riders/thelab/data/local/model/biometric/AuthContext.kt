package com.riders.thelab.data.local.model.biometric

import androidx.biometric.BiometricPrompt
import com.riders.thelab.data.local.bean.CryptoPurpose

data class AuthContext(val purpose: CryptoPurpose, val cryptoObject: BiometricPrompt.CryptoObject)
