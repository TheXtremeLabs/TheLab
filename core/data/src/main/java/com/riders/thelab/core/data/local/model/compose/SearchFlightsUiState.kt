package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import kotools.types.text.NotBlankString

@Stable
sealed interface SearchFlightsUiState {
    data class Success(val flights: List<SearchFlightModel>) : SearchFlightsUiState
    data class Error(
        val message: NotBlankString,
        val throwable: Throwable? = null
    ) : SearchFlightsUiState

    data object Loading : SearchFlightsUiState
}