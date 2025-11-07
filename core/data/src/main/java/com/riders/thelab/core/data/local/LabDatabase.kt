package com.riders.thelab.core.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.riders.thelab.core.data.local.dao.ArtistDao
import com.riders.thelab.core.data.local.dao.ContactDao
import com.riders.thelab.core.data.local.dao.MusicRecognitionDao
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.dao.WeatherDao
import com.riders.thelab.core.data.local.model.Contact
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.data.local.model.music.MusicRecognitionModel
import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.data.local.model.weather.CityModelFTS
import com.riders.thelab.core.data.local.model.weather.WeatherData

@Database(
    entities = [
        User::class,
        ArtistModel::class,
        Contact::class,
        CityModelFTS::class,
        CityModel::class,
        MusicRecognitionModel::class,
        WeatherData::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
    exportSchema = true
)
abstract class LabDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "lab"
    }

    abstract fun getUserDao(): UserDao

    abstract fun getArtistDao(): ArtistDao
    abstract fun getContactDao(): ContactDao

    abstract fun getMusicRecognitionDao(): MusicRecognitionDao

    abstract fun getWeatherDao(): WeatherDao
}