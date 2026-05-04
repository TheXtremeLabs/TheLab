package com.riders.thelab.data.di

import android.content.Context
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.RepositoryImpl
import com.riders.thelab.core.data.local.DbImpl
import com.riders.thelab.core.data.local.IDb
import com.riders.thelab.core.data.local.dao.ArtistDao
import com.riders.thelab.core.data.local.dao.ContactDao
import com.riders.thelab.core.data.local.dao.MusicRecognitionDao
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.dao.WeatherDao
import com.riders.thelab.core.data.preferences.PreferencesImpl
import com.riders.thelab.core.data.remote.ApiImpl
import com.riders.thelab.core.data.remote.IApi
import com.riders.thelab.core.data.remote.api.ArtistsAPIService
import com.riders.thelab.core.data.remote.api.FlightApiService
import com.riders.thelab.core.data.remote.api.GoogleAPIService
import com.riders.thelab.core.data.remote.api.SpotifyAPIService
import com.riders.thelab.core.data.remote.api.SpotifyAccountAPIService
import com.riders.thelab.core.data.remote.api.TMDBApiService
import com.riders.thelab.core.data.remote.api.TheLabBackApiService
import com.riders.thelab.core.data.remote.api.WeatherApiService
import com.riders.thelab.core.data.remote.api.WeatherBulkApiService
import com.riders.thelab.core.data.remote.api.WikimediaApiService
import com.riders.thelab.core.data.remote.api.YoutubeApiService
import com.riders.thelab.core.data.repository.WeatherRepository
import com.riders.thelab.core.domain.repository.IWeatherRepository
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
    fun provideDb(
        userDao: UserDao,
        artistDao: ArtistDao,
        contactDao: ContactDao,
        musicRecognitionDao: MusicRecognitionDao,
        weatherDao: WeatherDao
    ): IDb = DbImpl(
        userDao = userDao,
        artistDao = artistDao,
        contactDao = contactDao,
        musicRecognitionDao = musicRecognitionDao,
        weatherDao = weatherDao
    )

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
    fun provideApi(
        artistsAPIService: ArtistsAPIService,
        googleAPIService: GoogleAPIService,
        youtubeApiService: YoutubeApiService,
        weatherApiService: WeatherApiService,
        weatherBulkApiService: WeatherBulkApiService,
        theLabBackApiService: TheLabBackApiService,
        spotifyAccountAPIService: SpotifyAccountAPIService,
        spotifyAPIService: SpotifyAPIService,
        tmdbApiService: TMDBApiService,
        flightApiService: FlightApiService,
        wikimediaApiService: WikimediaApiService
    ): IApi = ApiImpl(
        artistsAPIService = artistsAPIService,
        googleAPIService = googleAPIService,
        youtubeApiService = youtubeApiService,
        weatherApiService = weatherApiService,
        weatherBulkApiService = weatherBulkApiService,
        theLabBackApiService = theLabBackApiService,
        spotifyAccountApiService = spotifyAccountAPIService,
        spotifyApiService = spotifyAPIService,
        tmdbApiService = tmdbApiService,
        flightApiService = flightApiService,
        wikimediaService = wikimediaApiService
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


    //////////////////////////////////////////////////////////////
    // --- Weather
    //////////////////////////////////////////////////////////////
    @Provides
    @Singleton
    fun provideWeatherRepository(
        db: IDb,
        api: IApi
    ): IWeatherRepository = WeatherRepository(db = db, api = api)

}