package com.riders.thelab.feature.biometric.data

import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class BiometricPromptContainerState {
    private lateinit var _cryptoObject: BiometricPrompt.CryptoObject
    private lateinit var _promptInfo: BiometricPrompt.PromptInfo

    val promptInfo: BiometricPrompt.PromptInfo by lazy { _promptInfo }
    val cryptoObject: BiometricPrompt.CryptoObject by lazy {
        _cryptoObject
    }

    private val _isPromptToShow = mutableStateOf(false)
    val isPromptToShow: State<Boolean> = _isPromptToShow

    fun authenticate(
        promptInfo: BiometricPrompt.PromptInfo,
        cryptoObject: BiometricPrompt.CryptoObject
    ) {
        _promptInfo = promptInfo
        _cryptoObject = cryptoObject
        _isPromptToShow.value = true
    }

    fun resetShowFlag() {
        _isPromptToShow.value = false
    }
}

@Composable
fun rememberPromptContainerState(): BiometricPromptContainerState =
    remember { BiometricPromptContainerState() }