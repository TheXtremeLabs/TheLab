package com.riders.thelab.feature.weather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.text.Editable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.utils.DateTimeUtils
import com.riders.thelab.core.common.utils.LabAddressesUtils
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.model.weather.Weather
import com.riders.thelab.core.domain.usecase.weather.DownloadWeatherDataUseCase
import com.riders.thelab.core.domain.usecase.weather.GetCitiesUseCase
import com.riders.thelab.core.domain.usecase.weather.GetCurrentWeatherUseCase
import com.riders.thelab.core.domain.usecase.weather.InsertWeatherDataUseCase
import com.riders.thelab.core.domain.usecase.weather.SearchCityUseCase
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.weather.data.compose.WeatherUiModel
import com.riders.thelab.feature.weather.data.compose.WeatherUiState
import com.riders.thelab.feature.weather.utils.WeatherUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    labNetworkManager: LabNetworkManager,
    uiRepository: IUiRepository,
    private val downloadWeatherDataUseCase: DownloadWeatherDataUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val insertWeatherDataUseCase: InsertWeatherDataUseCase,
    private val searchCityUseCase: SearchCityUseCase
) : BaseViewModel(uiRepository), DefaultLifecycleObserver {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    val weatherUiState: StateFlow<WeatherUiState>
        // Since Kotlin 2.3.20 : Introducing backing properties
        field = MutableStateFlow<WeatherUiState>(WeatherUiState.None)

    val searchText: StateFlow<String>
        // Since Kotlin 2.3.20 : Introducing backing properties
        field = MutableStateFlow<String>("")

    // Network State
    var hasInternetConnection: StateFlow<Boolean> = labNetworkManager.isConnectedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    var expanded by mutableStateOf(false)
        private set
    var isWeatherMoreDataVisible by mutableStateOf(false)
        private set
    var iconState by mutableStateOf(false)
        private set

    // Suggestions for search
    var suggestions: SnapshotStateList<City> = mutableStateListOf()
        private set


    fun updateWeatherUIState(state: WeatherUiState) {
        weatherUiState.update { state }
    }


    fun updateSearchText(newSearchText: String) {
        this.searchText.value = newSearchText

        if (2 <= newSearchText.length) {
            if (null != mSearchJob && mSearchJob?.isActive == true) {
                mSearchJob?.cancel()
            }

            if (!this.expanded) {
                this.expanded = true
            }

            getCitiesFromDb(this.searchText.value)
        } else {
            this.expanded = false
        }
    }

    fun updateExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    private fun updateSuggestions(suggestions: List<City>) {
        this.suggestions.clear()
        this.suggestions.addAll(suggestions)
    }

    fun updateMoreDataVisibility() {
        this.isWeatherMoreDataVisible = !isWeatherMoreDataVisible
    }

    fun updateIconState(iconState: Boolean) {
        this.iconState = iconState
    }


    //////////////////////////////////////////
    //Live Data
    //////////////////////////////////////////
    private val workerStatus: MutableLiveData<WorkInfo.State> = MutableLiveData()
    private val isWeatherData: MutableLiveData<Boolean> = MutableLiveData()

    fun getWorkerStatus(): LiveData<WorkInfo.State> = workerStatus

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private var mSearchJob: Job? = null

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | Error caught with message: ${throwable.message} (class: ${throwable.javaClass.canonicalName})")
        }
    private val searchCityCoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("searchCityCoroutineExceptionHandler | message: ${throwable.message}")
            handleError(throwable)
        }


    ///////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        viewModelScope.launch { checkWeatherLocalData() }
    }

    ///////////////////////////
    //
    // Class methods
    //
    ///////////////////////////
    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnUpdateSearchCityQuery -> updateSearchText(event.newQuery)
            is UiEvent.OnFetchWeatherForCity -> fetchWeather((event.latitude to event.longitude).toLocation())
            is UiEvent.OnMyLocationClicked -> {}
            is UiEvent.OnRetryRequest -> retry()
            is UiEvent.OnUpdateMoreWeatherDataVisible -> updateMoreDataVisibility()
            is UiEvent.OnUpdateSearchMenuExpanded -> updateExpanded(event.expanded)
            else -> Timber.d("Unhandled event: $event")
        }
    }

    private fun getCitiesFromDb(query: String) {
        Timber.d("getCitiesFromDb() | query: $query")

        mSearchJob =
            viewModelScope.launch(Dispatchers.IO + SupervisorJob() + searchCityCoroutineExceptionHandler) {
                delay(150L)

                if (query.isBlank()) {
                    when (val result = searchCityUseCase(query)) {
                        is Resource.Success -> {
                            result.data.all {
                                Timber.d("getCitiesFromDb() | query.isBlank() | Search: $it")
                                true
                            }
                        }

                        else -> false
                    }
                } else {
                    val sanitizedQuery =
                        sanitizeSearchQuery(Editable.Factory.getInstance().newEditable(query))

                    // Replace % with * here
                    when (val result = searchCityUseCase(sanitizedQuery)) {
                        is Resource.Success -> {
                            Timber.d("getCitiesFromDb() | query.isNotBlank() | list: ${result.data}")
                            handleResults(result.data)
                        }

                        else -> false
                    }
                }
            }

        viewModelScope.launch { mSearchJob?.join() }
    }

    private fun handleResults(cities: List<City>) {
        Timber.d("handleResults() | cities size: ${cities.size}, update suggestions")
        updateSuggestions(cities)
    }

    private fun handleError(t: Throwable) {
        Timber.e("handleError() | Problem while Fetching City (cause: ${t.message})")
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                UIManager.showToast(context, "Problem while fetching city")
            }
        }
    }

    private fun sanitizeSearchQuery(query: Editable?): String {
        if (query == null) {
            return ""
        }

        val sanitizedQuery = query.replace(Regex.fromLiteral("\""), "\"\"")
        Timber.d("sanitizeSearchQuery() | sanitized query: $sanitizedQuery")

//        val newQuery = "*\'$query\'*"
        val newQuery = "'%$query%'"
        Timber.d("sanitizeSearchQuery() | new query: $newQuery")

        return newQuery
    }

    fun retry() {
        Timber.d("Retrying...")
        updateWeatherUIState(WeatherUiState.Loading("Retrying fetching cities..."))

        (mWeakReference?.get() as? WeatherActivity)?.startWork()
    }

    @SuppressLint("NewApi")
    fun getCityNameWithCoordinates(
        activity: WeatherActivity,
        latitude: Double,
        longitude: Double,
        onAddressFetched: (Address) -> Unit
    ) {
        Timber.d("GetCityNameWithCoordinates()")

        val geocoder = Geocoder(activity, Locale.getDefault())

        if (!LabCompatibilityManager.isTiramisu()) {
            LabAddressesUtils.getDeviceAddressLegacy(
                geocoder,
                (latitude to longitude).toLocation()
            )?.let {
                onAddressFetched(it)
            }
        } else {
            LabAddressesUtils.getDeviceAddressAndroid13(
                geocoder,
                (latitude to longitude).toLocation()
            ) { address ->
                address?.let {
                    onAddressFetched(it)
                }
            }
        }
    }

    fun checkWeatherLocalData(callback: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getCitiesUseCase.invoke(null)) {
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        updateWeatherUIState(WeatherUiState.NoDataFound)
                        callback?.invoke(false)
                    } else {
                        if (weatherUiState.value is WeatherUiState.Success) {
                            val model: WeatherUiModel =
                                (weatherUiState.value as WeatherUiState.Success).model.copy(cities = result.data)
                            val finalState: WeatherUiState =
                                (weatherUiState.value as WeatherUiState.Success).copy(model = model)
                            weatherUiState.update { finalState }
                        } else {
                            updateWeatherUIState(WeatherUiState.Success(WeatherUiModel(result.data)))
                        }
                        callback?.invoke(true)
                    }
                }

                else -> {
                    Timber.e("onStart() | Error while fetching cities")
                    callback?.invoke(false)
                }
            }
        }
    }

    fun fetchCities(activity: WeatherActivity) {
        Timber.d("fetchCities()")

        if (!hasInternetConnection.value) {
            Timber.e("fetchCities() | No internet connection detected")
            updateWeatherUIState(WeatherUiState.Error(message = "Please check your internet connection"))
            return
        }

        updateWeatherUIState(WeatherUiState.Loading("Fetching cities..."))

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            checkWeatherLocalData { hasWeatherLocalData ->
                if (!hasWeatherLocalData) {
                    // Only for debug purposes
                    // Use worker to make long job operation in background
                    Timber.e("fetchCities() | Use worker to make long job operation in background...")

                    (mWeakReference?.get() as? WeatherActivity)?.startWork()
                } else {
                    // In this case data already exists in database
                    // Load data then let the the user perform his request
                    Timber.d("Record found in database. Continue...")
                    /* withContext(Dispatchers.Main) {
                         isWeatherData.value = true
                         updateWeatherDataState(WeatherDataState.SuccessWeatherData(true))
                     }*/
                }
            }
        }
    }

    fun fetchWeather(location: Location) {
        Timber.d("fetchWeather()")
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            when (val result = getCurrentWeatherUseCase.invoke(location)) {
                is Resource.Success -> {
                    Timber.d("fetchWeather() | Success")
                    processOneCallResponse(result.data)
                }

                is Resource.Error -> {
                    Timber.e("fetchWeather() | WeatherResponse is null")
                    updateWeatherUIState(WeatherUiState.Error(result.message))
                }
            }
        }
    }

    fun insertWeatherData(cities: List<City>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = insertWeatherDataUseCase.invoke(cities)) {
                is Resource.Success -> {
                    Timber.d("insertWeatherData() | Success")
                }

                is Resource.Error -> {
                    Timber.e("insertWeatherData() | Error")
                    updateWeatherUIState(WeatherUiState.Error(result.message))
                }
            }
        }
    }

    fun getCitiesSync(): List<City> = runBlocking(Dispatchers.IO) {
        return@runBlocking when (val result = getCitiesUseCase.invoke(null)) {
            is Resource.Success -> result.data
            else -> emptyList()
        }
    }

    private suspend fun processOneCallResponse(weatherResponse: Weather) {
        runCatching {
            val weatherModel: Weather = weatherResponse.apply {
                (mWeakReference?.get() as? WeatherActivity)?.let { activity ->
                    getCityNameWithCoordinates(
                        activity = activity,
                        latitude = weatherResponse.latitude,
                        longitude = weatherResponse.longitude,
                        onAddressFetched = { address ->
                            this.address = address
                        }
                    )
                } ?: run {
                    Timber.e("fetchWeather() | Activity object is null")
                }

                this.sunriseAsString = DateTimeUtils.formatMillisToTimeHoursMinutes(
                    weatherResponse.timezone!!,
                    weatherResponse.sunrise
                )
                this.sunsetAsString = DateTimeUtils.formatMillisToTimeHoursMinutes(
                    weatherResponse.timezone!!,
                    weatherResponse.sunset
                )

                this.weatherIconUrl =
                    WeatherUtils.getWeatherIconFromApi(weatherIconUrl.toString())

                hourlyWeather?.let {
                    getMaxMinTemperature(it).also {
                        this.temperature?.min = it.first
                        this.temperature?.max = it.second
                    }
                }

                dailyWeather?.let {
                    it.forEach { dailyItem ->
                        dailyItem.weatherIconUrl =
                            WeatherUtils.getWeatherIconFromApi(dailyItem.weatherIconUrl.toString())
                    }
                }
                hourlyWeather?.let {
                    it.forEach { hourlyItem ->
                        hourlyItem.weatherIconUrl =
                            WeatherUtils.getWeatherIconFromApi(hourlyItem.weatherIconUrl.toString())
                    }
                }
            }

            withContext(Dispatchers.Main) {
                if (weatherUiState.value !is WeatherUiState.Success) {
                    updateWeatherUIState(
                        WeatherUiState.Success(
                            WeatherUiModel(
                                cities = getCitiesSync(),
                                weather = weatherModel
                            )
                        )
                    )
                } else {
                    val state = weatherUiState.value as WeatherUiState.Success
                    val model = state.model.copy(weather = weatherModel)
                    weatherUiState.update {
                        (weatherUiState.value as WeatherUiState.Success).copy(model = model)
                    }
                }
            }
        }
            .onFailure { exception: Throwable -> Timber.e("processOneCallResponse() | onFailure | Error caught with message : ${exception.message} (class : ${exception.javaClass.canonicalName})") }
            .onSuccess { Timber.e("processOneCallResponse() | onSuccess") }
    }

    private fun getMaxMinTemperature(hourlyWeather: List<Weather>): Pair<Double, Double> {
        Timber.d("getMaxMinTemperature() | hourlyWeather: $hourlyWeather")
        var minStoredTemperature: Double = hourlyWeather[0].temperature?.temperature ?: 0.0
        var maxStoredTemperature: Double = hourlyWeather[0].temperature?.temperature ?: 0.0

        hourlyWeather.forEach { temp ->
            if (minStoredTemperature >= temp.temperature?.temperature!!) {
                minStoredTemperature = temp.temperature?.temperature!!
            }
        }

        hourlyWeather.forEach { temp ->
            if (temp.temperature?.temperature!! >= maxStoredTemperature) {
                maxStoredTemperature = temp.temperature?.temperature!!
            }
        }

        return minStoredTemperature to maxStoredTemperature
    }


    companion object {
        private val TAG = WeatherViewModel::class.java.simpleName

        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"
        private const val WORK_RESULT = "work_result"
    }
}