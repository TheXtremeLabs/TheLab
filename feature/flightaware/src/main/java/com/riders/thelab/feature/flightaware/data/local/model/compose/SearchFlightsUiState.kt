package com.riders.thelab.feature.flightaware.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.feature.flightaware.data.local.model.SearchFlightType
import kotools.types.text.NotBlankString

@Immutable
@Stable
sealed interface SearchFlightsUiState {

    @Immutable
    @Stable
    data class Success(val flights: List<SearchFlightModel>) : SearchFlightsUiState

    @Immutable
    @Stable
    data class Error(
        val message: NotBlankString,
        val throwable: Throwable? = null
    ) : SearchFlightsUiState

    @Immutable
    @Stable
    data class Loading(
        val searchType: SearchFlightType,
        val message: NotBlankString? = null
    ) : SearchFlightsUiState
}