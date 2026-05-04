package com.riders.thelab.core.domain.di

import android.content.Context
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.domain.usecase.weather.DownloadWeatherDataUseCase
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