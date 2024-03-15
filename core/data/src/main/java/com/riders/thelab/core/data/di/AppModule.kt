package com.riders.thelab.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.riders.thelab.core.data.local.LabDatabase
import com.riders.thelab.core.data.local.dao.ContactDao
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    /*@Singleton
    @Provides
    fun providesWeatherAppSearchManager(@ApplicationContext context: Context): WeatherSearchManager =
        WeatherSearchManager(context)*/


    //TODO : Due to Heroku back-end free services ending,
    // Use of the database to store and log users
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): LabDatabase {
        return Room
            .databaseBuilder(appContext, LabDatabase::class.java, LabDatabase.DATABASE_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // 3
                    db.execSQL("INSERT INTO city_fts(city_fts) VALUES ('rebuild')")
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(appDatabase: LabDatabase): UserDao {
        return appDatabase.getUserDao()
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