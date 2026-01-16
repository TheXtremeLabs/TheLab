package com.riders.thelab.feature.flightaware.ui.airport

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.flight.toAirportModel
import com.riders.thelab.core.data.remote.dto.flight.Airport
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.flightaware.data.local.model.compose.AirportDetailUiState
import com.riders.thelab.feature.flightaware.data.local.model.compose.ArrivalsUiState
import com.riders.thelab.feature.flightaware.data.local.model.compose.DeparturesUiState
import com.riders.thelab.feature.flightaware.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class AirportSearchDetailViewModel @Inject constructor(
    private val repository: IRepository,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository), CoroutineScope, DefaultLifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    // --- Variables
    private var airportID: NotBlankString? = null


    // --- Compose states
    private var _airportDetailUiState: MutableStateFlow<AirportDetailUiState> =
        MutableStateFlow(AirportDetailUiState.Loading)
    val airportDetailUiState: StateFlow<AirportDetailUiState> = _airportDetailUiState

    private var _departureFlights: MutableStateFlow<DeparturesUiState> =
        MutableStateFlow(DeparturesUiState.Loading)
    val departureFlights: StateFlow<DeparturesUiState> = _departureFlights

    private var _arrivalFlights: MutableStateFlow<ArrivalsUiState> =
        MutableStateFlow(ArrivalsUiState.Loading)
    val arrivalFlights: StateFlow<ArrivalsUiState> = _arrivalFlights

    var isFlightsFetched: Boolean by mutableStateOf(false)

    private fun updateAirportDetailUiState(newState: AirportDetailUiState) {
        _airportDetailUiState.update { newState }
    }

    private fun updateAirportDeparturesFlights(newState: DeparturesUiState) {
        _departureFlights.update { newState }
    }

    private fun updateAirportArrivalFlights(newState: ArrivalsUiState) {
        _arrivalFlights.update { newState }
    }


    private fun updateIsFlightsFetched(fetched: Boolean) {
        this.isFlightsFetched = fetched
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")
        }


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        airportID?.let {
            if (it.toString() != "N/A") {
                getAirportById()
            }
        } ?: run {
            Timber.e("airportID object is null")
        }
    }

    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")
        runCatching {
            intent
                .getStringExtra(Constants.EXTRA_AIRPORT_ID)
                ?.toNotBlankString()
                ?.getOrThrow()
                ?.also {
                    Timber.d("airportID: $it")
                    airportID = it
                }
                ?: "N/A".toNotBlankString().getOrThrow()
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("runCatching | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
            }
    }

    fun getAirportById() {
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            repository
                .getAirportById(airportID.toString())
                .also { airportResponse: Airport ->
                    Timber.d("getAirportById() | airportResponse: $airportResponse")
                    updateAirportDetailUiState(AirportDetailUiState.Success(airportResponse.toAirportModel()))
                }
        }
    }

    fun fetchFlights() {
        Timber.d("fetchFlights()")

        if (airportID.toString().trim().isBlank() || "N/A".equals(airportID.toString(), true)) {
            Timber.e("airportID is null or empty")
            return
        }

        viewModelScope.launch(coroutineContext + SupervisorJob() + coroutineExceptionHandler) {
            val airportFlightsResponse = repository.getAirportFlightsById(airportID.toString())

            Timber.d("getAirportFlightsById() | result: $airportFlightsResponse")
            updateAirportDeparturesFlights(newState = DeparturesUiState.Success(departures = airportFlightsResponse.departures))
            updateAirportArrivalFlights(newState = ArrivalsUiState.Success(arrivals = airportFlightsResponse.arrivals))

            updateIsFlightsFetched(true)
        }
    }
}