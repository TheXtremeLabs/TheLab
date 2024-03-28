package com.riders.thelab.feature.flightaware.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.BuildConfig
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.data.local.model.flight.toModel
import com.riders.thelab.core.data.remote.dto.flight.AirportSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val repository: IRepository
) : BaseFlightViewModel() {
    var isOptionSelectedByUser: Boolean = false

    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////

    var flightNumber: String by mutableStateOf("")
        private set

    var departureAirportQuery: String by mutableStateOf("")
        private set

    var departureAirportStateFlow: StateFlow<List<AirportSearchModel>> =
        snapshotFlow { departureAirportQuery }
            .debounce(750)
            .distinctUntilChanged()
            .mapLatest { latestDepartureAirportInput ->
                if (!hasInternetConnection) {
                    Timber.e("Internet connection not available. Make sure that you are connected to the internet to proceed to the search")
                    return@mapLatest emptyList()
                }
                if (latestDepartureAirportInput.isEmpty()) {
                    Timber.e("Input is empty. Cannot execute the query.")
                    return@mapLatest emptyList()
                }
                if (isOptionSelectedByUser) {
                    Timber.w("No need to perform action. user selection")
                    isOptionSelectedByUser = false
                    return@mapLatest emptyList()
                }

                Timber.d("departureAirportStateFlow | mapLatest | it: $latestDepartureAirportInput")
                val airportSearchList = getAirportByOmniSearch(latestDepartureAirportInput)
                Timber.d("result: $airportSearchList")

                if (airportSearchList.isNullOrEmpty()) {
                    return@mapLatest emptyList()
                }

                getAirportSearchModelList(airportSearchList)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    var arrivalAirportQuery: String by mutableStateOf("")
        private set
    var arrivalAirportStateFlow: StateFlow<List<AirportSearchModel>> =
        snapshotFlow { arrivalAirportQuery }
            .debounce(750)
            .distinctUntilChanged()
            .mapLatest { latestArrivalAirportInput ->
                if (!hasInternetConnection) {
                    Timber.e("Internet connection not available. Make sure that you are connected to the internet to proceed to the search")
                    return@mapLatest emptyList()
                }
                if (latestArrivalAirportInput.isEmpty()) {
                    Timber.e("Input is empty. Cannot execute the query.")
                    return@mapLatest emptyList()
                }
                if (isOptionSelectedByUser) {
                    Timber.w("No need to perform action. user selection")
                    isOptionSelectedByUser = false
                    return@mapLatest emptyList()
                }

                Timber.d("arrivalAirportStateFlow | mapLatest | it: $latestArrivalAirportInput")
                val airportSearchList = getAirportByOmniSearch(latestArrivalAirportInput)
                Timber.d("result: $airportSearchList")

                if (airportSearchList.isNullOrEmpty()) {
                    return@mapLatest emptyList()
                }

                getAirportSearchModelList(airportSearchList)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun updateFlightNumber(newFlightNumber: String) {
        this.flightNumber = newFlightNumber
    }

    fun updateDepartureAirportQuery(newAirportQuery: String) {
        this.departureAirportQuery = newAirportQuery
    }

    fun updateArrivalAirportQuery(newAirportQuery: String) {
        this.arrivalAirportQuery = newAirportQuery
    }

    /////////////////////////////////////
    // Coroutine
    /////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e("CoroutineExceptionHandler | Error caught with message: ${throwable.message}")
        }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun searchFlightByFlightNumber() {
        Timber.d("searchFlightByFlightNumber()")

        if (this.flightNumber.isEmpty()) {
            Timber.e("FLight number is null. Cannot perform REST call")
            return
        }

        val query = NotBlankString.create(if (BuildConfig.DEBUG) "AAL306" else flightNumber)
        searchFlight(query)
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun searchFlightByRoute(departureAirportCode: String, arrivalAirportCode: String) {
        Timber.d("searchFlightByRoute() | departureAirportCode: $departureAirportCode, arrivalAirportCode: $arrivalAirportCode")

        if (departureAirportCode.isEmpty()) {
            Timber.e("Departure Airport query is null. Cannot perform REST call")
            return
        }
        if (arrivalAirportCode.isEmpty()) {
            Timber.e("Arrival Airport query is null. Cannot perform REST call")
            return
        }

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            repository.searchFlightByRoute(
                NotBlankString.create(departureAirportCode),
                NotBlankString.create(arrivalAirportCode)
            )
        }
    }

    private fun searchFlight(query: NotBlankString) {
        Timber.d("searchFlight()")
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            repository.searchFlight(query = query)
        }
    }

    private fun getAirportByOmniSearch(airportID: String): List<AirportSearch>? =
        runBlocking(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            Timber.d("getAirportByOmniSearch() | airport ID: $airportID")
            repository.omniSearchAirport(airportID).airportsOmniData
        }


    fun getAirportById(airportID: String): AirportModel =
        runBlocking(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            Timber.d("getAirportById() | airport ID: $airportID")
            repository.getAirportById(airportID).toModel()
        }


    private fun getAirportSearchModelList(airportSearchList: List<AirportSearch>): List<AirportSearchModel> =
        runCatching {
            buildSet {
                airportSearchList.forEach {
                    add(it.toModel())
                }
            }.toList()
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("getAirportSearchModelList() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.simpleName})")
            }
            .getOrElse {
                emptyList()
            }

}