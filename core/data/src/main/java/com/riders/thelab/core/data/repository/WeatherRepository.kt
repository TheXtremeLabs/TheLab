package com.riders.thelab.core.data.repository

import android.content.Context
import android.database.Cursor
import android.location.Location
import com.riders.thelab.core.common.network.DownloadState
import com.riders.thelab.core.data.local.IDb
import com.riders.thelab.core.data.mapper.toDTO
import com.riders.thelab.core.data.mapper.toDomainModel
import com.riders.thelab.core.data.mapper.toModel
import com.riders.thelab.core.data.remote.IApi
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.model.weather.Weather
import com.riders.thelab.core.domain.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

class WeatherRepository(
    private val db: IDb,
    private val api: IApi
) : IWeatherRepository {

    override suspend fun hasWeatherData(): Boolean = getCities().isNotEmpty()

    override suspend fun saveCity(city: City): Long = db.saveCity(city.toModel())

    override suspend fun saveCities(
        dtoCities: List<City>
    ): List<Long> = db.saveCities(dtoCities.map { it.toDTO() })

    override suspend fun getCities(): List<City> = db.getCities().map { it.toDomainModel() }

    override fun getCitiesCursor(query: String): Cursor = db.getCitiesCursor("%$query%")


    override suspend fun searchCity(
        cityQuery: String
    ): List<City> = db.searchCity(cityQuery).map { it.toDomainModel() }

    override fun deleteAllCities() = db.deleteAllCities()

    override suspend fun getCurrentWeather(location: Location): Weather? = api
        .getCurrentWeather(location)
        ?.toDomainModel()

    override suspend fun getBulkDownload(): Call<ResponseBody> = api.getBulkDownload()

    override suspend fun getBulkDownloadAsFlow(context: Context): Flow<DownloadState> = api
        .getBulkDownloadAsFlow(
            context = context,
            filePath = context.filesDir.absolutePath + File.separator + "tmp/weather_data"
        )

}