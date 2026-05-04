package com.riders.thelab.feature.weather.core.worker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.riders.thelab.core.common.location.LabLocationManager
import com.riders.thelab.core.common.utils.DateTimeUtils
import com.riders.thelab.core.common.utils.LabAddressesUtils
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.common.worker.BaseCoroutineWorker
import com.riders.thelab.core.domain.model.weather.FeelsLike
import com.riders.thelab.core.domain.model.weather.ForecastWeatherWidget
import com.riders.thelab.core.domain.model.weather.Temperature
import com.riders.thelab.core.domain.model.weather.Weather
import com.riders.thelab.core.domain.model.weather.WeatherWidget
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.ui.R
import com.riders.thelab.feature.weather.utils.WeatherUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.Locale
import kotlin.math.roundToInt

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val repository: IWeatherRepository
) : BaseCoroutineWorker(context, workerParams) {


    @SuppressLint("NewApi")
    override suspend fun doWork(): Result {
        Timber.d("doWork()")

        val labLocationManager = LabLocationManager.getInstance(context)
        val location: Location? = labLocationManager.getCurrentLocation()

        if (null == location) {
            Timber.e("Location object is null. Unable to get user's location")
            // Unable to fetch user location
            return Result.failure(createOutputData(WORK_RESULT to WORK_LOCATION_FAILED))
        }

        val geocoder: Geocoder = if (LabCompatibilityManager.isTiramisu()) Geocoder(
            context,
            Locale.getDefault()
        ) else Geocoder(context)

        return runCatching {
            suspendCancellableCoroutine<Result> {
                val weather = runBlocking { repository.getCurrentWeather(location) }

                // Check if response is null
                if (null == weather) {
                    Timber.e("null == oneCallWeatherResponse | weather call failed, response value is null")

                    Result.failure(createOutputData(WORK_RESULT to WORK_WEATHER_CALL_FAILED))
                } else {
                    Timber.d("observer.onSuccess(responseFile)")

                    val weatherLocation = (weather.latitude to weather.longitude).toLocation()

                    if (!LabCompatibilityManager.isTiramisu()) {
                        val address =
                            LabAddressesUtils.getDeviceAddressLegacy(geocoder, weatherLocation)

                        // Load city name
                        val cityName = address?.locality
                        val country = address?.countryName

                        // val weatherBundle = buildWeatherBundle(oneCallWeatherResponse, cityName!!, country!!)
                        // updateWidgetViaBroadcast(weatherWidgetBundle)

                        val weatherWidgetBundle = runBlocking {
                            buildWeatherWidget(weather)
                        }

                        if (null == weatherWidgetBundle) {
                            Timber.e("Failed to build weather widget object because fields may be null")
                        } else {
                            // Create and send outputData
                            Result.success(createOutputData(WORK_RESULT to WORK_SUCCESS))
                        }

                    } else {
                        LabAddressesUtils.getDeviceAddressAndroid13(geocoder, weatherLocation) {
                            it?.let {
                                // Load city name
                                val cityName = it.locality
                                val country = it.countryName

                                /*val weatherBundle = buildWeatherBundle(
                                    oneCallWeatherResponse,
                                    cityName!!,
                                    country!!
                                )*/

                                // updateWidgetViaBroadcast(weatherBundle)

                                val weatherWidgetBundle =
                                    runBlocking { buildWeatherWidget(weather) }
                                weatherWidgetBundle?.let {
                                    // updateWidgetViaBroadcast(weatherWidgetBundle)
                                    // Create and send outputData
                                    Result.success(createOutputData(WORK_RESULT to WORK_SUCCESS))
                                }
                                    ?: run { Timber.e("Failed to build weather widget object because fields may be null") }
                            }
                        }
                    }

                    Result.success()
                }
            }
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("runCatching | onFailure | error caught with message: ${it.message}")
            }
            .getOrElse {
                Timber.e("runCatching | getOrElse | error caught with message: ${it.message}")
                Result.failure(createOutputData(WORK_RESULT to WORK_ERROR_FAILED))
            }
    }


    /**
     * Build bundle to send to widget provider
     *
     */
    private fun buildWeatherBundle(
        response: Weather,
        city: String,
        country: String
    ): Bundle {
        Timber.d("buildWeatherBundle()")
        val description = response.description
        val temperature =
            "${response.temperature?.temperature?.roundToInt()} ${context.getString(R.string.degree_placeholder)}"
        val realFeels =
            "${response.feelsLike?.feelsLike?.roundToInt()} ${
                context.getString(
                    R.string.degree_placeholder
                )
            }"
        val icon = response.weatherIconUrl?.let {
            WeatherUtils.getWeatherIconFromApi(it)
        }

        return Bundle().apply {
            putString(EXTRA_WEATHER_CITY, city)
            putString(EXTRA_WEATHER_COUNTRY, country)
            putString(EXTRA_WEATHER_DESCRIPTION, description)
            putString(EXTRA_WEATHER_TEMPERATURE, temperature)
            putString(EXTRA_WEATHER_REAL_FEELS, realFeels)
            putString(EXTRA_WEATHER_ICON, icon)
        }
    }

    /**
     * Build bundle to send to widget provider
     *
     */
    private fun buildWeatherWidget(response: Weather): WeatherWidget? {
        Timber.d("buildWeatherWidget()")

        return response?.run {
            val temperature = Temperature(
                day = this.temperature?.day ?: 0.0,
                night = this.temperature?.night ?: 0.0,
                evening = this.temperature?.evening ?: 0.0,
                morning = this.temperature?.morning ?: 0.0,
                min = this.temperature?.min ?: 0.0,
                max = this.temperature?.max ?: 0.0
            )
            val feelsLike = FeelsLike(
                this.feelsLike?.day ?: 0.0,
                this.feelsLike?.night ?: 0.0,
                this.feelsLike?.evening ?: 0.0,
                this.feelsLike?.morning ?: 0.0
            )

            val description: String? = this?.description
            val icon: String? = this?.weatherIconUrl?.let {
                WeatherUtils.getWeatherIconFromApi(it)
            }


            val dailyWeather: List<ForecastWeatherWidget>? = response.dailyWeather?.run {
                this.map {
                    ForecastWeatherWidget(
                        day = DateTimeUtils.getDayFromTime(it.dateTimeUTC),
                        temperature = it.temperature!!,
                        icon = it.weatherIconUrl!!
                    )
                }.toList()
            }

            /*val currentTemperature = "${this.temperature.roundToInt()} ${context.getString(R.string.degree_placeholder)}"
            val realFeels = "${this.feelsLike.roundToInt()} ${context.getString(R.string.degree_placeholder)}"*/

            temperature.let { temp ->
                icon?.let { ic ->
                    description?.let { desc ->
                        dailyWeather?.let { daily ->
                            return WeatherWidget(
                                description = desc,
                                icon = ic,
                                temperature = temp,
                                forecast = daily
                            )
                        }
                    }
                }
            }
        } ?: run {
            Timber.e("current weather object is null")
            return null
        }
    }


    companion object {
        const val WORK_RESULT = "work_result"
        const val WORK_SUCCESS = "Loading finished"
        const val WORK_ERROR_FAILED: String =
            "Some errors occurred while processing data check log to see more details"
        const val WORK_WEATHER_CALL_FAILED: String = "Weather call failed, response value is null"
        const val WORK_LOCATION_FAILED: String = "Unable to fetch user's location"

        const val EXTRA_WEATHER_WIDGET = "EXTRA_WEATHER_WIDGET"
        const val EXTRA_WEATHER_CITY = "EXTRA_WEATHER_CITY"
        const val EXTRA_WEATHER_COUNTRY = "EXTRA_WEATHER_COUNTRY"
        const val EXTRA_WEATHER_DESCRIPTION = "EXTRA_WEATHER_DESCRIPTION"
        const val EXTRA_WEATHER_TEMPERATURE = "EXTRA_WEATHER_TEMPERATURE"
        const val EXTRA_WEATHER_REAL_FEELS = "EXTRA_WEATHER_REAL_FEELS"
        const val EXTRA_WEATHER_ICON = "EXTRA_WEATHER_ICON"
    }
}