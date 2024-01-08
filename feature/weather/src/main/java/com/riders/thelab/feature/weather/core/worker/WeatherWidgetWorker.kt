package com.riders.thelab.feature.weather.core.worker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.FileProvider
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
import com.riders.thelab.core.data.local.model.weather.toWidgetModel
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.data.remote.dto.weather.toModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.utils.getIconUri
import com.riders.thelab.feature.weather.core.widget.WeatherGlanceStateDefinition
import com.riders.thelab.feature.weather.core.widget.WeatherInfo
import com.riders.thelab.feature.weather.core.widget.WeatherWidget
import com.riders.thelab.feature.weather.ui.WeatherUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.time.Duration
import java.util.Locale
import kotlin.math.roundToInt


@HiltWorker
class WeatherWidgetWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val mRepository: IRepository
) : CoroutineWorker(context, workerParams) {

    private var outputData: Data? = null

    @SuppressLint("NewApi")
    override suspend fun doWork(): Result {
        Timber.d("doWork()")

        // Update state to indicate loading
        setWidgetState(WeatherInfo.Loading)

        val labLocationManager = LabLocationManager(context)

        if (!labLocationManager.canGetLocation()) {
            Timber.e("Unable to get user's location (provider error)")
            // Unable to fetch user location
            outputData = createOutputData(WORK_LOCATION_FAILED)
            setWidgetState(WeatherInfo.Unavailable("Unable to fetch user's location"))
            return Result.failure(outputData!!)
        } else {
            Timber.i("Attempt to get current location")


            return runCatching {
                val location: Location? = labLocationManager.getCurrentLocation()

                if (null == location) {
                    Timber.e("Unable to get user's location")
                    setWidgetState(WeatherInfo.Unavailable("Unable to fetch user's location"))
                    Result.failure()
                } else {
                    val oneCallWeatherResponse: OneCallWeatherResponse? =
                        runBlocking { mRepository.getWeatherOneCallAPI(location) }

                    // Check if response is null
                    if (null == oneCallWeatherResponse) {
                        Timber.e("null == oneCallWeatherResponse | weather call failed, response value is null")

                        outputData = createOutputData(WORK_WEATHER_CALL_FAILED)
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
                            val weatherWidgetBundle =
                                runBlocking {
                                    //buildWeatherWidget(oneCallWeatherResponse)

                                    oneCallWeatherResponse.toWidgetModel().apply {
                                        this?.let {
                                            icon = it.icon.getIconUri(context)

                                        }
                                    }

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

                                    val weatherWidgetBundle =
                                        runBlocking {
//                                            buildWeatherWidget(oneCallWeatherResponse)

                                            oneCallWeatherResponse.toWidgetModel().apply {
                                                this?.let {
                                                    icon = getIconUri(it.icon)

                                                }
                                            }
                                        }

                                    weatherWidgetBundle?.let {
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
                    }

                    // Create and send outputData
                    outputData = createOutputData(WORK_SUCCESS)
                    Result.success(outputData!!)
                }
            }
                .onFailure {
                    it.printStackTrace()
                }
                .getOrElse {
                    Timber.e("runCatching | getOrElse | error caught with message: ${it.message}")
                    outputData = createOutputData(WORK_ERROR_FAILED)
                    setWidgetState(WeatherInfo.Unavailable(WORK_ERROR_FAILED))
                    Result.failure(outputData!!)
                }
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
        Timber.d("createOutputData() | message: $message")
        return Data.Builder()
            .put("work_result", message)
            .build()
    }

    /**
     * Build bundle to send to widget provider
     *
     */
    private suspend fun buildWeatherWidget(response: OneCallWeatherResponse): WeatherWidgetModel? {
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
        val path = context.imageLoader.diskCache?.openSnapshot(iconUrl)?.use { snapshot ->
            Timber.i("diskCache?.openSnapshot(iconUrl) | snapshot")
            val imageFile = snapshot.data.toFile()

            // Use the FileProvider to create a content URI
            val contentUri = FileProvider.getUriForFile(
                context,
                "com.riders.thelab.fileprovider",
                imageFile
            )

            // Find the current launcher everytime to ensure it has read permissions
            val resolveInfo = context.packageManager
                .resolveActivity(
                    Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) },
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            val launcherName = resolveInfo?.activityInfo?.packageName
            if (launcherName != null) {
                context.grantUriPermission(
                    launcherName,
                    contentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                )
            }

            Timber.i("return the path | ${contentUri.toString()}")
            // return the path
            contentUri.toString()
        }
        return requireNotNull(path) {
            "Couldn't find cached file"
        }
    }


    /**
     * Update the state of all widgets and then force update UI
     */
    private suspend fun setWidgetState(newState: WeatherInfo) {
        Timber.d("setWidgetState() | state: $newState")

        val widget = WeatherWidget()

        // To obtain the glanceId, query the GlanceAppWidgetManager
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(widget.javaClass)
        glanceIds.forEach { glanceId ->
            Timber.d("attempt to update widget with id: $glanceId")

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
        const val WORK_SUCCESS = "Loading finished"
        const val WORK_ERROR_FAILED: String =
            "Some errors occurred while processing data check log to see more details"
        const val WORK_WEATHER_CALL_FAILED: String = "Weather call failed, response value is null"
        const val WORK_LOCATION_FAILED: String = "Unable to fetch user's location"

        private val uniqueWorkName = WeatherWidgetWorker::class.java.simpleName

        /**
         * Enqueues a new worker to refresh weather data only if not enqueued already
         *
         * Note: if you would like to have different workers per widget instance you could provide
         * the unique name based on some criteria (e.g selected weather location).
         *
         * @param force set to true to replace any ongoing work and expedite the request
         */
        fun enqueue(context: Context, force: Boolean = false) {
            Timber.d("enqueue() | force: $force")

            val manager = WorkManager.getInstance(context)
            val requestBuilder = PeriodicWorkRequestBuilder<WeatherWidgetWorker>(
                Duration.ofMinutes(30)
            )
            var workPolicy = ExistingPeriodicWorkPolicy.KEEP

            // Replace any enqueued work and expedite the request
            if (force) {
                workPolicy = ExistingPeriodicWorkPolicy.UPDATE
            }

            manager.enqueueUniquePeriodicWork(
                uniqueWorkName,
                workPolicy,
                requestBuilder.build()
            )
        }

        /**
         * Cancel any ongoing worker
         */
        fun cancel(context: Context) {
            Timber.e("cancel()")
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }
}