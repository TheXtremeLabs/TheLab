package com.riders.thelab.feature.flightaware.ui.search

import com.riders.thelab.core.data.local.model.flight.SearchFlightModel

sealed interface UiEvent {
    data class OnFlightClicked(val flightItem: SearchFlightModel) : UiEvent
}