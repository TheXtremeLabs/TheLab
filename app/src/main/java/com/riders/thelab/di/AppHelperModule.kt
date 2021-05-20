package com.riders.thelab.di

import com.riders.thelab.data.IRepository
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.DbImpl
import com.riders.thelab.data.local.LabDatabase
import com.riders.thelab.data.remote.ApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class) // this is new
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
            ApiModule.proWeatherBulkApiService()
        )

    @Provides
    @ViewModelScoped // this is new
    fun provideRepository(dbImpl: DbImpl, apiImpl: ApiImpl) =
        RepositoryImpl(dbImpl, apiImpl) as IRepository
}