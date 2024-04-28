package com.riders.thelab.feature.flightaware.ui.airport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
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
class AirportSearchViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////
    // var airports: SnapshotStateList<AirportModel> = mutableStateListOf()

    var airportQuery: String by mutableStateOf("")
        private set

    var airportStateFlow: StateFlow<List<AirportSearchModel>> =
        snapshotFlow { airportQuery }
            .debounce(750)
            .distinctUntilChanged()
            .mapLatest { latestInput ->
                if (latestInput.isEmpty()) {
                    return@mapLatest emptyList()
                }
                updateIsQueryLoading(true)

                Timber.d("departureAirportStateFlow | mapLatest | it: ${latestInput}")
                val airportSearchList = getAirportByOmniSearch(latestInput)
                Timber.d("result: $airportSearchList")

                if (airportSearchList.isNullOrEmpty()) {
                    return@mapLatest emptyList()
                }

                getAirportSearchModelList(airportSearchList)
                    .also { updateIsQueryLoading(false) }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    var isQueryLoading: Boolean by mutableStateOf(false)
        private set


    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)

            updateIsQueryLoading(false)
        }


    fun updateIsQueryLoading(loading: Boolean) {
        this.isQueryLoading = loading
    }

    fun updateAirportQuery(airportQuery: String) {
        this.airportQuery = airportQuery
    }

    fun searchAirports(query: String): List<AirportSearch>? =
        runBlocking(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            Timber.d("searchAirports() | with query: $query")
            repository.searchAirportById(query).airports
        }

    private fun getAirportByOmniSearch(airportID: String): List<AirportSearch>? =
        runBlocking(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            Timber.d("getAirportByOmniSearch() | airport ID: $airportID")
            repository.omniSearchAirport(airportID).airportsOmniData
        }


    fun getAirportById(airportID: String): AirportModel {
        return runBlocking(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            Timber.d("getAirportById() | airport ID: $airportID")
            repository.getAirportById(airportID).toModel()
        }
    }


    private fun getAirportSearchModelList(airportSearchList: List<AirportSearch>): List<AirportSearchModel> =
        runCatching {
            buildSet<AirportSearchModel> {
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