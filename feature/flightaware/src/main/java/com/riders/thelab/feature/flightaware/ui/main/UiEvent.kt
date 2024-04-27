package com.riders.thelab.feature.flightaware.ui.main

import android.content.Context
import com.riders.thelab.core.data.local.model.flight.AirportSearchModel
import kotools.types.text.NotBlankString

sealed interface UiEvent {
    // Search category
    data class OnSearchCategorySelected(val pageIndex: Int) : UiEvent

    data class OnSearchFlightByID(val id: NotBlankString, val context: Context) : UiEvent

    /**
     * Search flight by route with departure airport's ICAO code and arrival airport's ICAO Code
     *
     * @param departureAirportIcaoCode airport's ICAO code
     * @param arrivalAirportIcaoCode airport's ICAO code
     */
    data class OnSearchFlightByRoute(
        val context:Context
       /* val departureAirportIcaoCode: NotBlankString,
        val arrivalAirportIcaoCode: NotBlankString*/
    ) : UiEvent


    //////////////////////////////////////////////////////////
    // Departure event
    //////////////////////////////////////////////////////////
    data class OnUpdateDepartureQuery(val departureAirportQuery: String) : UiEvent
    data class OnDepartureExpanded(val expanded: Boolean) : UiEvent
    data class OnDepartureOptionsSelected(val departureAirport: AirportSearchModel) : UiEvent


    //////////////////////////////////////////////////////////
    // Arrival event
    //////////////////////////////////////////////////////////
    data class OnUpdateArrivalQuery(val arrivalAirportQuery: String) : UiEvent
    data class OnArrivalExpanded(val expanded: Boolean) : UiEvent
    data class OnArrivalOptionsSelected(val arrivalAirport: AirportSearchModel) : UiEvent

    data object OnFetchAirportNearBy : UiEvent
    data object OnSeeAllAirportClicked : UiEvent
}