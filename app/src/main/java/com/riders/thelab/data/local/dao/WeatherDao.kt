package com.riders.thelab.data.local.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRX(city: CityModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRX(cities: List<CityModel>): List<Long>

    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(isWeatherData: WeatherData): Long


    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM weather_data")
    suspend fun getWeatherData(): WeatherData

    @Query("SELECT * FROM city")
    suspend fun getCities(): List<CityModel>

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    fun getCities(cityText: String): LiveData<List<CityModel>>

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    fun getCitiesCursor(cityText: String): Cursor


    @Query("DELETE FROM city")
    fun deleteAll()
}