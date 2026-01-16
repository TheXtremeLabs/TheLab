package com.riders.thelab.feature.flightaware.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.flight.AirportModel

@Stable
sealed class AirportDetailUiState {
    @Stable
    @Immutable
    data class Success(val airport: AirportModel) : AirportDetailUiState()

    @Stable
    @Immutable
    data class Error(val message: String, val throwable: Throwable? = null) : AirportDetailUiState()

    @Stable
    @Immutable
    object Loading : AirportDetailUiState()
}