package com.riders.thelab.feature.weather.core.worker

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.riders.thelab.core.common.utils.LabAddressesUtils
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabLocationManager
import com.riders.thelab.core.common.utils.LabLocationUtils
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.ui.R
import com.riders.thelab.feature.weather.core.widget.WeatherWidgetReceiver
import com.riders.thelab.feature.weather.ui.WeatherUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val repository: IRepository
) : CoroutineWorker(context, workerParams) {


    private var outputData: Data? = null

    @Inject
    lateinit var mRepository: IRepository

    @SuppressLint("NewApi")
    override suspend fun doWork(): Result {
        Timber.d("doWork()")

        val location: Location?
        val labLocationManager = LabLocationManager(context)

        if (!labLocationManager.canGetLocation()) {
            // Unable to fetch user location
            outputData = createOutputData(WORK_LOCATION_FAILED)
            return Result.failure(outputData!!)
        } else {
            try {
                location = labLocationManager.getCurrentLocation()
            } catch (e: Exception) {
                // Unable to fetch user location
                outputData = createOutputData(
                    "$WORK_LOCATION_FAILED | You may check if required permissions are granted"
                )
                return Result.failure(outputData!!)
            }
        }

        if (null == location) return Result.failure()

        return try {
            suspendCancellableCoroutine {

                val oneCallWeatherResponse: OneCallWeatherResponse? = runBlocking {
                    mRepository.getWeatherOneCallAPI(location)
                }

                // Check if response is null
                if (null == oneCallWeatherResponse) {
                    Result.failure()
                } else {
                    Timber.d("observer.onSuccess(responseFile)")

                    if (!LabCompatibilityManager.isTiramisu()) {
                        val address = LabAddressesUtils
                            .getDeviceAddressLegacy(
                                Geocoder(context, Locale.getDefault()),
                                LabLocationUtils.buildTargetLocationObject(
                                    oneCallWeatherResponse.latitude,
                                    oneCallWeatherResponse.longitude
                                )
                            )

                        // Load city name
                        val cityName = address?.locality
                        val country = address?.countryName

                        val weatherBundle =
                            buildWeatherBundle(oneCallWeatherResponse, cityName!!, country!!)
                        updateWidgetViaBroadcast(weatherBundle)

                        // Create and send outputData
                        outputData = createOutputData(WORK_SUCCESS)
                        Result.success(outputData!!)

                    } else {
                        LabAddressesUtils.getDeviceAddressAndroid13(
                            Geocoder(context, Locale.getDefault()),
                            LabLocationUtils.buildTargetLocationObject(
                                oneCallWeatherResponse.latitude,
                                oneCallWeatherResponse.longitude
                            )
                        ) {
                            it?.let {
                                // Load city name
                                val cityName = it.locality
                                val country = it.countryName

                                val weatherBundle = buildWeatherBundle(
                                    oneCallWeatherResponse,
                                    cityName!!,
                                    country!!
                                )
                                updateWidgetViaBroadcast(weatherBundle)

                                // Create and send outputData
                                outputData = createOutputData(WORK_SUCCESS)
                                Result.success(outputData!!)
                            }
                        }
                    }
                }
            }
        } catch (throwable: Exception) {
            Timber.e(WeatherDownloadWorker.WORK_DOWNLOAD_FAILED)
            Timber.e(throwable)
            outputData = createOutputData(WORK_DOWNLOAD_FAILED)
            Result.failure(outputData!!)
        }
    }

    /**
     * Creates ouput data to send back to the activity / presenter which is listening to it
     *
     * @param message
     * @return
     */

    @SuppressLint("RestrictedApi")
    private fun createOutputData(message: String): Data {
        Timber.d("createOutputData()")
        return Data.Builder()
            .put("work_result", message)
            .build()
    }


    /**
     * Build bundle to send to widget provider
     *
     */
    private fun buildWeatherBundle(
        response: OneCallWeatherResponse,
        city: String,
        country: String
    ): Bundle {
        Timber.d("buildWeatherBundle()")
        val description = response.currentWeather?.weather?.get(0)?.description
        val temperature =
            "${response.currentWeather?.temperature?.roundToInt()} ${context.getString(R.string.degree_placeholder)}"
        val realFeels =
            "${response.currentWeather?.feelsLike?.roundToInt()} ${
                context.getString(
                    R.string.degree_placeholder
                )
            }"
        val icon =
            response.currentWeather?.weather?.get(0)?.let {
                WeatherUtils.getWeatherIconFromApi(
                    it.icon
                )
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

    private fun updateWidgetViaBroadcast(bundle: Bundle) {
        Timber.d("updateWidgetViaBroadcast()")

        val broadcastIntent: Intent =
            Intent(context, WeatherWidgetReceiver::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtras(bundle)
            }

        context.sendBroadcast(broadcastIntent)
    }

    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED: String = "Error while downloading zip file"
        const val WORK_LOCATION_FAILED: String = "Unable to fetch user location"
        const val WORK_RESULT = "work_result"

        const val EXTRA_WEATHER_CITY = "EXTRA_WEATHER_CITY"
        const val EXTRA_WEATHER_COUNTRY = "EXTRA_WEATHER_COUNTRY"
        const val EXTRA_WEATHER_DESCRIPTION = "EXTRA_WEATHER_DESCRIPTION"
        const val EXTRA_WEATHER_TEMPERATURE = "EXTRA_WEATHER_TEMPERATURE"
        const val EXTRA_WEATHER_REAL_FEELS = "EXTRA_WEATHER_REAL_FEELS"
        const val EXTRA_WEATHER_ICON = "EXTRA_WEATHER_ICON"
    }
}