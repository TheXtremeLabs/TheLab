package com.riders.thelab.core.data.di

import android.content.Context
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.RepositoryImpl
import com.riders.thelab.core.data.local.DbImpl
import com.riders.thelab.core.data.local.LabDatabase
import com.riders.thelab.core.data.local.dao.ArtistDao
import com.riders.thelab.core.data.local.dao.ContactDao
import com.riders.thelab.core.data.local.dao.MusicRecognitionDao
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.dao.WeatherDao
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

    @Singleton
    @Provides
    fun provideDbHelper(
        userDao: UserDao,
        artistDao: ArtistDao,
        contactDao: ContactDao,
        musicRecognitionDao: MusicRecognitionDao,
        weatherDao: WeatherDao
    ) = DbImpl(
        userDao = userDao,
        artistDao = artistDao,
        contactDao = contactDao,
        musicRecognitionDao = musicRecognitionDao,
        weatherDao = weatherDao
    )

    @Singleton
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

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext appContext: Context) =
        PreferencesImpl(appContext)

    @Singleton
    @Provides
//    @ViewModelScoped // this is new
    fun provideRepository(dbImpl: DbImpl, apiImpl: ApiImpl, preferencesImpl: PreferencesImpl) =
        RepositoryImpl(dbImpl, apiImpl, preferencesImpl) as IRepository
}