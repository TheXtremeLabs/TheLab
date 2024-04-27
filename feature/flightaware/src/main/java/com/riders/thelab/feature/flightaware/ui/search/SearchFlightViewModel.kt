package com.riders.thelab.feature.flightaware.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.compose.SearchFlightUiState
import com.riders.thelab.core.data.local.model.flight.FlightModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class SearchFlightViewModel : ViewModel() {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _searchFlightUiState: MutableStateFlow<SearchFlightUiState> =
        MutableStateFlow(SearchFlightUiState.Loading)
    var searchFlightUiState: StateFlow<SearchFlightUiState> = _searchFlightUiState

    private fun updateUiState(newState: SearchFlightUiState) {
        this._searchFlightUiState.value = newState
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")

        // Try to get bundle values
        intent.extras?.let {

            val searchType = it.getString(SearchFlightActivity.EXTRA_SEARCH_TYPE).toString()
            val extraItem: FlightModel? = if (!LabCompatibilityManager.isTiramisu()) {
                it.getSerializable(SearchFlightActivity.EXTRA_FLIGHT) as FlightModel?
            } else {
                it.getSerializable(
                    SearchFlightActivity.EXTRA_FLIGHT,
                    FlightModel::class.java
                )
            }

            when (searchType) {
                SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER -> {
                    extraItem?.let {
                        // Log
                        Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | item : $it")
                        updateUiState(SearchFlightUiState.SearchFlightByNumber(it))
                    }
                        ?: run { Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | Extra item object is null") }
                }

                SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE -> {
                    extraItem?.let {
                        // Log
                        Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | item : $it")
                        updateUiState(SearchFlightUiState.SearchFlightByRoute(it))
                    }
                        ?: run { Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | Extra item object is null") }
                }

                else -> {
                    Timber.e("Unknown search type: $searchType")
                }
            }
        } ?: run { Timber.e("Intent extras are null") }
    }

    fun onEvent(uiEvent: UiEvent) {
        Timber.d("onEvent() | uiEvent: $uiEvent")

    }
}