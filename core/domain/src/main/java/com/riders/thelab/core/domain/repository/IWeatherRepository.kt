package com.riders.thelab.core.domain.repository

import android.content.Context
import android.database.Cursor
import android.location.Location
import com.riders.thelab.core.common.network.DownloadState
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.model.weather.Weather
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call

interface IWeatherRepository {
    suspend fun hasWeatherData(): Boolean

    // --- DB
    suspend fun saveCity(city: City): Long
    suspend fun saveCities(dtoCities: List<City>): List<Long>
    suspend fun getCities(): List<City>
    fun getCitiesCursor(query: String): Cursor
    suspend fun searchCity(cityQuery: String): List<City>
    fun deleteAllCities()


    // --- API
    suspend fun getCurrentWeather(location: Location): Weather?
    suspend fun getBulkDownload(): Call<ResponseBody>
    suspend fun getBulkDownloadAsFlow(context: Context): Flow<DownloadState>
}