package com.riders.thelab.feature.flightaware.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val repository: IRepository
) : BaseFlightViewModel() {

    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////
    var departureAirportQuery by mutableStateOf("")
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
            .mapLatest { latestInput ->
                if (!hasInternetConnection) {
                    Timber.e("Internet connection not available. Make sure that you are connected to the internet to proceed to the search")
                    return@mapLatest emptyList()
                }
                if (latestInput.isEmpty()) {
                    return@mapLatest emptyList()
                }

                Timber.d("arrivalAirportStateFlow | mapLatest | it: $latestInput")
                val airportSearchList = getAirportByOmniSearch(latestInput)
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