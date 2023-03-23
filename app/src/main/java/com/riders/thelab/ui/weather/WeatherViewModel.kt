package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.riders.thelab.core.utils.*
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.local.model.compose.WeatherUIState
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repositoryImpl: IRepository
) : ViewModel() {
    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _weatherUiState = MutableStateFlow<WeatherUIState>(WeatherUIState.Loading)
    val weatherUiState = _weatherUiState
    fun updateUIState(state: WeatherUIState) {
        _weatherUiState.value = state
    }

    var searchText by mutableStateOf("")

    var expanded by mutableStateOf(false)

    var suggestions by mutableStateOf(emptyList<String>())
        private set


    fun updateSearchText(searchQuery: String) {
        searchText = searchQuery

        if (2 <= searchText.length) {
            expanded = true
            getCitiesFromDb(searchText)
        } else {
            expanded = false
        }
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private var searchDbJob: Job? = null
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    //////////////////////////////////////////
    //Live Data
    //////////////////////////////////////////
    private val progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val downloadStatus: MutableLiveData<String> = MutableLiveData()
    private val workerStatus: MutableLiveData<WorkInfo.State> = MutableLiveData()
    private val downloadDone: MutableLiveData<Boolean> = MutableLiveData()
    private val isWeatherData: MutableLiveData<Boolean> = MutableLiveData()
    private val weatherCursor: MutableLiveData<Cursor> = MutableLiveData()
    private val oneCallWeather: MutableLiveData<OneCallWeatherResponse> = MutableLiveData()

    fun getProgressBarVisibility(): LiveData<Boolean> = progressVisibility
    fun getConnectionStatus(): LiveData<Boolean> = connectionStatus
    fun getDownloadStatus(): LiveData<String> = downloadStatus
    fun getDownloadDone(): LiveData<Boolean> = downloadDone
    fun getIsWeatherData(): LiveData<Boolean> = isWeatherData
    fun getWorkerStatus(): LiveData<WorkInfo.State> = workerStatus
    fun getOneCalWeather(): LiveData<OneCallWeatherResponse> = oneCallWeather


    ///////////////////////////
    //
    // Class methods
    //
    ///////////////////////////
    fun getCitiesFromDb(query: String) {
        Timber.d("getCitiesFromDb() | query: $query")

        if (null != searchDbJob && searchDbJob?.isActive == true) {
            searchDbJob?.cancel()
        }

        searchDbJob = viewModelScope.launch(IO /*+ SupervisorJob() + coroutineExceptionHandler*/) {

            try {
                val cursor = repositoryImpl.getCitiesCursor(searchText)

                withContext(Main) {
                    handleResults(cursor)
                }
            } catch (exception: Exception) {
                handleError(exception)
            }
        }
        // viewModelScope.launch { searchDbJob?.join() }
    }

    private fun handleResults(cursor: Cursor) {
        Timber.d("handleResults() | available cursor's column: ${cursor.columnNames.toString()}")

        if (suggestions.isNotEmpty()) suggestions = mutableListOf()

        val tempList = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG

                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val country = cursor.getString(cursor.getColumnIndexOrThrow("country"))

                Timber.d("handleResults() | name: $name, country: $country")

                tempList.add("$name, $country")

                cursor.moveToNext()
            }

            suggestions = tempList.take(10).toList()
        }
        /* mSearchView.suggestionsAdapter =
             WeatherSearchViewAdapter(
                 context,
                 cursor,
                 mSearchView,
                 listener
             )*/
    }

    private fun handleError(t: Throwable) {
        Timber.e("handleError() | ${t.message}")
        Timber.e("Problem in Fetching City")
        /*Toast.makeText(
            context, "Problem in Fetching City",
            Toast.LENGTH_LONG
        ).show()*/
    }

    private fun showLoader() {
        progressVisibility.value = true
    }

    private fun hideLoader() {
        progressVisibility.value = false
    }

    fun retry() {
        Timber.d("Retrying...")
        updateUIState(WeatherUIState.Loading)
    }

    fun getCityNameWithCoordinates(
        activity: WeatherActivity,
        latitude: Double,
        longitude: Double
    ): Address? {
        Timber.d("getCityNameWithCoordinates()")
        return LabAddressesUtils.getDeviceAddress(
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

            viewModelScope.launch(IO + coroutineExceptionHandler) {
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
                        withContext(Main) { isWeatherData.value = false }
                    } else {
                        // In this case data already exists in database
                        // Load data then let the the user perform his request
                        Timber.d("Record found in database. Continue...")
                        withContext(Main) {
                            hideLoader()
                            isWeatherData.value = true
                            updateUIState(WeatherUIState.SuccessWeatherData(true))
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
        viewModelScope.launch(IO + coroutineExceptionHandler) {
            try {
                supervisorScope {
                    val weatherResponse = repositoryImpl.getWeatherOneCallAPI(location)

                    withContext(Main) {
                        hideLoader()
                        weatherResponse?.let {
                            oneCallWeather.value = it
                        }
                    }
                }
            } catch (throwable: Exception) {

                Timber.e(throwable)
                withContext(Main) {

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
                        updateUIState(WeatherUIState.Loading)
                    }
                    WorkInfo.State.SUCCEEDED -> {

                        // Save data in database
                        viewModelScope.launch(IO) {
                            repositoryImpl.insertWeatherData(WeatherData(true))
                        }

                        hideLoader()
                        downloadDone.value = true
                        downloadStatus.value = "Loading finished"
                        workerStatus.value = WorkInfo.State.SUCCEEDED

                        updateUIState(WeatherUIState.Success(OneCallWeatherResponse()))
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
                        updateUIState(WeatherUIState.Error())
                    }
                    WorkInfo.State.BLOCKED -> Timber.e("Worker BLOCKED")
                    WorkInfo.State.CANCELLED -> Timber.e("Worker CANCELLED")
                    else -> {
                        Timber.e("Else branch")
                        updateUIState(WeatherUIState.Error())
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
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"
        private const val WORK_RESULT = "work_result"
    }
}