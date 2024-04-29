package com.riders.thelab.feature.flightaware.ui.search

import com.riders.thelab.core.data.local.model.flight.FlightModel

sealed interface UiEvent {
    data class OnFlightClicked(val flightModel: FlightModel) : UiEvent
}