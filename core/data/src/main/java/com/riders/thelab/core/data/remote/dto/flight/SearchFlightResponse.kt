package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

/**
 * {
 * links*: {
 * Object containing links to related resources.
 *
 * next*: uri-reference
 * A link to the next set of records in a collection.
 *
 * }
 * num_pages*: integer
 * Number of pages returnedConstraints: Min 1
 * flights*: [{
 * ident*: string
 * Either the operator code followed by the flight number for the flight (for commercial flights) or the aircraft's registration (for general aviation).
 *
 * ident_icao: string┃null
 * The ICAO operator code followed by the flight number for the flight (for commercial flights)
 *
 * ident_iata: string┃null
 * The IATA operator code followed by the flight number for the flight (for commercial flights)
 *
 * fa_flight_id*: string
 * Unique identifier assigned by FlightAware for this specific flight. If the flight is diverted, the new leg of the flight will have a duplicate fa_flight_id.
 *
 * origin*: {
 * FlightAirportRef: Information for this flight's origin airport.
 *
 * code*: string┃null
 * ICAO/IATA/LID code or string indicating the location where tracking of the flight began/ended for position-only flights.
 *
 * code_icao: string┃null
 * ICAO code
 *
 * code_iata: string┃null
 * IATA code
 *
 * code_lid: string┃null
 * LID code
 *
 * timezone: string┃null
 * Applicable timezone for the airport, in the TZ database format
 *
 * name: string┃null
 * Common name of airport
 *
 * city: string┃null
 * Closest city to the airport
 *
 * airport_info_url*: uri-reference┃null
 * The URL to more information about the airport. Will be null for position-only flights.
 *
 * }
 * destination*: {
 * FlightAirportRef: Information for this flight's destination airport.
 *
 * code*: string┃null
 * ICAO/IATA/LID code or string indicating the location where tracking of the flight began/ended for position-only flights.
 *
 * code_icao: string┃null
 * ICAO code
 *
 * code_iata: string┃null
 * IATA code
 *
 * code_lid: string┃null
 * LID code
 *
 * timezone: string┃null
 * Applicable timezone for the airport, in the TZ database format
 *
 * name: string┃null
 * Common name of airport
 *
 * city: string┃null
 * Closest city to the airport
 *
 * airport_info_url*: uri-reference┃null
 * The URL to more information about the airport. Will be null for position-only flights.
 *
 * }
 * waypoints*: [number]
 * Route waypoints as an array of alternating latitudes and longitudes.
 *
 * first_position_time*: date-time┃null
 * Timestamp of when the first position for this flight was received.
 *
 * last_position*: {
 * FlightPosition: Most recent position received for this flight.
 *
 * fa_flight_id*: string┃null
 * Unique identifier assigned by FlightAware to the flight with this position. This field is only populated by the /flights/search/positions (in other cases, the user will have already specified the fa_flight_id).
 *
 * altitude*: integer
 * Aircraft altitude in hundreds of feet
 *
 * altitude_change*: enum
 * C when the aircraft is climbing, D when descending, and - when the altitude is being maintained.Allowed: C┃D┃-
 * groundspeed*: integer
 * Most recent groundspeed (knots)
 *
 * heading*: integer┃null
 * Aircraft heading in degrees (0-360)Constraints: Min 0┃Max 360
 * latitude*: number
 * Most recent latitude position
 *
 * longitude*: number
 * Most recent longitude position
 *
 * timestamp*: date-time
 * Time that position was received
 *
 * update_type*: enum┃null
 * P=projected, O=oceanic, Z=radar, A=ADS-B, M=multilateration, D=datalink, X=surface and near surface (ADS-B and ASDE-X), S=space-basedAllowed: P┃O┃Z┃A┃M┃D┃X┃S┃}
 * bounding_box*: [number]
 * List of 4 coordinates representing the edges of a box that entirely contains this flight's positions. The order of the coordinates are the top, left, bottom, and right sides of the box.
 * Min Items: 4 Max Items: 4
 *
 * ident_prefix*: string┃null
 * A one or two character identifier prefix code (Common values: G or GG Medevac, L Lifeguard, A Air Taxi, H Heavy, M Medium).
 *
 * aircraft_type*: string┃null
 * Aircraft type will generally be ICAO code, but IATA code will be given when the ICAO code is not known.
 *
 * actual_off*: date-time┃null
 * Actual runway departure time.
 *
 * actual_on*: date-time┃null
 * Actual runway arrival time.
 *
 * foresight_predictions_available*: boolean
 * Indicates if Foresight predictions are available for AeroAPI /foresight endpoints.
 *
 * predicted_out*: date-time┃null
 * Predicted time of gate departure event. Only available from /foresight endpoints.
 *
 * predicted_off*: date-time┃null
 * Predicted time of runway departure event. Only available from /foresight endpoints.
 *
 * predicted_on*: date-time┃null
 * Predicted time of runway arrival event. Only available from /foresight endpoints.
 *
 * predicted_in*: date-time┃null
 * Predicted time of gate arrival event. Only available from /foresight endpoints.
 *
 * predicted_out_source*: enum┃null
 * Source indicator of the predicted time of the gate departure event. Only available from /foresight endpoints.Allowed: ┃Foresight┃Historical Average
 * predicted_off_source*: enum┃null
 * Source indicator of the predicted time of the runway departure event. Only available from /foresight endpoints.Allowed: ┃Foresight┃Historical Average
 * predicted_on_source*: enum┃null
 * Source indicator of the predicted time of the runway arrival event. Only available from /foresight endpoints.Allowed: ┃Foresight┃Historical Average
 * predicted_in_source*: enum┃null
 * Source indicator of the predicted time of the gate arrival event. Only available from /foresight endpoints.Allowed: ┃Foresight┃Historical Average}]}
 */
@kotlinx.serialization.Serializable
data class SearchFlightResponse(
    @SerialName("links")
    val links: Link? = null,
    @SerialName("num_pages")
    val numPages: Int,
    @SerialName("flights")
    val flights: List<Flight>,
) : Serializable

