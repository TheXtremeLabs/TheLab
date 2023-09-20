package com.riders.thelab.feature.biometric.data

import android.annotation.SuppressLint
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt.CryptoObject
import com.riders.thelab.feature.biometric.core.exception.InvalidCryptoLayerException
import com.riders.thelab.feature.biometric.data.bean.BiometricAuthStatus
import com.riders.thelab.feature.biometric.data.bean.CryptoPurpose
import com.riders.thelab.feature.biometric.data.bean.KeyStatus
import com.riders.thelab.feature.biometric.data.bean.ValidationResult
import com.riders.thelab.feature.biometric.data.biometric.BiometricInfo
import com.riders.thelab.feature.biometric.data.biometric.KeyValueStorage
import com.riders.thelab.feature.biometric.utils.CryptoEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class BiometricRepositoryImpl @Inject constructor(
    private val biometricManager: BiometricManager,
    private val requiredAuthenticators: Int = BIOMETRIC_STRONG,
    private val keyValueStorage: KeyValueStorage,
    private val cryptoEngine: CryptoEngine,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IBiometric {

    private suspend fun checkInternalWithCrypto(): ValidationResult = withContext(dispatcher) {
        Timber.d("checkInternalWithCrypto()")
        val validationResult = cryptoEngine.validate()

        when (validationResult) {
            ValidationResult.KEY_PERMANENTLY_INVALIDATED,
            ValidationResult.KEY_INIT_FAIL -> {
                // Delete data immediately is a policy that we have decided to implement: you have always to
                // notify this condition to the user
                clearCryptoAndData()
            }

            else -> {}
        }
        validationResult
    }

    override suspend fun getBiometricInfo(): BiometricInfo = withContext(dispatcher) {
        Timber.d("getBiometricInfo()")
        val biometricAuthStatus = readBiometricAuthStatus()
        val cryptoValidationResult = checkInternalWithCrypto()
        val isBiometricTokenPresent = isTokenPresent()

        BiometricInfo(
            biometricTokenPresent = isBiometricTokenPresent,
            biometricAuthStatus = biometricAuthStatus,
            keyStatus = when (cryptoValidationResult) {
                ValidationResult.OK -> KeyStatus.READY
                ValidationResult.KEY_INIT_FAIL,
                ValidationResult.VALIDATION_FAILED -> KeyStatus.NOT_READY
                ValidationResult.KEY_PERMANENTLY_INVALIDATED -> KeyStatus.INVALIDATED
            }
        )
    }

    @SuppressLint("WrongConstant")
    private fun readBiometricAuthStatus() =
        when (biometricManager.canAuthenticate(requiredAuthenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthStatus.NOT_AVAILABLE
        }

    override suspend fun fetchAndStoreEncryptedToken(
        cryptoObject: CryptoObject
    ) = withContext(dispatcher) {
        validateCryptoLayer()
        // 1. fetch the token from our backend
        val token = getTokenFromBackend()
        // 2. encrypt the data using the cipher inside the cryptoObject
        val encryptedData = cryptoEngine.encrypt(token, cryptoObject)
        // 3. Store encrypted data and iv.
        storeDataAndIv(encryptedData.data, encryptedData.iv!!)
    }

    private suspend fun getTokenFromBackend(): String {
        // this is a mock generation
        val token = UUID.randomUUID().toString()
        keyValueStorage.storeValue(MOCK_TOKEN_KEY, token)
        return token
    }

    private fun storeDataAndIv(encryptedData: ByteArray, iv: ByteArray) {
        val dataBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT)
        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)
        keyValueStorage.storeValue(BIOMETRIC_TOKEN_KEY, dataBase64)
        keyValueStorage.storeValue(BIOMETRIC_IV_KEY, ivBase64)
    }

    override suspend fun decryptToken(cryptoObject: CryptoObject): String {
        validateCryptoLayer()
        // 1. read encrypted token (string base64 encoded)
        val encToken = keyValueStorage.getValue(BIOMETRIC_TOKEN_KEY)
        // 2. decode token data on byteArray
        val encTokenData = Base64.decode(encToken, Base64.DEFAULT)
        // 3. decrypt token via cryptoEngine (using cipher inside cryptoObject
        return cryptoEngine.decrypt(encTokenData, cryptoObject)
    }

    override suspend fun createCryptoObject(purpose: CryptoPurpose): CryptoObject =
        withContext(dispatcher) {
            validateCryptoLayer()

            val iv: ByteArray? = when (purpose) {
                CryptoPurpose.Decryption -> {
                    Base64.decode(
                        keyValueStorage.getValue(BIOMETRIC_IV_KEY),
                        Base64.DEFAULT
                    )
                }

                else -> null
            }
            cryptoEngine.createCryptoObject(purpose, iv)
        }

    private suspend fun isTokenPresent(): Boolean =
        keyValueStorage.contains(key = BIOMETRIC_TOKEN_KEY)
                && keyValueStorage.contains(BIOMETRIC_IV_KEY)


    override suspend fun clear() {
        keyValueStorage.clear()
    }

    /**
     * Validate the crypto layer. In case of invalid status, this method
     * throws an [InvalidCryptoLayerException]
     */
    private suspend fun validateCryptoLayer() {
        Timber.d("validateCryptoLayer() ")
        val status = checkInternalWithCrypto()
        if (status != ValidationResult.OK) {
            throw InvalidCryptoLayerException(status)
        }
    }

    private fun clearCryptoAndData() {
        cryptoEngine.clear()
        keyValueStorage.clear()
    }

    companion object {
        const val BIOMETRIC_TOKEN_KEY = "BIOMETRIC_TOKEN"
        const val BIOMETRIC_IV_KEY = "BIOMETRIC_TOKEN_IV"
        const val MOCK_TOKEN_KEY = "mockTokenForFakeAuthValidation"
    }
}