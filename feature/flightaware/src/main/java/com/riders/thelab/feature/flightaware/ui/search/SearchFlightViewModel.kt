package com.riders.thelab.feature.flightaware.ui.search

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.data.local.model.compose.SearchFlightUiState
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.feature.flightaware.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import timber.log.Timber

class SearchFlightViewModel : ViewModel() {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    var searchType: String? = null

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _searchFlightUiState: MutableStateFlow<SearchFlightUiState> =
        MutableStateFlow(SearchFlightUiState.Loading)
    var searchFlightUiState: StateFlow<SearchFlightUiState> = _searchFlightUiState

    private fun updateUiState(newState: SearchFlightUiState) {
        this._searchFlightUiState.value = newState
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")

        // Try to get bundle values
        intent.extras?.let { bundle ->

            val searchType = bundle.getString(Constants.EXTRA_SEARCH_TYPE).toString()
            this.searchType = searchType
            val flightString: String? = bundle.getString(Constants.EXTRA_FLIGHT)

            flightString?.let {
                val extraItem: List<FlightModel>? = Json.decodeFromString(flightString)
                when (searchType) {
                    Constants.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER -> {

                        extraItem?.let { flights ->
                            // Log
                            Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | item : $it")
                            updateUiState(SearchFlightUiState.Success(flights))
                        }
                            ?: run { Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | Extra item object is null") }
                    }

                    Constants.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE -> {
                        extraItem?.let { flights ->
                            // Log
                            Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | item : $it")
                            updateUiState(SearchFlightUiState.Success(flights))
                        }
                            ?: run { Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | Extra item object is null") }
                    }

                    else -> {
                        Timber.e("Unknown search type: $searchType")
                    }
                }
            } ?: run { Timber.e("getBundle() | flightString is null") }

        } ?: run { Timber.e("Intent extras are null") }
    }

    fun onEvent(uiEvent: UiEvent) {
        Timber.d("onEvent() | uiEvent: $uiEvent")
    }
}