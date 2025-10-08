package com.riders.thelab.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.riders.thelab.core.data.BuildConfig
import com.riders.thelab.core.data.local.LabDatabase
import com.riders.thelab.core.data.local.dao.ArtistDao
import com.riders.thelab.core.data.local.dao.ContactDao
import com.riders.thelab.core.data.local.dao.MusicRecognitionDao
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    /*@Singleton
    @Provides
    fun providesWeatherAppSearchManager(@ApplicationContext context: Context): WeatherSearchManager =
        WeatherSearchManager(context)*/


    //TODO : Due to Heroku back-end free services ending,
    // Use of the database to store and log users
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): LabDatabase {
        return Room
            .databaseBuilder(appContext, LabDatabase::class.java, LabDatabase.DATABASE_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // 3
                    db.execSQL("INSERT INTO city_fts(city_fts) VALUES('rebuild')")
                }
            })
            .setQueryCallback(
                { sqlQuery, bindArgs ->
                    if (BuildConfig.DEBUG) {
                        Timber.d("QueryCallback | SQL Query: $sqlQuery, SQL Args: $bindArgs")
                    }
                },
                Executors.newSingleThreadExecutor()
            )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: LabDatabase): UserDao = appDatabase.getUserDao()


    @Singleton
    @Provides
    fun provideArtistDao(appDatabase: LabDatabase): ArtistDao = appDatabase.getArtistDao()

    @Singleton
    @Provides
    fun provideContactDao(appDatabase: LabDatabase): ContactDao = appDatabase.getContactDao()

    @Singleton
    @Provides
    fun provideMusicRecognitionDao(appDatabase: LabDatabase): MusicRecognitionDao =
        appDatabase.getMusicRecognitionDao()

    @Singleton
    @Provides
    fun provideWeatherDao(appDatabase: LabDatabase): WeatherDao = appDatabase.getWeatherDao()
}