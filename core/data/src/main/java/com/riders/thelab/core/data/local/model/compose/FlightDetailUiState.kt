package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import kotools.types.text.NotBlankString

@Stable
sealed interface FlightDetailUiState {
    data class Success(val flight: SearchFlightModel) : FlightDetailUiState
    data class Error(val message: NotBlankString) : FlightDetailUiState
    data object Loading : FlightDetailUiState
}