package com.riders.thelab.feature.flightaware.data.local.model.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.Arrivals

@Stable
sealed class ArrivalsUiState {
    @Stable
    @Immutable
    data class Success(val arrivals: List<Arrivals>) : ArrivalsUiState()

    @Stable
    @Immutable
    data class Error(val message: String, val throwable: Throwable? = null) : ArrivalsUiState()

    @Stable
    @Immutable
    object Loading : ArrivalsUiState()

}