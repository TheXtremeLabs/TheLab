package com.riders.thelab.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.riders.thelab.core.data.local.dao.ContactDao
import com.riders.thelab.core.data.local.dao.WeatherDao
import com.riders.thelab.core.data.local.model.Contact
import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.data.local.model.weather.WeatherData

@Database(
    entities = [Contact::class, CityModel::class, WeatherData::class],
    version = 3,
    exportSchema = false
)
abstract class LabDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "lab"
    }

    abstract fun getContactDao(): ContactDao

    abstract fun getWeatherDao(): WeatherDao
}