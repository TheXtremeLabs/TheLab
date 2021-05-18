package com.riders.thelab.di

import android.content.Context
import androidx.room.Room
import com.riders.thelab.data.local.LabDatabase
import com.riders.thelab.data.local.dao.ContactDao
import com.riders.thelab.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): LabDatabase {
        return Room
                .databaseBuilder(appContext, LabDatabase::class.java, LabDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    fun provideContactDao(appDatabase: LabDatabase): ContactDao {
        return appDatabase.getContactDao()
    }

    @Provides
    fun provideWeatherDao(appDatabase: LabDatabase): WeatherDao {
        return appDatabase.getWeatherDao()
    }
}