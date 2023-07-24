package com.riders.thelab.di

import android.content.Context
import androidx.biometric.BiometricManager
import com.riders.thelab.data.BiometricRepositoryImpl
import com.riders.thelab.data.IBiometric
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.IUser
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.UserRepositoryImpl
import com.riders.thelab.data.local.DbImpl
import com.riders.thelab.data.local.LabDatabase
import com.riders.thelab.data.local.model.biometric.KeyValueStorage
import com.riders.thelab.data.preferences.PreferencesImpl
import com.riders.thelab.data.remote.ApiImpl
import com.riders.thelab.ui.biometric.CryptoEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
//@InstallIn(ViewModelComponent::class) // this is new
@InstallIn(SingletonComponent::class)
object AppHelperModule {

    @Provides
    fun provideDbHelper(appDatabase: LabDatabase) =
        DbImpl(
            appDatabase.getContactDao(),
            appDatabase.getWeatherDao()
        )

    @Provides
    fun provideApiHelper() =
        ApiImpl(
            ApiModule.provideArtistsAPIService(),
            ApiModule.provideGoogleAPIService(),
            ApiModule.provideYoutubeApiService(),
            ApiModule.provideWeatherApiService(),
            ApiModule.proWeatherBulkApiService(),
            ApiModule.provideUserAPIService()
        )

    @Provides
    fun providePreferences(@ApplicationContext appContext: Context) =
        PreferencesImpl(appContext)

    @Provides
//    @ViewModelScoped // this is new
    @Singleton
    fun provideRepository(dbImpl: DbImpl, apiImpl: ApiImpl, preferencesImpl: PreferencesImpl) =
        RepositoryImpl(dbImpl, apiImpl, preferencesImpl) as IRepository


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