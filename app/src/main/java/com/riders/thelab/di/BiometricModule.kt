package com.riders.thelab.di

import android.content.Context
import androidx.biometric.BiometricManager
import com.riders.thelab.ui.biometric.CryptoEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BiometricModule {

    @Provides
    fun provideBiometricManager(@ApplicationContext context: Context): BiometricManager =
        BiometricManager.from(context)


    @Provides
    @Singleton
    fun provideCryptoEngine(): CryptoEngine = CryptoEngine()

}