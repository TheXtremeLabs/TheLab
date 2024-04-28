package com.riders.thelab.core.data.di

import android.content.Context
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.RepositoryImpl
import com.riders.thelab.core.data.local.DbImpl
import com.riders.thelab.core.data.local.LabDatabase
import com.riders.thelab.core.data.preferences.PreferencesImpl
import com.riders.thelab.core.data.remote.ApiImpl
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
            appDatabase.getUserDao(),
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
            ApiModule.provideUserAPIService(),
            ApiModule.provideSpotifyAccountAPIService(),
            ApiModule.provideSpotifyAPIService(),
            ApiModule.provideTMDBAPIService(),
            ApiModule.provideFlightAPIService(),
            ApiModule.provideWikimediaAPIService(),
        )

    @Provides
    fun providePreferences(@ApplicationContext appContext: Context) =
        PreferencesImpl(appContext)

    @Provides
//    @ViewModelScoped // this is new
    @Singleton
    fun provideRepository(dbImpl: DbImpl, apiImpl: ApiImpl, preferencesImpl: PreferencesImpl) =
        RepositoryImpl(dbImpl, apiImpl, preferencesImpl) as IRepository
}