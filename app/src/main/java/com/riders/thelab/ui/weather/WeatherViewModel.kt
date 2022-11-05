package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.riders.thelab.core.utils.*
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repositoryImpl: IRepository
) : ViewModel() {

    private val progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val downloadStatus: MutableLiveData<String> = MutableLiveData()
    private val workerStatus: MutableLiveData<WorkInfo.State> = MutableLiveData()
    private val downloadDone: MutableLiveData<Boolean> = MutableLiveData()
    private val isWeatherData: MutableLiveData<Boolean> = MutableLiveData()
    private val weatherCursor: MutableLiveData<Cursor> = MutableLiveData()
    private val oneCallWeather: MutableLiveData<OneCallWeatherResponse> = MutableLiveData()


    ///////////////////////////
    //
    // Observers
    //
    ///////////////////////////
    fun getProgressBarVisibility(): LiveData<Boolean> {
        return progressVisibility
    }

    fun getConnectionStatus(): LiveData<Boolean> {
        return connectionStatus
    }

    fun getDownloadStatus(): LiveData<String> {
        return downloadStatus
    }

    fun getDownloadDone(): LiveData<Boolean> {
        return downloadDone
    }

    fun getIsWeatherData(): LiveData<Boolean> {
        return isWeatherData
    }

    fun getWorkerStatus(): LiveData<WorkInfo.State> {
        return workerStatus
    }

    fun getOneCalWeather(): LiveData<OneCallWeatherResponse> {
        return oneCallWeather
    }


    ///////////////////////////
    //
    // Class methods
    //
    ///////////////////////////
    private fun showLoader() {
        progressVisibility.value = true
    }

    private fun hideLoader() {
        progressVisibility.value = false
    }

    fun getCityNameWithCoordinates(
        activity: WeatherActivity,
        latitude: Double,
        longitude: Double
    ): Address? {
        Timber.d("getCityNameWithCoordinates()")
        return LabAddressesUtils
            .getDeviceAddress(
                Geocoder(activity, Locale.getDefault()),
                (latitude to longitude).toLocation()
                // LabLocationUtils.buildTargetLocationObject(latitude, longitude)
            )
    }

    fun fetchCities(context: Context) {
        Timber.d("fetchCities()")
        if (!LabNetworkManagerNewAPI.getInstance(context).isOnline()) {
            hideLoader()
            connectionStatus.value = false
            return
        } else {

            showLoader()

            viewModelScope.launch(ioContext) {
                try {
                    // First step
                    // Call repository to check if there is data in database
                    val weatherData = repositoryImpl.getWeatherData()

                    if (null == weatherData || !weatherData.isWeatherData) {

                        // In this case record's return is null
                        // then we have to call our Worker to perform
                        // the web service call to retrieve data from api
                        Timber.e("List is empty. No Record found in database")

                        // Only for debug purposes
                        // Use worker to make long job operation in background
                        Timber.e("Use worker to make long job operation in background...")
                        withContext(mainContext) { isWeatherData.value = false }

                    } else {
                        // In this case data already exists in database
                        // Load data then let the the user perform his request
                        Timber.d("Record found in database. Continue...")
                        withContext(mainContext) {
                            hideLoader()
                            isWeatherData.value = true
                        }
                    }
                } catch (throwable: Exception) {
                    Timber.e("Error while fetching records in database")

//                    if (throwable is EmptyResultSetException) {
//                        Timber.e(throwable)
//                        Timber.e("weatherData is empty. No Record found in database")
//                        isWeatherData.value = false
//                    } else {
                    Timber.e(throwable)
//                    }
                }
            }
        }
    }


    fun fetchWeather(location: Location) {
        Timber.d("fetchWeather()")
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val weatherResponse = repositoryImpl.getWeatherOneCallAPI(location)

                    withContext(mainContext) {
                        hideLoader()
                        weatherResponse?.let {
                            oneCallWeather.value = it
                        }
                    }
                }
            } catch (throwable: Exception) {

                Timber.e(throwable)
                withContext(mainContext) {

                    hideLoader()
                }
            }
        }
    }


    /////////////////////////////////////
    //
    // WORKER
    //
    /////////////////////////////////////
    /**
     * Launch Worker that will manage download and extraction of the cities zip file from bulk openweather server
     */
    @SuppressLint("RestrictedApi")
    fun startWork(activity: WeatherActivity) {
        Timber.d("startWork()")

        val constraints = Constraints.Builder()
            //                .setRequiresBatteryNotLow(true)
            //                .setRequiresCharging(false)
            //                .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val weatherCitiesWorkRequest: WorkRequest =
            OneTimeWorkRequest.Builder(WeatherDownloadWorker::class.java)
                .setConstraints(constraints)
                .setInputData(
                    Data.Builder()
                        .putString(
                            URL_REQUEST,
                            Constants.BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD + Constants.WEATHER_BULK_DOWNLOAD_URL
                        )
                        .build()
                )
                .addTag(WeatherDownloadWorker::class.java.simpleName)
                .build()

        val id = weatherCitiesWorkRequest.id

        WorkManager
            .getInstance(activity)
            .enqueue(weatherCitiesWorkRequest)

        listenToTheWorker(activity, id)
    }


    private fun listenToTheWorker(activity: WeatherActivity, workerId: UUID) {
        Timber.d("listenToTheWorker : %s", workerId)
        WorkManager
            .getInstance(activity)
            .getWorkInfoByIdLiveData(workerId)
            .observe(
                activity
            ) { workInfos: WorkInfo ->
                when (workInfos.state) {
                    WorkInfo.State.ENQUEUED -> Timber.d("Worker ENQUEUED")
                    WorkInfo.State.RUNNING -> {
                        Timber.d("Worker RUNNING")
                        workerStatus.value = WorkInfo.State.RUNNING
                        downloadStatus.value = "Loading..."
                    }
                    WorkInfo.State.SUCCEEDED -> {

                        // Save data in database
                        viewModelScope.launch(Dispatchers.IO) {
                            repositoryImpl.insertWeatherData(WeatherData(true))
                        }

                        hideLoader()
                        downloadDone.value = true
                        downloadStatus.value = "Loading finished"
                        workerStatus.value = WorkInfo.State.SUCCEEDED
                    }
                    WorkInfo.State.FAILED -> {
                        Timber.e("Worker FAILED")
                        downloadDone.value = true
                        downloadStatus.value = "Worker FAILED"
                        workerStatus.value = WorkInfo.State.FAILED

                        UIManager.showActionInSnackBar(
                            activity,
                            activity.findViewById(android.R.id.content),
                            "Worker FAILED",
                            SnackBarType.ALERT,
                            "", null
                        )
                    }
                    WorkInfo.State.BLOCKED -> Timber.e("Worker BLOCKED")
                    WorkInfo.State.CANCELLED -> Timber.e("Worker CANCELLED")
                    else -> {
                    }
                }
            }
    }

    fun clearBackgroundResources(activity: WeatherActivity) {
        cancelWorker(activity)
    }

    private fun cancelWorker(activity: WeatherActivity) {
        Timber.e("cancelWorker()")
        Timber.d("Worker is about to be cancelled")
        WorkManager
            .getInstance(activity)
            .cancelAllWork()
    }


    companion object {
        private val ioContext = Dispatchers.IO
        private val mainContext = Dispatchers.Main

        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"
        private const val WORK_RESULT = "work_result"
    }
}