package com.riders.thelab.feature.weather.ui

import android.annotation.SuppressLint
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
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.riders.thelab.core.common.network.LabNetworkManagerNewAPI
import com.riders.thelab.core.common.utils.LabAddressesUtils
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.WeatherCityUIState
import com.riders.thelab.core.data.local.model.compose.WeatherUIState
import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.data.local.model.weather.WeatherData
import com.riders.thelab.core.data.remote.dto.weather.CurrentWeather
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.data.utils.WeatherSearchManager
import com.riders.thelab.core.ui.data.SnackBarType
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.weather.core.worker.WeatherDownloadWorker
import com.riders.thelab.feature.weather.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: IRepository,
    private val weatherSearchManager: WeatherSearchManager
) : ViewModel() {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var mSearchJob: Job? = null


    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _weatherUiState = MutableStateFlow<WeatherUIState>(WeatherUIState.Loading)
    val weatherUiState = _weatherUiState
    private var _weatherCityUiState = MutableStateFlow<WeatherCityUIState>(WeatherCityUIState.None)
    val weatherCityUiState = _weatherCityUiState
    fun updateUIState(state: WeatherUIState) {
        _weatherUiState.value = state
    }

    private fun updateWeatherCityUIState(cityState: WeatherCityUIState) {
        _weatherCityUiState.value = cityState
    }

    var searchText by mutableStateOf("")
        private set

    var expanded by mutableStateOf(false)
        private set
    var isWeatherMoreDataVisible by mutableStateOf(false)
        private set
    var iconState by mutableStateOf(false)
        private set

    // Suggestions for search
    var suggestions by mutableStateOf(emptyList<CityModel>())
        private set

    var weatherAddress: Address? by mutableStateOf(null)
        private set
    var cityMaxTemp by mutableStateOf("")
        private set
    var cityMinTemp by mutableStateOf("")
        private set

    fun updateSearchText(searchQuery: String) {
        this.searchText = searchQuery

        if (2 <= searchText.length) {
            this.expanded = true
            // getCitiesFromDb(searchText)

            mSearchJob?.cancel()
            mSearchJob = viewModelScope.launch {
                delay(500L)
                val results = weatherSearchManager.searchCities(searchText)

                results.forEach {
                    Timber.d("updateSearchText() | appSearch element: $it")
                }
            }

        } else {
            this.expanded = false
        }
    }

    fun updateExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    private fun updateCityMaxTemp(newTemperature: Double) {
        this.cityMaxTemp = "${newTemperature.toInt()}"
    }

    private fun updateCityMinTemp(newTemperature: Double) {
        this.cityMinTemp = "${newTemperature.toInt()}"
    }

    private fun updateWeatherAddress(newAddress: Address) {
        this.weatherAddress = newAddress
    }

    fun updateMoreDataVisibility() {
        this.isWeatherMoreDataVisible = !isWeatherMoreDataVisible
    }

    fun updateIconState(iconState: Boolean) {
        this.iconState = iconState
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


    init {
        Timber.i("$TAG | init method")

        viewModelScope.launch(coroutineExceptionHandler) {
            weatherSearchManager.init()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    //////////////////////////////////////////
    //Live Data
    //////////////////////////////////////////
    private val workerStatus: MutableLiveData<WorkInfo.State> = MutableLiveData()
    private val isWeatherData: MutableLiveData<Boolean> = MutableLiveData()

    fun getWorkerStatus(): LiveData<WorkInfo.State> = workerStatus


    ///////////////////////////
    //
    // Class methods
    //
    ///////////////////////////
    private fun getCitiesFromDb(query: String) {
        Timber.d("getCitiesFromDb() | query: $query")

        if (null != searchDbJob && searchDbJob?.isActive == true) {
            searchDbJob?.cancel()
        }

        searchDbJob =
            viewModelScope.launch(Dispatchers.IO /*+ SupervisorJob() + coroutineExceptionHandler*/) {

                try {
                    val cursor = repository.getCitiesCursor(searchText)

                    withContext(Dispatchers.Main) {
                        handleResults(cursor)
                    }
                } catch (exception: Exception) {
                    handleError(exception)
                }
            }
        // viewModelScope.launch { searchDbJob?.join() }
    }

    private fun handleResults(cursor: Cursor) {
        Timber.d("handleResults() | available cursor's column: ${cursor.columnNames}")

        if (suggestions.isNotEmpty()) suggestions = mutableListOf()

        val tempList = mutableListOf<CityModel>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) { // If you use c.moveToNext() here, you will bypass the first row, which is WRONG

                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val country = cursor.getString(cursor.getColumnIndexOrThrow("country"))

                Timber.d("handleResults() | name: $name, country: $country")

                tempList.add(CityModel(cursor))

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

    fun retry() {
        Timber.d("Retrying...")
        updateUIState(WeatherUIState.Loading)
    }

    @SuppressLint("NewApi")
    fun getCityNameWithCoordinates(
        activity: WeatherActivity,
        latitude: Double,
        longitude: Double
    ) {
        Timber.d("GetCityNameWithCoordinates()")

        val geocoder = Geocoder(activity, Locale.getDefault())

        if (!LabCompatibilityManager.isTiramisu()) {
            LabAddressesUtils.getDeviceAddressLegacy(
                geocoder,
                (latitude to longitude).toLocation()
            )?.let { updateWeatherAddress(it) }
        } else {
            LabAddressesUtils.getDeviceAddressAndroid13(
                geocoder,
                (latitude to longitude).toLocation()
            ) { address ->
                address?.let { updateWeatherAddress(it) }
            }
        }
    }

    fun fetchCities(activity: WeatherActivity) {
        Timber.d("fetchCities()")
        if (!LabNetworkManagerNewAPI.getInstance(activity).isOnline()) {
            updateUIState(WeatherUIState.Error())
            return
        } else {
            updateUIState(WeatherUIState.Loading)

            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                try {
                    // First step
                    // Call repository to check if there is data in database
                    val weatherData = repository.getWeatherData()

                    if (null == weatherData || !weatherData.isWeatherData) {

                        // In this case record's return is null
                        // then we have to call our Worker to perform
                        // the web service call to retrieve data from api
                        Timber.e("List is empty. No Record found in database")

                        // Only for debug purposes
                        // Use worker to make long job operation in background
                        Timber.e("Use worker to make long job operation in background...")
                        
                        startWork(activity)
                    } else {
                        // In this case data already exists in database
                        // Load data then let the the user perform his request
                        Timber.d("Record found in database. Continue...")
                        withContext(Dispatchers.Main) {
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
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            try {
                val weatherResponse = repository.getWeatherOneCallAPI(location)

                withContext(Dispatchers.Main) {
                    weatherResponse?.let {
                        updateWeatherCityUIState(WeatherCityUIState.Success(it))
                    }
                }
            } catch (throwable: Exception) {
                Timber.e(throwable)
                withContext(Dispatchers.Main) {
                    updateUIState(WeatherUIState.Error())
                }
            }
        }
    }

    fun getMaxMinTemperature(hourlyWeather: List<CurrentWeather>) {
        Timber.d("getMaxMinTemperature() | hourlyWeather: $hourlyWeather")
        var minStoredTemperature: Double = hourlyWeather[0].temperature
        var maxStoredTemperature: Double = hourlyWeather[0].temperature

        hourlyWeather.forEach { temp ->
            if (minStoredTemperature >= temp.temperature) {
                minStoredTemperature = temp.temperature
            }
        }

        hourlyWeather.forEach { temp ->
            if (temp.temperature >= maxStoredTemperature) {
                maxStoredTemperature = temp.temperature
            }
        }
        updateCityMaxTemp(maxStoredTemperature)
        updateCityMinTemp(minStoredTemperature)
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
            .observe(activity) { workInfos: WorkInfo ->
                when (workInfos.state) {
                    WorkInfo.State.ENQUEUED -> Timber.d("Worker ENQUEUED")
                    WorkInfo.State.RUNNING -> {
                        Timber.d("Worker RUNNING")
                        workerStatus.value = WorkInfo.State.RUNNING
                        updateUIState(WeatherUIState.Loading)
                    }

                    WorkInfo.State.SUCCEEDED -> {

                        // Save data in database
                        viewModelScope.launch(Dispatchers.IO) {
                            repository.insertWeatherData(WeatherData(true))
                        }

                        updateUIState(WeatherUIState.Success(OneCallWeatherResponse()))
                        workerStatus.value = WorkInfo.State.SUCCEEDED
                    }

                    WorkInfo.State.FAILED -> {
                        Timber.e("Worker FAILED")
                        workerStatus.value = WorkInfo.State.FAILED

                        activity.runOnUiThread {
                            UIManager.showActionInSnackBar(
                                activity,
                                "Worker FAILED",
                                SnackBarType.ALERT,
                                "",
                                null
                            )
                        }

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
        private val TAG = WeatherViewModel::class.java.simpleName

        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"
        private const val WORK_RESULT = "work_result"
    }
}