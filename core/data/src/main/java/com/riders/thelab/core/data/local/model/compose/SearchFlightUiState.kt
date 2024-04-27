package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.flight.FlightModel

@Stable
sealed class SearchFlightUiState {
    data class SearchFlightByNumber(val flight: FlightModel) : SearchFlightUiState()
    data class SearchFlightByRoute(val flight: FlightModel) : SearchFlightUiState()
    data object Loading : SearchFlightUiState()
}
