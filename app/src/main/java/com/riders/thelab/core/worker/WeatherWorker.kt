package com.riders.thelab.core.worker

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
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabAddressesUtils
import com.riders.thelab.core.utils.LabLocationManager
import com.riders.thelab.core.utils.LabLocationUtils
import com.riders.thelab.core.widget.TheLabAppWidgetProvider
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.ui.weather.WeatherDownloadWorker
import com.riders.thelab.ui.weather.WeatherUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED: String = "Error while downloading zip file"
        const val WORK_LOCATION_FAILED: String = "Unable to fetch user location"
        const val WORK_RESULT = "work_result"
    }

    private var outputData: Data? = null

    @Inject
    lateinit var mRepository: IRepository

    override suspend fun doWork(): Result {
        Timber.d("doWork()")

        val location: Location?
        val labLocationManager = LabLocationManager(context)

        if (!labLocationManager.canGetLocation()) {
            // Unable to fetch user location
            outputData = createOutputData(
                WORK_RESULT,
                WORK_LOCATION_FAILED
            )
            return Result.failure(outputData!!)
        } else {
            try {
                location = labLocationManager.getCurrentLocation()
            } catch (e: Exception) {
                // Unable to fetch user location
                outputData = createOutputData(
                    WORK_RESULT,
                    "$WORK_LOCATION_FAILED | You may check if required permissions are granted"
                )
                return Result.failure(outputData!!)
            }
        }

        if (null == location) return Result.failure()

        return try {
            suspendCancellableCoroutine {

                val job = runBlocking {
                    mRepository.getWeatherOneCallAPI(location)
                }

                // Check if response is null
                if (null == job) {
                    Result.failure()
                } else {

                    Timber.d("observer.onSuccess(responseFile)")

                    val address = LabAddressesUtils
                        .getDeviceAddress(
                            Geocoder(context, Locale.getDefault()),
                            LabLocationUtils.buildTargetLocationObject(
                                job.latitude,
                                job.longitude
                            )
                        )

                    // Load city name
                    val cityName = address?.locality
                    val country = address?.countryName

                    val weatherBundle = buildWeatherBundle(job, cityName!!, country!!)
                    updateWidgetViaBroadcast(weatherBundle)

                    // Create and send outputData
                    outputData = createOutputData(
                        WORK_RESULT,
                        WORK_SUCCESS
                    )
                    Result.success(outputData!!)
                }
            }
        } catch (throwable: Exception) {

            Timber.e(WeatherDownloadWorker.WORK_DOWNLOAD_FAILED)
            Timber.e(throwable)
            outputData = createOutputData(
                WORK_RESULT,
                WORK_DOWNLOAD_FAILED
            )
            Result.failure(outputData!!)
        }
    }

    /**
     * Creates ouput data to send back to the activity / presenter which is listening to it
     *
     * @param outputDataKey
     * @param message
     * @return
     */
    @SuppressLint("RestrictedApi")
    private fun createOutputData(outputDataKey: String, message: String): Data {
        Timber.d("createOutputData()")
        return Data.Builder()
            .put(outputDataKey, message)
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
            putString(TheLabAppWidgetProvider.EXTRA_WEATHER_CITY, city)
            putString(TheLabAppWidgetProvider.EXTRA_WEATHER_COUNTRY, country)
            putString(TheLabAppWidgetProvider.EXTRA_WEATHER_DESCRIPTION, description)
            putString(TheLabAppWidgetProvider.EXTRA_WEATHER_TEMPERATURE, temperature)
            putString(TheLabAppWidgetProvider.EXTRA_WEATHER_REAL_FEELS, realFeels)
            putString(TheLabAppWidgetProvider.EXTRA_WEATHER_ICON, icon)
        }
    }

    private fun updateWidgetViaBroadcast(bundle: Bundle) {
        Timber.d("updateWidgetViaBroadcast()")

        val broadcastIntent: Intent =
            Intent(context, TheLabAppWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtras(bundle)
            }

        context.sendBroadcast(broadcastIntent)
    }
}