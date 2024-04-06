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
    suspend fun omniSearchAirport(
        @Query("searchterm") searchTerm: String,
        @Query("q") query: String
    ): AirportsSearchResponse

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
     *
     * @param departureAirportCode ICAO, IATA or LID ID of destination airport to fetch. ICAO is highly preferred to prevent ambiguity.
     * @param arrivalAirportCode ICAO, IATA or LID ID of destination airport to fetch. ICAO is highly preferred to prevent ambiguity.
     * @param type Type of flights to return. Allowed: General_Aviation ┃ Airline
     * @param connection Whether flights should be filtered based on their connection status.
     * If setting start/end date parameters then connection must be set to nonstop, and will default to nonstop if left blank.
     * If start/end are not specified then leaving this blank will result in a mix of nonstop and one-stop flights being returned,
     * with a preference for nonstop flights. One-stop flights are identified with a custom heuristic, which may be incomplete.
     * Allowed: nonstop ┃ onestop
     * @param startDate The starting date range for flight results. The format is ISO8601 date or datetime, and the bound is inclusive.
     * Specified start date must be no further than 10 days in the past and 2 days in the future.
     * If using date instead of datetime, the time will default to 00:00:00Z.
     * @param endDate The ending date range for flight results. The format is ISO8601 date or datetime, and the bound is exclusive.
     * Specified end date must be no further than 10 days in the past and 2 days in the future.
     * If using date instead of datetime, the time will default to 00:00:00Z.
     * @param maxPages Maximum number of pages to fetch. This is an upper limit and not a guarantee of how many pages will be returned.
     * @param cursor Opaque value used to get the next batch of data from a paged collection.
     *
     * @return the result of flights by route route for specified airports.
     */
    @GET("airports/{id}/flights/to/{dest_id}")
    suspend fun searchFlightByRoute(
        @Path("id") departureAirportCode: NotBlankString,
        @Path("dest_id") arrivalAirportCode: NotBlankString,
        @Query("type") type: NotBlankString? = null,
        @Query("connection") connection: NotBlankString? = null,
        @Query("start") startDate: NotBlankString? = null,
        @Query("end") endDate: NotBlankString? = null,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): SearchFlightResponse


    /**
     * @return a list of airports located within a given distance from the given location.
     */
    @GET("airports/nearby")
    suspend fun getAirportNearBy(
        @Query("latitude") latitude: NotBlankString,
        @Query("longitude") longitude: NotBlankString,
        @Query("radius") radius: Int,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): AirportsResponse

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

    /**
     * @param ident, registration, or fa_flight_id to fetch.
     * If using a flight ident, it is highly recommended to specify ICAO flight ident rather than IATA flight ident to avoid ambiguity and unexpected results.
     * Setting the ident_type can also be used to help disambiguate.
     * @param type Type of ident provided in the ident parameter.
     * By default, the passed ident is interpreted as a registration if possible.
     * This parameter can force the ident to be interpreted as a designator instead.
     * Allowed: designator ┃ registration ┃ fa_flight_id
     * @param startDate The starting date range for flight results, comparing against flights' scheduled_out field (or scheduled_off if scheduled_out is missing).
     * The format is ISO8601 date or datetime, and the bound is inclusive. Specified start date must be no further than 10 days in the past and 2 days in the future.
     * If not specified, will default to departures starting approximately 11 days in the past. If using date instead of datetime, the time will default to 00:00:00Z.
     * @param endDate The ending date range for flight results, comparing against flights' scheduled_out field (or scheduled_off if scheduled_out is missing).
     * The format is ISO8601 date or datetime, and the bound is exclusive. Specified end date must be no further than 10 days in the past and 2 days in the future.
     * If not specified, will default to departures starting approximately 2 days in the future. If using date instead of datetime, the time will default to 00:00:00Z.
     * @param maxPages Maximum number of pages to fetch. This is an upper limit and not a guarantee of how many pages will be returned.
     * @param cursor Opaque value used to get the next batch of data from a paged collection.
     *
     * @return the flight info status summary for a registration, ident, or fa_flight_id.
     * If a fa_flight_id is specified then a maximum of 1 flight is returned,
     * unless the flight has been diverted in which case both the original flight and any diversions will be returned with a duplicate fa_flight_id.
     * If a registration or ident is specified, approximately 14 days of recent and scheduled flight information is returned, ordered by scheduled_out (or scheduled_off if scheduled_out is missing) descending.
     * Alternately, specify a start and end parameter to find your flight(s) of interest, including up to 10 days of flight history.
     */
    @GET("flights/{ident}")
    suspend fun searchFlightByID(
        @Path("ident") query: NotBlankString,
        @Query("ident_type") type: NotBlankString? = null,
        @Query("start") startDate: NotBlankString? = null,
        @Query("end") endDate: NotBlankString? = null,
        @Query("max_pages") maxPages: Int = 1,
        @Query("cursor") cursor: String? = null
    ): SearchFlightResponse
}