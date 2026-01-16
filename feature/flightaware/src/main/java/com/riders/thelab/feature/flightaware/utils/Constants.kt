package com.riders.thelab.feature.flightaware.utils

object Constants {
    const val DATE_FORMAT_PATTERN = "d MMM uuuu"

    private const val BASE_ENDPOINT_FLIGHT_LOGO =
        "https://assets.duffel.com/img/airlines/for-light-background/"
    const val ENDPOINT_FLIGHT_LOGO = "${BASE_ENDPOINT_FLIGHT_LOGO}full-color-logo/"
    const val ENDPOINT_FLIGHT_FULL_LOGO = "${BASE_ENDPOINT_FLIGHT_LOGO}full-color-lockup/"
    const val EXTENSION_SVG = ".svg"


    const val EXTRA_AIRPORT_ID = "EXTRA_AIRPORT_ID"
    const val EXTRA_FLIGHT: String = "EXTRA_FLIGHT"
    const val EXTRA_FLIGHT_LIST: String = "EXTRA_FLIGHT_LIST"
    const val EXTRA_SEARCH_TYPE: String = "EXTRA_SEARCH_TYPE"
    const val EXTRA_SEARCH_TYPE_FLIGHT_NUMBER: String = "EXTRA_SEARCH_TYPE_FLIGHT_NUMBER"
    const val EXTRA_SEARCH_TYPE_FLIGHT_ROUTE: String = "EXTRA_SEARCH_TYPE_FLIGHT_ROUTE"
}