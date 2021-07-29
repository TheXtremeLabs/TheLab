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
import androidx.room.rxjava3.EmptyResultSetException
import androidx.work.*
import com.riders.thelab.core.utils.*
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl
) : ViewModel() {

    companion object {

        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"
        private const val WORK_RESULT = "work_result"
    }

    private var progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()
    private var downloadStatus: MutableLiveData<String> = MutableLiveData()
    private var workerStatus: MutableLiveData<WorkInfo.State> = MutableLiveData()
    private var downloadDone: MutableLiveData<Boolean> = MutableLiveData()
    private var isWeatherData: MutableLiveData<Boolean> = MutableLiveData()
    private var weatherCursor: MutableLiveData<Cursor> = MutableLiveData()
    private var oneCallWeather: MutableLiveData<OneCallWeatherResponse> = MutableLiveData()

    @SuppressLint("StaticFieldLeak")
    private lateinit var labLocationManager: LabLocationManager

    /* RxJava / RxAndroid */

    // Disposable is used to dispose the subscription when an Observer
    // no longer wants to listen to Observable
    // To prevent memory leak, for example when a task is done
    // and the activity/fragment is already destroyed,
    // the observer try to update the activity/fragment's UI that has been destroyed

    // CompositeDisposable can maintain list of subscriptions in  pool
    // and can dispose them all at once
    // Disposing them in Destroy one bye one is a tedious task and it can be error
    // prone as you might forgot to dispose.
    // In this case we can use CompositeDisposable.
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


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
                LabLocationUtils.buildTargetLocationObject(latitude, longitude)
            )
    }

    fun canGetLocation(activity: WeatherActivity, context: Context): Boolean {
        Timber.d("canGetLocation()")
        this.labLocationManager = LabLocationManager(activity, context)
        return this.labLocationManager.canGetLocation()
    }

    fun getCurrentWeather() {
        this.labLocationManager.getLocation()
    }


    fun fetchCities() {
        Timber.d("fetchCities()")
        if (!LabNetworkManagerNewAPI.isConnected) {
            hideLoader()
            connectionStatus.value = false
            return
        } else {

            showLoader()
            // First step
            // Call repository to check if there is data in database
            val disposable: Disposable =
                repositoryImpl
                    .getWeatherData()
                    .subscribe(
                        { weatherData ->
                            if (!weatherData.isWeatherData) {

                                // In this case record's return is null
                                // then we have to call our Worker to perform
                                // the web service call to retrieve data from api
                                Timber.e("List is empty. No Record found in database")

                                // Only for debug purposes
                                // Use worker to make long job operation in background
                                Timber.e("Use worker to make long job operation in background...")
                                isWeatherData.value = false
                            } else {
                                // In this case data already exists in database
                                // Load data then let the the user perform his request
                                Timber.d("Record found in database. Continue...")
                                hideLoader()
                                isWeatherData.value = true
                            }
                        },
                        { throwable ->
                            Timber.e("Error while fetching records in database")

                            if (throwable is EmptyResultSetException) {
                                Timber.e(throwable)
                                Timber.e("weatherData is empty. No Record found in database")
                                isWeatherData.value = false
                            } else {
                                Timber.e(throwable)
                            }
                        })

            compositeDisposable.add(disposable)
        }
    }


    fun fetchWeather(location: Location) {
        Timber.d("fetchWeather()")
        val disposable: Disposable =
            repositoryImpl
                .getWeatherOneCallAPI(location)
                .subscribe(
                    { weatherResponse ->
                        hideLoader()
                        oneCallWeather.value = weatherResponse
                    },
                    { throwable ->
                        Timber.e(throwable)
                        hideLoader()
                    }
                )
        compositeDisposable.add(disposable)
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
                activity,
                { workInfos: WorkInfo ->
                    when (workInfos.state) {
                        WorkInfo.State.ENQUEUED -> Timber.d("Worker ENQUEUED")
                        WorkInfo.State.RUNNING -> {
                            Timber.d("Worker RUNNING")
                            workerStatus.value = WorkInfo.State.RUNNING
                            downloadStatus.value = "Loading..."
                        }
                        WorkInfo.State.SUCCEEDED -> {

                            val disposable: Disposable =
                                repositoryImpl
                                    .insertWeatherData(WeatherData(true))
                                    .subscribe()

                            compositeDisposable.add(disposable)

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
                })
    }

    fun clearBackgroundResources(activity: WeatherActivity) {
        clearDisposables()
        cancelWorker(activity)
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

    private fun cancelWorker(activity: WeatherActivity) {
        Timber.e("cancelWorker()")
        Timber.d("Worker is about to be cancelled")
        WorkManager
            .getInstance(activity)
            .cancelAllWork()
    }
}