package com.riders.thelab.feature.flightaware.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import com.riders.thelab.core.data.local.model.flight.OperatorModel
import com.riders.thelab.core.data.local.model.flight.toModel
import com.riders.thelab.feature.flightaware.BuildConfig
import com.riders.thelab.feature.flightaware.ui.airport.PreviewProviderAirportSearch
import com.riders.thelab.feature.flightaware.viewmodel.FlightSearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(
    private val repository: IRepository
) : FlightSearchViewModel(repository) {


    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////
    var searchPageIndex by mutableIntStateOf(0)
        private set

    var airports: SnapshotStateList<AirportModel> = mutableStateListOf()
    var operators: SnapshotStateList<OperatorModel> = mutableStateListOf()


    // Departures
    var departureDropdownExpanded by mutableStateOf(false)
        private set
    private var departureAirportOptionSelected: AirportSearchModel? by mutableStateOf(null)
        private set

    // Arrival
    var arrivalDropdownExpanded by mutableStateOf(false)
        private set
    private var arrivalAirportOptionSelected: AirportSearchModel? by mutableStateOf(null)
        private set

    var currentAirport: AirportModel? by mutableStateOf(null)
        private set

    var currentOperator: OperatorModel? by mutableStateOf(null)
        private set

    private fun updateSearchIndex(newIndex: Int) {
        this.searchPageIndex = newIndex
    }

    fun updateDepartureExpanded(isExpanded: Boolean) {
        Timber.d("updateDepartureExpanded() | isExpanded: $isExpanded")
        this.departureDropdownExpanded = isExpanded
    }

    fun updateArrivalExpanded(isExpanded: Boolean) {
        Timber.d("updateArrivalExpanded() | isExpanded: $isExpanded")
        this.arrivalDropdownExpanded = isExpanded
    }

    fun updateDepartureAirportOption(newAirportOption: AirportSearchModel) {
        Timber.d("updateDepartureAirportOption() | newAirportOption: $newAirportOption")
        this.departureAirportOptionSelected = newAirportOption
    }

    fun updateArrivalAirportOption(newAirportOption: AirportSearchModel) {
        Timber.d("updateArrivalAirportOption() | newAirportOption: $newAirportOption")
        this.arrivalAirportOptionSelected = newAirportOption
    }


    private fun updateAirportList(newList: List<AirportModel>) {
        this.airports.addAll(newList)
    }

    private fun updateOperators(newList: List<OperatorModel>) {
        this.operators.addAll(newList)
    }

    private fun updateAirport(newAirport: AirportModel) {
        this.currentAirport = newAirport
    }

    private fun updateOperator(newOperator: OperatorModel) {
        this.currentOperator = newOperator
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
    // OVERRIDE
    //
    ///////////////////////////////
    init {
        if (BuildConfig.DEBUG) {
            updateDepartureAirportOption(PreviewProviderAirportSearch().values.toList()[0])
            updateArrivalAirportOption(PreviewProviderAirportSearch().values.toList()[1])
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun onEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnSearchCategorySelected -> updateSearchIndex(uiEvent.pageIndex)
            is UiEvent.OnDepartureExpanded -> updateDepartureExpanded(uiEvent.expanded)
            is UiEvent.OnDepartureOptionsSelected -> {
                isOptionSelectedByUser = true
                updateDepartureAirportOption(uiEvent.departureAirport)
                super.updateDepartureAirportQuery(uiEvent.departureAirport.name.toString())
            }

            is UiEvent.OnArrivalExpanded -> updateArrivalExpanded(uiEvent.expanded)
            is UiEvent.OnArrivalOptionsSelected -> {
                isOptionSelectedByUser = true
                updateArrivalAirportOption(uiEvent.arrivalAirport)
                super.updateArrivalAirportQuery(uiEvent.arrivalAirport.name.toString())
            }

            is UiEvent.OnSearchFlightByRoute -> {
                departureAirportOptionSelected?.let { departure ->
                    arrivalAirportOptionSelected?.let { arrival ->
                        super.searchFlightByRoute(
                            uiEvent.context,
                            departure.icaoCode!!,
                            arrival.icaoCode!!
                        )
                    } ?: run {
                        Timber.e("onEvent() | onSearchFlightByRoute | arrivalAirportOptionSelected is null")
                    }
                } ?: run {
                    Timber.e("onEvent() | onSearchFlightByRoute | departureAirportOptionSelected is null")
                }
            }

            else -> {
                Timber.e("onEvent() | else branch | uiEvent: $uiEvent")
                super.onEvent(uiEvent = uiEvent, activity = null)
            }
        }
    }

    fun getAirports() {
        Timber.d("getAirports()")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val firstAirports = repository.getAirports(maxPages = 3)
            Timber.d("First Airports Next link: ${firstAirports.links.getNext()}")

            val secondAirports =
                repository.getAirports(maxPages = 3, cursor = firstAirports.links.getNext())
            val thirdAirports =
                repository.getAirports(maxPages = 3, cursor = secondAirports.links.getNext())

            val totalAirports =
                firstAirports.airports + secondAirports.airports + thirdAirports.airports

            Timber.d("Total Airports: ${totalAirports.size}")
            updateAirportList(totalAirports.map { it.toModel() })
        }
    }

    fun getAirport(airportID: String = "01LA") {
        Timber.d("getAirport() | id: $airportID")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val airport = repository.getAirportById(airportID).toModel()
            Timber.d("airport: $airport")
            updateAirport(airport)
        }
    }

    fun getOperators() {
        Timber.d("getOperators()")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val firstOperators = repository.getOperators(maxPages = 3)
            Timber.d("First Operators Next link: ${firstOperators.links.getNext()}")

            val secondOperators =
                repository.getOperators(maxPages = 3, cursor = firstOperators.links.getNext())
            val thirdOperators =
                repository.getOperators(maxPages = 3, cursor = secondOperators.links.getNext())

            val totalOperators =
                firstOperators.operators + secondOperators.operators + thirdOperators.operators

            Timber.d("Total Operators: ${totalOperators.size}")
            updateOperators(totalOperators.map { it.toModel() })
        }
    }

    fun getOperator(operatorID: String = "AFR") {
        Timber.d("getOperator() | id: $operatorID")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val operator = repository.getOperatorById(operatorID).toModel()
            Timber.d("operator: $operator")
            updateOperator(operator)
        }
    }
}