package com.riders.thelab.feature.flightaware.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.data.local.model.flight.toSearchFlightModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.flightaware.base.FlightSearchViewModel
import com.riders.thelab.feature.flightaware.data.local.model.SearchFlightType
import com.riders.thelab.feature.flightaware.data.local.model.compose.SearchFlightsUiState
import com.riders.thelab.feature.flightaware.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchFlightViewModel @Inject constructor(
    labNetworkManager: LabNetworkManager,
    private val repository: IRepository,
    uiRepository: IUiRepository
) : FlightSearchViewModel(labNetworkManager, repository, uiRepository) {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    var searchType: SearchFlightType? = null
    var flightNumber: NotBlankString? = null
    var flightRoute: Pair<NotBlankString, NotBlankString>? = null
    var flightJson: String? = null


    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _searchFlightUiState: MutableStateFlow<SearchFlightsUiState> =
        MutableStateFlow(SearchFlightsUiState.Loading(searchType = SearchFlightType.UNSPECIFIED))
    var searchFlightUiState: StateFlow<SearchFlightsUiState> = _searchFlightUiState

    private fun updateUiState(newState: SearchFlightsUiState) {
        this._searchFlightUiState.value = newState
    }


    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | error caught with message: ${throwable.message} (class: ${throwable.javaClass.canonicalName})")

            updateUiState(
                SearchFlightsUiState.Error(
                    message = "Error occurred while getting value".toNotBlankString().getOrThrow(),
                    throwable = throwable
                )
            )
        }

    /////////////////////////////////////
    //
    // OVERRIDE METHODS
    //
    /////////////////////////////////////
    override fun onCleared() {
        super.onCleared()
    }


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")

        flightNumber?.let { searchFlightByFlightNumber() }
        flightRoute?.let { searchFlightByRoute() }
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    @SuppressLint("NewApi")
    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")

        runCatching {
            // Try to get bundle values
            intent.extras?.let { bundle ->

                searchType = if (LabCompatibilityManager.isTiramisu()) {
                    bundle.getSerializable(
                        Constants.EXTRA_SEARCH_TYPE,
                        SearchFlightType::class.java
                    )
                } else {
                    bundle.getSerializable(Constants.EXTRA_SEARCH_TYPE) as SearchFlightType
                }

                flightNumber = bundle.getString(Constants.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER)
                    ?.toNotBlankString()
                    ?.getOrThrow()

                val flightRoutePair: Pair<String, String>? =
                    if (LabCompatibilityManager.isTiramisu()) {
                        bundle.getSerializable(
                            Constants.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE,
                            Pair::class.java
                        ) as? Pair<String, String>
                    } else {
                        bundle.getSerializable(Constants.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE) as? Pair<String, String>
                    }

                flightRoutePair?.let {
                    flightRoute = Pair(
                        it.first.toNotBlankString().getOrThrow(),
                        it.second.toNotBlankString().getOrThrow()
                    )
                }

                flightJson = bundle.getString(Constants.EXTRA_FLIGHT)

                flightJson?.let {
                    val extraItem: List<SearchFlightModel>? =
                        Json.decodeFromString<List<SearchFlightModel>?>(it)

                    when (searchType) {
                        SearchFlightType.NUMBER -> {
                            extraItem?.let { flights: List<SearchFlightModel> ->
                                // Log
                                Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | item length: ${flights.size}")
                                updateUiState(SearchFlightsUiState.Success(flights))
                            }
                                ?: run { Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | Extra item object is null") }
                        }

                        SearchFlightType.ROUTE -> {
                            extraItem?.let { flights: List<SearchFlightModel> ->
                                // Log
                                Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | item length: ${flights.size}")
                                updateUiState(SearchFlightsUiState.Success(flights))
                            }
                                ?: run {
                                    Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | Extra item object is null")
                                    updateUiState(
                                        SearchFlightsUiState.Error(
                                            "Error occurred while getting value".toNotBlankString()
                                                .getOrThrow()
                                        )
                                    )
                                }
                        }

                        else -> {
                            Timber.e("Unknown search type: $searchType")
                            updateUiState(
                                SearchFlightsUiState.Error(
                                    "Error occurred while getting value"
                                        .toNotBlankString()
                                        .getOrThrow()
                                )
                            )
                        }
                    }
                } ?: run {
                    Timber.e("getBundle() | flightString is null")
                    updateUiState(
                        SearchFlightsUiState.Error(
                            "Error occurred while getting value".toNotBlankString().getOrThrow()
                        )
                    )
                }

            } ?: run {
                Timber.e("Intent extras are null")
                updateUiState(
                    SearchFlightsUiState.Error(
                        "Error occurred while getting value".toNotBlankString().getOrThrow()
                    )
                )
            }
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("getBundle() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
                updateUiState(
                    SearchFlightsUiState.Error(
                        message = "Error occurred while getting value".toNotBlankString()
                            .getOrThrow(),
                        throwable = it
                    )
                )
            }
            .onSuccess {
                Timber.d("getBundle() | onSuccess")
            }
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun searchFlightByFlightNumber() {
        Timber.d("searchFlightByFlightNumber()")

        updateUiState(
            newState = SearchFlightsUiState.Loading(
                searchType = SearchFlightType.NUMBER,
                message = "Searching flights for $flightNumber}".toNotBlankString().getOrThrow()
            )
        )

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val flights = super.searchFlightByFlightNumber(flightNumber!!)

            if (flights.isNullOrEmpty()) {
                Timber.e("No results found for search query $flightNumber")
                updateUiState(
                    newState = SearchFlightsUiState.Error(
                        message = "No results found for search query $flightNumber"
                            .toNotBlankString()
                            .getOrThrow()
                    )
                )
                return@launch
            }

            withContext(Dispatchers.Main) {
                updateUiState(newState = SearchFlightsUiState.Success(flights = flights.map { it.toSearchFlightModel() }))
            }
        }
    }

    fun searchFlightByRoute() {
        val departureAirportCode: NotBlankString? = flightRoute?.first
        val arrivalAirportCode: NotBlankString? = flightRoute?.second

        if (null == departureAirportCode || departureAirportCode.toString().isEmpty()) {
            Timber.e("Departure Airport query is null. Cannot perform REST call")
            return
        }
        if (null == arrivalAirportCode || arrivalAirportCode.toString().isEmpty()) {
            Timber.e("Arrival Airport query is null. Cannot perform REST call")
            return
        }

        Timber.d("searchFlightByRoute() | departureAirportCode: $departureAirportCode, arrivalAirportCode: $arrivalAirportCode")

        updateUiState(
            SearchFlightsUiState.Loading(
                searchType = SearchFlightType.ROUTE,
                message = "Searching flights for $departureAirportCode to $arrivalAirportCode"
                    .toNotBlankString()
                    .getOrThrow()
            )
        )

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val segments: List<SearchFlightModel>? = super.searchFlightByRoute(
                departureAirportCode,
                arrivalAirportCode
            )
                .also { Timber.d("flights total : ${it?.size}") }

            if (segments.isNullOrEmpty()) {
                Timber.e("No results found for search query $departureAirportCode to $arrivalAirportCode")
                updateUiState(
                    newState = SearchFlightsUiState.Error(
                        message = "No results found for search query $departureAirportCode to $arrivalAirportCode".toNotBlankString()
                            .getOrThrow()
                    )
                )
                return@launch
            }

            withContext(Dispatchers.Main) {
                updateUiState(newState = SearchFlightsUiState.Success(flights = segments))
            }
        }
    }
}