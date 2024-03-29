package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.flight.Airport
import com.riders.thelab.core.data.remote.dto.flight.AirportFlightsResponse
import com.riders.thelab.core.data.remote.dto.flight.AirportsResponse
import com.riders.thelab.core.data.remote.dto.flight.AirportsSearchResponse
import com.riders.thelab.core.data.remote.dto.flight.Operator
import com.riders.thelab.core.data.remote.dto.flight.OperatorResponse
import com.riders.thelab.core.data.remote.dto.flight.SearchFlightResponse
import kotools.types.text.NotBlankString
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface FlightApiService {

    @GET("airports")
    suspend fun getAirports(
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): AirportsResponse

    @GET("https://www.flightaware.com/ajax/ignoreall/airport_names_yajl.rvt?locale=fr_FR&code=1")
    suspend fun searchAirportById(@Query("q") query: String): AirportsSearchResponse
    @GET("https://www.flightaware.com/ajax/ignoreall/omnisearch/airport.rvt?v=50&locale=fr_FR")
    suspend fun omniSearchAirport(@Query("searchterm") searchTerm: String, @Query("q") query: String): AirportsSearchResponse

    @GET("airports/{id}")
    suspend fun getAirportById(@Path("id") airportID: String): Airport

    /**
     * Get flights that have recently departed from an airport
     *
     * @return Returns flights that have departed from an airport and not been diverted, ordered by actual_off descending.
     * The optional start and end parameters will be compared against actual_off to limit the flights returned.
     * The start parameter's default value is 24 hours before the current time.
     * The end parameter's default value is the current time.
     */
    @GET("airports/{id}/flights/departures")
    suspend fun getAirportFlightsDepartures(
        @Path("id") airportID: String,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null,
        @Query("start") startDate: String? = null,
        @Query("end") endDate: String? = null,
        @Query("type") type: String? = null
    ): AirportFlightsResponse

    /**
     * Get flights that have recently arrived at an airport
     *
     * @return Returns flights that have arrived at an airport, ordered by actual_on descending.
     * The start parameter's default value is 24 hours before the current time.
     * The end parameter's default value is the current time.
     */
    @GET("airports/{id}/flights/arrivals")
    suspend fun getAirportFlightsArrivals(
        @Path("id") airportID: String,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null,
        @Query("start") startDate: String? = null,
        @Query("end") endDate: String? = null,
        @Query("type") type: String? = null
    ): AirportFlightsResponse

    /**
     * Get flight counts for an airport
     *
     * @return Returns counts of flights for an airport broken down by flight status.
     * The returned categories are subtly different from what is returned from the /airports/{id}/flights endpoints.
     * Specifically, this operation does not include completed flights in its counts, and it does not count cancelled/diverted flights.
     * It also does not strictly bound the time for which scheduled flights are counted, so all future flights that FlightAware knows about are included in the counts.
     * See the response schema and documentation for the airport flights endpoints for more information.
     */
    @GET("airports/{id}/flights/counts")
    suspend fun getAirportFlightsCount(
        @Path("id") airportID: String,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null,
        @Query("start") startDate: String? = null,
        @Query("end") endDate: String? = null,
        @Query("type") type: String? = null
    ): AirportFlightsResponse

    /**
     * Get all flights for a given airport
     *
     * @param startDate The starting date range for flight results. The format is ISO8601 date or datetime, and the bound is inclusive.
     * Specified start date must be no further than 10 days in the past and 2 days in the future.
     * If using date instead of datetime, the time will default to 00:00:00Z.
     * Example: 2021-12-31T19:59:59Z ┃ 2021-12-31
     *
     * @param endDate The ending date range for flight results. The format is ISO8601 date or datetime, and the bound is exclusive.
     * Specified end date must be no further than 10 days in the past and 2 days in the future.
     * If using date instead of datetime, the time will default to 00:00:00Z.
     * Example: 2021-12-31T19:59:59Z ┃ 2021-12-31
     *
     * @return All recent and upcoming flights departing from or arriving at the specified airport.
     * Filtering/ordering behavior for the optional start and end parameters
     * for each type (scheduled_departures, scheduled_arrivals, departures, arrivals)
     * match the behavior in their corresponding endpoints.
     */
    @GET("airports/{id}/flights")
    suspend fun getAirportFlightsById(
        @Path("id") airportID: String,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null,
        @Query("start") startDate: String? = null,
        @Query("end") endDate: String? = null,
        @Query("type") type: String? = null
    ): AirportFlightsResponse

    /**
     * Get all flights for a given airport
     *
     * @param startDate The starting date range for flight results. The format is ISO8601 date or datetime, and the bound is inclusive.
     * Specified start date must be no further than 10 days in the past and 2 days in the future.
     * If using date instead of datetime, the time will default to 00:00:00Z.
     * Example: 2021-12-31T19:59:59Z ┃ 2021-12-31
     *
     * @param endDate The ending date range for flight results. The format is ISO8601 date or datetime, and the bound is exclusive.
     * Specified end date must be no further than 10 days in the past and 2 days in the future.
     * If using date instead of datetime, the time will default to 00:00:00Z.
     * Example: 2021-12-31T19:59:59Z ┃ 2021-12-31
     *
     * @return All recent and upcoming flights departing from or arriving at the specified airport.
     * Filtering/ordering behavior for the optional start and end parameters
     * for each type (scheduled_departures, scheduled_arrivals, departures, arrivals)
     * match the behavior in their corresponding endpoints.
     */
    @GET("airports/{id}/flights")
    suspend fun getAirportFlightsWithUrl(
        @Url flightForAirportIdUrl: String,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null,
        @Query("start") startDate: String? = null,
        @Query("end") endDate: String? = null,
        @Query("type") type: String? = null
    ): AirportFlightsResponse

    // TODO: Create model
    /**
     * This endpoint is quite similar to the FindFlight operator in prior versions of AeroAPI.
     * Results may include both non-stop and one-stop flights.
     * Note that because the returned flights can include multiple legs,
     * the response format differs from most other flight-returning endpoints.
     * If the optional start or end query parameters are not provided start will default to 1 day in the future,
     * while end will default to 7 days in the past relative to the time the query is made.
     */
    @GET("airports/{id}/flights/to/{dest_id}")
    suspend fun searchFlightByRoute(
        @Path("id") departureAirportCode: NotBlankString,
        @Path("dest_id") arrivalAirportCode: NotBlankString,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): SearchFlightResponse

    // TODO: Create model
    /**
     * Returns a list of airports located within a given distance from the given location.
     */
    @GET("airports/nearby")
    suspend fun getAirportNearBy(
        @Query("latitude") departureAirportCode: NotBlankString,
        @Query("longitude") arrivalAirportCode: NotBlankString,
        @Query("radius") radius: Int,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): SearchFlightResponse

    @GET("operators")
    suspend fun getOperators(
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): OperatorResponse

    @GET("operators/{id}")
    suspend fun getOperatorById(@Path("id") operatorID: String): Operator


    /**
     *
     * @param flightID The fa_flight_id to fetch. If looking for data from more than 10 days ago, please use the corresponding historical endpoint. Example: UAL1234-1234567890-airline-0123
     * @param height Height of requested image (pixels). Default: 480. Min 1┃Max 1500
     * @param width Width of requested image (pixels). Default:Default: 640. Min 1┃Max 1500
     * @param layerOn List of map layers to enable. Allowed: US Cities ┃ european country boundaries ┃ asia country boundaries ┃ major airports ┃ country boundaries ┃ US state boundaries ┃ water ┃ US major roads ┃ radar ┃ track ┃ flights ┃ airports
     * @param layerOff List of map layers to disable. Allowed: US Cities ┃ european country boundaries ┃ asia country boundaries ┃ major airports ┃ country boundaries ┃ US state boundaries ┃ water ┃ US major roads ┃ radar ┃ track ┃ flights ┃ airports
     * @param showDataBlock Whether a textual caption containing the ident, type, heading, altitude, origin, and destination should be displayed by the flight's position.
     * @param airportExpandView Whether to force zoom area to ensure origin/destination airports are visible. Enabling this flag forcefully enables the show_airports flag as well.
     * @param showAirports Whether to show the origin/destination airports for the flight as labeled points on the map.
     * @param boundingBox Manually specify the zoom area of the map using custom bounds. Should be a list of 4 coordinates representing the top, right, bottom, and left sides of the area (in that order).
     *
     * @return A flight's track as a base64-encoded image.
     * Image can contain a variety of additional data layers beyond just the track.
     * Data from up to 10 days ago can be obtained.
     * If looking for older data, please use the corresponding historical endpoint.
     */
    @GET("flights/{id}/map")
    suspend fun getFlightOnMap(
        @Path("id") flightID: NotBlankString,
        @Query("height") height: Int = 1,
        @Query("width") width: Int = 1,
        @Query("layer_on") layerOn: Array<NotBlankString>? = null,
        @Query("layer_off") layerOff: Array<NotBlankString>? = null,
        @Query("show_data_block") showDataBlock: Boolean = false,
        @Query("airports_expand_view") airportExpandView: Boolean = false,
        @Query("show_airports") showAirports: Boolean = false,
        @Query("bounding_box") boundingBox: Boolean = false,
    ): ByteArray

    /**
     * Search for airborne flights by matching against various parameters including geospatial data.
     * Uses a simplified query syntax compared to /flights/search/advanced.
     *
     * -prefix STRING
     * -type STRING
     * -idents STRING
     * -identOrReg STRING
     * -airline STRING
     * -destination STRING
     * -origin STRING
     * -originOrDestination STRING
     * -aboveAltitude INTEGER
     * -belowAltitude INTEGER
     * -aboveGroundspeed INTEGER
     * -belowGroundspeed INTEGER
     * -latlong "MINLAT MINLON MAXLAT MAXLON"
     * -filter {ga|airline}
     */
    @GET("flights/search")
    suspend fun searchFlight(
        @Query("query") query: NotBlankString,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): SearchFlightResponse
}