package com.riders.thelab.feature.biometric.data

import androidx.biometric.BiometricPrompt.CryptoObject
import com.riders.thelab.feature.biometric.data.bean.CryptoPurpose
import com.riders.thelab.feature.biometric.data.biometric.BiometricInfo

/**
 * Represent the repository for our biometric related data / info
 */
interface IBiometric {

    /**
     * Read the biometric info that contains the biometric authentication
     * state, the underling key status and a flat to inform when the token is
     * already present
     *
     * @return the biometric info object
     */
    suspend fun getBiometricInfo(): BiometricInfo

    /**
     * Store the token using the [cryptoObject] passed as parameter.
     *
     * @param cryptoObject the cryptoObject to use for encryption operations
     * @throws [com.riders.thelab.feature.biometric.core.exception.InvalidCryptoLayerException] if
     * crypto layer is invalid
     */
    suspend fun fetchAndStoreEncryptedToken(cryptoObject: CryptoObject)

    /**
     * Decrypt the token using the [cryptoObject] passed as parameter
     *
     * @param cryptoObject the cryptoObject to use for decryption operations
     * @return the token as string
     * @throws [com.riders.thelab.feature.biometric.core.exception.InvalidCryptoLayerException] if crypto layer is invalid
     */
    suspend fun decryptToken(cryptoObject: CryptoObject): String

    /**
     * Create a new [CryptoObject] instance for the specified purpose
     *
     * @param purpose the final purpose of the required cryptoObject
     * @throws [com.riders.thelab.feature.biometric.core.exception.InvalidCryptoLayerException] if crypto layer is invalid
     */
    suspend fun createCryptoObject(purpose: CryptoPurpose): CryptoObject

    /**
     * Clear the stored information
     */
    suspend fun clear()
}