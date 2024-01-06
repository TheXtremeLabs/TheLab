package com.riders.thelab.feature.weather.core.worker

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ErrorResult
import coil.request.ImageRequest
import com.riders.thelab.core.common.utils.LabAddressesUtils
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabLocationManager
import com.riders.thelab.core.common.utils.LabLocationUtils
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.weather.ForecastWeatherWidgetModel
import com.riders.thelab.core.data.local.model.weather.TemperatureModel
import com.riders.thelab.core.data.local.model.weather.WeatherWidgetModel
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.data.remote.dto.weather.toModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.feature.weather.core.widget.WeatherGlanceStateDefinition
import com.riders.thelab.feature.weather.core.widget.WeatherInfo
import com.riders.thelab.feature.weather.core.widget.WeatherWidget
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
            setWidgetState(WeatherInfo.Unavailable("Unable to fetch user location"))
            return Result.failure(outputData!!)
        } else {
            try {
                location = labLocationManager.getCurrentLocation()
            } catch (e: Exception) {
                // Unable to fetch user location
                outputData = createOutputData(
                    "$WORK_LOCATION_FAILED | You may check if required permissions are granted"
                )
                setWidgetState(WeatherInfo.Unavailable("You may check if required permissions are granted"))
                return Result.failure(outputData!!)
            }
        }

        if (null == location) {
            setWidgetState(WeatherInfo.Unavailable("Unable to fetch user location"))
            return Result.failure()
        }

        // Update state to indicate loading
        setWidgetState(WeatherInfo.Loading)

        return runCatching {
            suspendCancellableCoroutine<Result> {
                val oneCallWeatherResponse: OneCallWeatherResponse? =
                    runBlocking { mRepository.getWeatherOneCallAPI(location) }

                // Check if response is null
                if (null == oneCallWeatherResponse) {
                    Timber.e(WeatherDownloadWorker.WORK_DOWNLOAD_FAILED)
                    outputData = createOutputData(WORK_DOWNLOAD_FAILED)
                    Result.failure(outputData!!)
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

                        // val weatherBundle = buildWeatherBundle(oneCallWeatherResponse, cityName!!, country!!)
                        // updateWidgetViaBroadcast(weatherWidgetBundle)

                        val weatherWidgetBundle =
                            runBlocking {
                                buildWeatherWidget(
                                    oneCallWeatherResponse,
                                    cityName!!,
                                    country!!
                                )
                            }

                        if (null == weatherWidgetBundle) {
                            Timber.e("Failed to build weather widget object because fields may be null")
                        } else {
                            // updateWidgetViaBroadcast(weatherWidgetBundle)
                            runBlocking {
                                setWidgetState(
                                    WeatherInfo.Available(
                                        cityName!!,
                                        weatherWidgetBundle
                                    )
                                )
                            }
                        }

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

                                /*val weatherBundle = buildWeatherBundle(
                                    oneCallWeatherResponse,
                                    cityName!!,
                                    country!!
                                )*/

                                // updateWidgetViaBroadcast(weatherBundle)

                                val weatherWidgetBundle =
                                    runBlocking {
                                        buildWeatherWidget(
                                            oneCallWeatherResponse,
                                            cityName!!,
                                            country!!
                                        )
                                    }
                                weatherWidgetBundle?.let {
                                    // updateWidgetViaBroadcast(weatherWidgetBundle)

                                    runBlocking {
                                        setWidgetState(
                                            WeatherInfo.Available(
                                                cityName!!,
                                                weatherWidgetBundle
                                            )
                                        )
                                    }
                                }
                                    ?: run { Timber.e("Failed to build weather widget object because fields may be null") }

                                // Create and send outputData
                                outputData = createOutputData(WORK_SUCCESS)
                                Result.success(outputData!!)
                            }
                        }
                    }

                    Result.success()
                }
            }
        }.getOrElse {
            Timber.e(WeatherDownloadWorker.WORK_DOWNLOAD_FAILED)
            outputData = createOutputData(WORK_DOWNLOAD_FAILED)
            setWidgetState(WeatherInfo.Unavailable(WeatherDownloadWorker.WORK_DOWNLOAD_FAILED))
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

    /**
     * Build bundle to send to widget provider
     *
     */
    private suspend fun buildWeatherWidget(
        response: OneCallWeatherResponse,
        city: String,
        country: String
    ): WeatherWidgetModel? {
        Timber.d("buildWeatherWidget()")

        response.currentWeather?.run {
            val temperature =
                TemperatureModel(temperature = this.temperature, realFeels = this.feelsLike)

            val description: String? = this.weather?.get(0)?.description
            val icon: String? =
                this.weather?.get(0)?.let {
                    WeatherUtils.getWeatherIconFromApi(
                        it.icon
                    )
                }?.run {
                    getIconUri(this)
                }


            val dailyWeather: List<ForecastWeatherWidgetModel>? = response.dailyWeather?.run {
                this.map {
                    ForecastWeatherWidgetModel(
                        it.temperature.toModel(),
                        it.weather[0].icon
                    )
                }.toList()
            }

            val currentTemperature =
                "${this.temperature.roundToInt()} ${context.getString(R.string.degree_placeholder)}"
            val realFeels =
                "${this.feelsLike.roundToInt()} ${context.getString(R.string.degree_placeholder)}"

            temperature.let { temp ->
                icon?.let { ic ->
                    description?.let { desc ->
                        dailyWeather?.let { daily ->
                            return WeatherWidgetModel(temp, ic, desc, daily)
                        }
                    }
                }
            }
        } ?: run {
            Timber.e("current weather object is null")
            return null
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    private suspend fun getIconUri(iconUrl: String): String {
        Timber.d("getIconUri() | icon url: $iconUrl")

        val request = ImageRequest.Builder(context)
            .data(iconUrl)
            .build()

        // Request the image to be loaded and throw error if it failed
        with(context.imageLoader) {
            diskCache?.remove(iconUrl)
            memoryCache?.remove(MemoryCache.Key(iconUrl))

            val result = execute(request)
            if (result is ErrorResult) {
                throw result.throwable
            }
        }

        // Get the path of the loaded image from DiskCache.
        val path = context.imageLoader.diskCache?.get(iconUrl)?.use { snapshot ->
            val imageFile = snapshot.data.toFile()

            // Use the FileProvider to create a content URI
            val contentUri = FileProvider.getUriForFile(
                context,
                "com.riders.theLab.fileprovider",
                imageFile
            )

            // Find the current launcher everytime to ensure it has read permissions
            val resolveInfo = context.packageManager.resolveActivity(
                Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) },
                PackageManager.MATCH_DEFAULT_ONLY
            )
            val launcherName = resolveInfo?.activityInfo?.packageName
            if (launcherName != null) {
                context.grantUriPermission(
                    launcherName,
                    contentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                )
            }

            // return the path
            contentUri.toString()
        }
        return requireNotNull(path) {
            "Couldn't find cached file"
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

    private fun updateWidgetViaBroadcast(weatherWidgetModel: WeatherWidgetModel) {
        Timber.d("updateWidgetViaBroadcast()")

        val broadcastIntent: Intent =
            Intent(context, WeatherWidgetReceiver::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(EXTRA_WEATHER_WIDGET, weatherWidgetModel)
            }

        context.sendBroadcast(broadcastIntent)
    }


    /**
     * Update the state of all widgets and then force update UI
     */
    private suspend fun setWidgetState(newState: WeatherInfo) {
        val widget = WeatherWidget()

        // To obtain the glanceId, query the GlanceAppWidgetManager
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(widget.javaClass)
        glanceIds.forEach { glanceId ->
            widget.update(context, glanceId)
            updateAppWidgetState(
                context = context,
                definition = WeatherGlanceStateDefinition,
                glanceId = glanceId,
                updateState = {

                    if (newState is WeatherInfo.Available) {
                        /*    it[WeatherWidget.getImageKey(48f, 48f)] = Uri.parse(newState.currentData.icon)
                            it[WeatherWidget.sourceKey] = "Picsum Photos"
                            it[WeatherWidget.sourceUrlKey] = newState.currentData.icon*/


                    }

                    newState
                }
            )

        }
        widget.updateAll(context)
    }

    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED: String = "Error while downloading zip file"
        const val WORK_LOCATION_FAILED: String = "Unable to fetch user location"
        const val WORK_RESULT = "work_result"

        const val EXTRA_WEATHER_WIDGET = "EXTRA_WEATHER_WIDGET"
        const val EXTRA_WEATHER_CITY = "EXTRA_WEATHER_CITY"
        const val EXTRA_WEATHER_COUNTRY = "EXTRA_WEATHER_COUNTRY"
        const val EXTRA_WEATHER_DESCRIPTION = "EXTRA_WEATHER_DESCRIPTION"
        const val EXTRA_WEATHER_TEMPERATURE = "EXTRA_WEATHER_TEMPERATURE"
        const val EXTRA_WEATHER_REAL_FEELS = "EXTRA_WEATHER_REAL_FEELS"
        const val EXTRA_WEATHER_ICON = "EXTRA_WEATHER_ICON"
    }
}