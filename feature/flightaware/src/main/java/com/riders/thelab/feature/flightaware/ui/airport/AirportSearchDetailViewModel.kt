package com.riders.thelab.feature.flightaware.ui.airport

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.data.local.model.flight.toModel
import com.riders.thelab.core.data.remote.dto.flight.Arrivals
import com.riders.thelab.core.data.remote.dto.flight.Departures
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AirportSearchDetailViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel(), DefaultLifecycleObserver {

    var airportID: NotBlankString? by mutableStateOf(null)
        private set
    var airportModel: AirportModel? by mutableStateOf(null)
        private set
    var departureFlights: List<Departures>? by mutableStateOf(null)
        private set
    var arrivalFlights: List<Arrivals>? by mutableStateOf(null)
        private set
    var isFlightsFetched: Boolean by mutableStateOf(false)
        private set

    private fun updateAirportID(newAirportId: NotBlankString) {
        this.airportID = newAirportId
    }

    private fun updateAirportModel(newAirportModel: AirportModel) {
        this.airportModel = newAirportModel
    }

    private fun updateAirportDeparturesFlights(departures: List<Departures>) {
        this.departureFlights = departures
    }

    private fun updateAirportArrivalFlights(arrivalFlights: List<Arrivals>) {
        this.arrivalFlights = arrivalFlights
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
    override fun onCleared() {
        super.onCleared()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        airportID?.let {
            if (it.toString() != "N/A") {
                viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                    val airportResponse = repository.getAirportById(it.toString())

                    Timber.d("getAirportById() | result: $airportResponse")
                    updateAirportModel(airportResponse.toModel())
                }
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
            airportID = intent.getStringExtra(AirportSearchDetailActivity.EXTRA_AIRPORT_ID)
                ?.toNotBlankString()?.getOrThrow() ?: "N/A".toNotBlankString().getOrThrow()
            Timber.d("airportID: $airportID")
            airportID?.let { updateAirportID(it) }
        }.onFailure {
            it.printStackTrace()
            Timber.e("runCatching | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.simpleName})")
        }
    }

    fun fetchFlights() {
        Timber.d("fetchFlights()")

        airportID?.let {
            if (it.toString() != "N/A") {
                viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                    val airportFlightsResponse = repository.getAirportFlightsById(it.toString())

                    Timber.d("getAirportFlightsById() | result: $airportFlightsResponse")
                    updateAirportDeparturesFlights(airportFlightsResponse.departures)
                    updateAirportArrivalFlights(airportFlightsResponse.arrivals)

                    updateIsFlightsFetched(true)
                }
            }
        } ?: run {
            Timber.e("airportID object is null")
        }

    }
}