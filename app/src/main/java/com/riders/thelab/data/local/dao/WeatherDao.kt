package com.riders.thelab.data.local.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRX(city: CityModel): Maybe<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRX(cities: List<CityModel>): Maybe<List<Long>>

    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(isWeatherData: WeatherData): Maybe<Long>


    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM weather_data")
    fun getWeatherData(): Single<WeatherData>

    @Query("SELECT * FROM city")
    fun getCities(): Single<List<CityModel>>

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    fun getCities(cityText: String): LiveData<List<CityModel>>

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    fun getCitiesCursor(cityText: String): Cursor


    @Query("DELETE FROM city")
    fun deleteAll()
}