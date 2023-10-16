package com.riders.thelab.feature.biometric.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.biometric.BiometricManager
import com.riders.thelab.feature.biometric.data.BiometricRepositoryImpl
import com.riders.thelab.feature.biometric.data.IBiometric
import com.riders.thelab.feature.biometric.data.IUser
import com.riders.thelab.feature.biometric.data.UserRepositoryImpl
import com.riders.thelab.feature.biometric.data.biometric.KeyValueStorage
import com.riders.thelab.feature.biometric.utils.CryptoEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@SuppressLint("InlinedApi")
@Module
@InstallIn(SingletonComponent::class)
object BiometricModule {

    @Provides
    @Singleton
    fun provideRequiredAuthenticators(): Int =
        BiometricManager.Authenticators.BIOMETRIC_STRONG
    @Provides
    @Singleton
    fun provideKeyValueStorage(@ApplicationContext context: Context): KeyValueStorage {
        val preferences = context.getSharedPreferences("simpleStorage", Context.MODE_PRIVATE)
        return KeyValueStorage(
            sharedPreferences = preferences
        )
    }

    @Provides
    fun provideBiometricManager(@ApplicationContext context: Context): BiometricManager =
        BiometricManager.from(context)


    @Provides
    @Singleton
    fun provideCryptoEngine(): CryptoEngine = CryptoEngine()


    @Provides
    @Singleton
    fun provideUserRepository(keyValueStorage: KeyValueStorage) =
        UserRepositoryImpl(keyValueStorage) as IUser

    @Provides
    @Singleton
    fun provideTokenRepository(
        biometricManager: BiometricManager,
        keyValueStorage: KeyValueStorage,
        cryptoEngine: CryptoEngine
    ) = BiometricRepositoryImpl(
        biometricManager = biometricManager,
        keyValueStorage = keyValueStorage,
        cryptoEngine = cryptoEngine
    ) as IBiometric
}