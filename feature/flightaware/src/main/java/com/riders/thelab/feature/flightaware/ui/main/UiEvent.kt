package com.riders.thelab.feature.flightaware.ui.main

import android.content.Context
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import kotools.types.text.NotBlankString

sealed interface UiEvent {
    // Search category
    data class OnSearchCategorySelected(val pageIndex: Int) : UiEvent

    data class OnSearchFlightByID(val id: NotBlankString, val context: Context) : UiEvent
    data class OnSearchFlightByRoute(
        val departureAirportIcaoCode: NotBlankString,
        val arrivalAirportIcaoCode: NotBlankString
    ) : UiEvent

    data class OnUpdateDepartureQuery(val departureAirportQuery: String) : UiEvent
    data class OnUpdateArrivalQuery(val arrivalAirportQuery: String) : UiEvent

    data class OnDepartureOptionsSelected(val departureAirport: AirportSearchModel) : UiEvent
    data class OnArrivalOptionsSelected(val arrivalAirport: AirportSearchModel) : UiEvent

    data object OnFetchAirportNearBy : UiEvent
    data object OnSeeAllAirportClicked : UiEvent
}