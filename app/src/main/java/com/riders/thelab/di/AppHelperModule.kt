package com.riders.thelab.di

import com.riders.thelab.data.IRepository
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.DbImpl
import com.riders.thelab.data.local.LabDatabase
import com.riders.thelab.data.remote.ApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppHelperModule {

    @Provides
    fun provideDbHelper(appDatabase: LabDatabase) =
        DbImpl(
            appDatabase.getContactDao(),
            appDatabase.getWeatherDao()
        )

    @Provides
    fun provideRepository(dbImpl: DbImpl, apiImpl: ApiImpl) =
        RepositoryImpl(dbImpl, apiImpl) as IRepository
}