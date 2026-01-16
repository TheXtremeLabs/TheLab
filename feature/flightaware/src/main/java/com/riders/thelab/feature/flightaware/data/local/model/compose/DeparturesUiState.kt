package com.riders.thelab.feature.flightaware.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.Departures

@Stable
sealed class DeparturesUiState {
    @Stable
    @Immutable
    data class Success(val departures: List<Departures>) : DeparturesUiState()

    @Stable
    @Immutable
    data class Error(val message: String, val throwable: Throwable? = null) : DeparturesUiState()

    @Stable
    @Immutable
    object Loading : DeparturesUiState()
}