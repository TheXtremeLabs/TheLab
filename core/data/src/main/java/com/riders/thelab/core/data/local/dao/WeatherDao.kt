package com.riders.thelab.core.data.local.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.data.local.model.weather.CityModelFTS
import com.riders.thelab.core.data.local.model.weather.WeatherData

@Dao
interface WeatherDao {
    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRX(city: CityModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRX(cities: List<CityModel>): List<Long>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(isWeatherData: WeatherData): Long

    // TODO: Update room search time
    /*
     * Here the city table joins with the city_fts table on the name of a city.
     * This way, the query matches against the city_fts table but returns the columns of the city table.
     * With this change, the app now uses FTS instead of pattern matching.
     */
    @Query(
        "SELECT * FROM city " +
                "JOIN city_fts ON city_fts.name = city.name " +
                "WHERE city_fts.name MATCH :query " +
                "LIMIT 10"
    )
    suspend fun searchCity(query: String): List<CityModel>

    /*@Query(
        "SELECT * FROM city " +
                "JOIN city_fts ON city_fts.name = city.name " +
                "LIKE '%' || :query || '%' " +
                "LIMIT 10"
    )
    suspend fun searchCity(query: String): List<CityModel>*/

    @Transaction
    @Query("SELECT * FROM city INNER JOIN city_fts ON city.name = city_fts.name WHERE city_fts.name LIKE '%' || :query || '%' LIMIT 10")
    suspend fun searchCityFTS(query: String): List<CityModelFTS>

    @RawQuery([CityModelFTS::class])
    suspend fun searchCityWithRawQuery(rawQuery: SupportSQLiteQuery): List<CityModelFTS>

    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM weather_data LIMIT 1")
    fun getWeatherData(): WeatherData

    @Query("SELECT * FROM city")
    fun getCities(): List<CityModel>

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    fun getCities(cityText: String): LiveData<List<CityModel>>

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    fun getCitiesCursor(cityText: String): Cursor


    @Query("DELETE FROM city")
    fun deleteAll()
}