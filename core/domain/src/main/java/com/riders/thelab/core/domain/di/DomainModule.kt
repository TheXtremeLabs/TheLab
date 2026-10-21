package com.riders.thelab.core.domain.di

import android.content.Context
import com.riders.thelab.core.domain.repository.IArtistsRepository
import com.riders.thelab.core.domain.repository.IFirebaseRepository
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.domain.usecase.GetStorageReferenceUseCase
import com.riders.thelab.core.domain.usecase.artists.FetchArtistsThumbnailsUseCase
import com.riders.thelab.core.domain.usecase.artists.FetchArtistsUseCase
import com.riders.thelab.core.domain.usecase.artists.GetArtistsUseCase
import com.riders.thelab.core.domain.usecase.artists.GetThumbnailsUseCase
import com.riders.thelab.core.domain.usecase.artists.SaveArtistsUseCase
import com.riders.thelab.core.domain.usecase.weather.DownloadWeatherDataUseCase
import com.riders.thelab.core.domain.usecase.weather.GetCitiesUseCase
import com.riders.thelab.core.domain.usecase.weather.GetCurrentWeatherUseCase
import com.riders.thelab.core.domain.usecase.weather.InsertWeatherDataUseCase
import com.riders.thelab.core.domain.usecase.weather.SearchCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    //////////////////////////////////////////////////////////////
    // --- Artist
    //////////////////////////////////////////////////////////////
    @Provides
    @Singleton
    fun provideFetchArtistsUseCase(
        storageReferenceUseCase: GetStorageReferenceUseCase,
        artistRepository: IArtistsRepository
    ): FetchArtistsUseCase = FetchArtistsUseCase(
        getStorageReferenceUseCase = storageReferenceUseCase,
        repository = artistRepository
    )

    @Provides
    @Singleton
    fun provideFetchArtistsThumbnailsUseCase(
        storageReferenceUseCase: GetStorageReferenceUseCase
    ): FetchArtistsThumbnailsUseCase = FetchArtistsThumbnailsUseCase(
        storageReferenceUseCase = storageReferenceUseCase
    )

    @Provides
    @Singleton
    fun provideGetArtistsUseCase(
        artistRepository: IArtistsRepository
    ): GetArtistsUseCase = GetArtistsUseCase(artistRepository)

    @Provides
    @Singleton
    fun provideGetThumbnailsUseCase(
        artistRepository: IArtistsRepository
    ): GetThumbnailsUseCase = GetThumbnailsUseCase()

    @Provides
    @Singleton
    fun provideSaveArtistsUseCase(
        artistRepository: IArtistsRepository
    ): SaveArtistsUseCase = SaveArtistsUseCase(artistRepository)

    //////////////////////////////////////////////////////////////
    // --- Firebase
    //////////////////////////////////////////////////////////////
    @Provides
    @Singleton
    fun provideGetStorageReferenceUseCase(
        firebaseRepository: IFirebaseRepository
    ): GetStorageReferenceUseCase = GetStorageReferenceUseCase(
        firebaseRepository = firebaseRepository
    )


    //////////////////////////////////////////////////////////////
    // --- Weather
    //////////////////////////////////////////////////////////////
    @Provides
    @Singleton
    fun provideDownloadWeatherDataUseCase(
        @ApplicationContext context: Context,
        repository: IWeatherRepository
    ): DownloadWeatherDataUseCase = DownloadWeatherDataUseCase(context, repository)

    @Provides
    @Singleton
    fun provideGetCitiesUseCase(
        repository: IWeatherRepository
    ): GetCitiesUseCase = GetCitiesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCurrentWeatherUseCase(
        repository: IWeatherRepository
    ): GetCurrentWeatherUseCase = GetCurrentWeatherUseCase(repository)

    @Provides
    @Singleton
    fun provideInsertWeatherDataUseCase(
        repository: IWeatherRepository
    ): InsertWeatherDataUseCase = InsertWeatherDataUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchCityUseCase(
        repository: IWeatherRepository
    ): SearchCityUseCase = SearchCityUseCase(repository)
}