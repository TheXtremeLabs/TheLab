package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Flight(
    @SerialName("ident")
    val operatorID: NotBlankString? = null,
    @SerialName("ident_icao")
    val identICAO: NotBlankString? = null,
    @SerialName("ident_iata")
    val identIATA: NotBlankString? = null,
    @SerialName("fa_flight_id")
    val faFlightID: NotBlankString? = null,
    @SerialName("waypoints")
    val waypoints: Int? = null,
    @SerialName("first_position_time")
    val firstTimePosition: NotBlankString? = null,
    @SerialName("last_position")
    val lastPosition: LastPosition?,
    /* List of 4 coordinates representing the edges of a box that entirely contains this flight's positions.
     * The order of the coordinates are the top, left, bottom, and right sides of the box.
     */
    @SerialName("bounding_box")
    val boundingBox: List<Int>? = null,
    @SerialName("ident_prefix")
    val identPrefix: NotBlankString? = null,
    @SerialName("aircraft_type")
    val aircraftType: NotBlankString? = null,

    @SerialName("registration")
    val registration: NotBlankString? = null,

    /* The ident of the flight for Air Traffic Control purposes, when known and different than ident.*/
    @SerialName("atc_ident")
    val atcIdent: NotBlankString? = null,

    @SerialName("inbound_fa_flight_id")
    val inboundFaFlightID: NotBlankString? = null,

    @SerialName("codeshares")
    val codeShares: List<NotBlankString>? = null,

    @SerialName("codeshares_iata")
    val codeSharesIata: List<NotBlankString>? = null,

    /* Flag indicating whether this flight is blocked from public viewing.*/
    @SerialName("blocked")
    val blocked: Boolean? = null,
    @SerialName("diverted")
    val diverted: Boolean? = null,
    @SerialName("cancelled")
    val cancelled: Boolean? = null,
    @SerialName("position_only")
    val positionOnly: Boolean? = null,
    @SerialName("origin")
    val origin: Origin? = null,
    @SerialName("destination")
    val destination: Destination? = null,
    /*
     * Departure delay (in seconds) based on either actual or estimated gate departure time.
     * If gate time is unavailable then based on runway departure time.
     * A negative value indicates the flight is early.
     */
    @SerialName("departure_delay")
    val departureDelay: Int? = null,
    /*
     * Arrival delay (in seconds) based on either actual or estimated gate arrival time.
     * If gate time is unavailable then based on runway arrival time.
     * A negative value indicates the flight is early.
     */
    @SerialName("arrival_delay")
    val arrivalDelay: Int? = null,
    /* Runway-to-runway filed duration (seconds). */
    @SerialName("filed_ete")
    val filedEte: Int? = null,
    /*
     * The percent completion of a flight, based on runway departure/arrival.
     * Null for en route position-only flights.
     * Constraints: Min 0┃Max 100
     */
    @SerialName("progress_percent")
    val progress: Int?,
    @SerialName("status")
    val status: NotBlankString? = null,
    @SerialName("route_distance")
    val routeDistance: Int?,
    /* Filed IFR airspeed (knots). */
    @SerialName("filed_airspeed")
    val filedAirSpeed: Int? = null,
    /* Filed IFR altitude (100s of feet). */
    @SerialName("filed_altitude")
    val filedAltitude: Int? = null,
    /* The textual description of the flight's route. */
    @SerialName("route")
    val route: NotBlankString? = null,
    @SerialName("baggage_claim")
    val baggageClaim: NotBlankString? = null,
    @SerialName("seats_cabin_business")
    val seatsCabinBusiness: Int? = null,
    @SerialName("seats_cabin_coach")
    val seatsCabinCoach: Int? = null,
    @SerialName("seats_cabin_first")
    val seatsCabinFirst: Int? = null,
    @SerialName("gate_origin")
    val gateOrigin: NotBlankString? = null,
    @SerialName("gate_destination")
    val gateDestination: NotBlankString? = null,
    @SerialName("terminal_origin")
    val terminalOrigin: NotBlankString? = null,
    @SerialName("terminal_destination")
    val terminalDestination: NotBlankString? = null,
    @SerialName("type")
    val type: String? = null,
    /* Scheduled gate departure time. */
    @SerialName("scheduled_out")
    val scheduledOut: NotBlankString? = null,
    /* Estimated gate departure time. */
    @SerialName("estimated_out")
    val estimatedOut: NotBlankString? = null,
    /* Actual gate departure time. */
    @SerialName("actual_out")
    val actualOut: NotBlankString? = null,
    /* Scheduled runway departure time. */
    @SerialName("scheduled_off")
    val scheduledOff: NotBlankString? = null,
    /* Estimated runway departure time. */
    @SerialName("estimated_off")
    val estimatedOff: NotBlankString? = null,
    /* Scheduled runway arrival time. */
    @SerialName("scheduled_on")
    val scheduledOn: NotBlankString? = null,
    /* Estimated runway arrival time. */
    @SerialName("estimated_on")
    val estimatedOn: NotBlankString? = null,
    /*Scheduled gate arrival time. */
    @SerialName("scheduled_in")
    val scheduledIn: NotBlankString? = null,
    /* Estimated gate arrival time. */
    @SerialName("estimated_in")
    val estimatedIn: NotBlankString? = null,
    /* Actual gate arrival time. */
    @SerialName("actual_in")
    val actualIn: NotBlankString? = null,
    /* Actual runway departure time. */
    @SerialName("actual_off")
    val actualOff: NotBlankString? = null,
    /* Actual runway arrival time. */
    @SerialName("actual_on")
    val actualOn: NotBlankString? = null,
    @SerialName("foresight_predictions_available")
    val foresightPredictionsAvailable: Boolean? = null,
    /* Predicted time of gate departure event. Only available from /foresight endpoints. */
    @SerialName("predicted_out")
    val predictedOut: NotBlankString? = null,
    /* Predicted time of runway departure event. Only available from /foresight endpoints. */
    @SerialName("predicted_off")
    val predictedOff: NotBlankString? = null,
    /* Predicted time of runway arrival event. Only available from /foresight endpoints. */
    @SerialName("predicted_on")
    val predictedOn: NotBlankString? = null,
    /* Predicted time of gate arrival event. Only available from /foresight endpoints. */
    @SerialName("predicted_in")
    val predictedIn: NotBlankString? = null,
    @SerialName("predicted_out_source")
    val predictedOutSource: PredictedSource? = null,
    @SerialName("predicted_off_source")
    val predictedOffSource: PredictedSource? = null,
    @SerialName("predicted_on_source")
    val predictedOnSource: PredictedSource? = null,
    @SerialName("predicted_in_source")
    val predictedInSource: PredictedSource? = null,
    @SerialName("segments")
    val segments: List<Segment>? = null,
) : Serializable
