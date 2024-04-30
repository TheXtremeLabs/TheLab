package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class ScheduledArrivals(

    @SerialName("ident")
    val operatorID: NotBlankString,

    @SerialName("ident_icao")
    val identICAO: NotBlankString?,

    @SerialName("ident_iata")
    val identIATA: NotBlankString?,
    @SerialName("actual_runway_off")
    val actualRunwayOff: NotBlankString?,

    @SerialName("actual_runway_on")
    val actualRunwayOn: NotBlankString?,

    @SerialName("fa_flight_id")
    val faFlightID: NotBlankString,

    @SerialName("operator")
    val operator: NotBlankString?,

    @SerialName("operator_icao")
    val operatorICAO: NotBlankString?,

    @SerialName("operator_iata")
    val operatorIATA: NotBlankString?,

    @SerialName("flight_number")
    val flightNumber: NotBlankString?,

    @SerialName("registration")
    val registration: NotBlankString?,

    /* The ident of the flight for Air Traffic Control purposes, when known and different than ident.*/
    @SerialName("atc_ident")
    val atcIdent: NotBlankString?,


    @SerialName("inbound_fa_flight_id")
    val inboundFaFlightID: NotBlankString?,

    @SerialName("codeshares")
    val codeShares: List<NotBlankString>,

    @SerialName("codeshares_iata")
    val codeSharesIata: List<NotBlankString>,

    /* Flag indicating whether this flight is blocked from public viewing.*/
    @SerialName("blocked")
    val blocked: Boolean,
    @SerialName("diverted")
    val diverted: Boolean,
    @SerialName("cancelled")
    val cancelled: Boolean,
    @SerialName("position_only")
    val positionOnly: Boolean,
    @SerialName("origin")
    val origin: OriginDestination,
    @SerialName("destination")
    val destination: OriginDestination? = null,
    @SerialName("departure_delay")
    val departureDelay: Int?,
    @SerialName("arrival_delay")
    val arrivalDelay: Int?,
    /* Runway-to-runway filed duration (seconds). */
    @SerialName("filed_ete")
    val filedEte: Int?,
    @SerialName("progress_percent")
    val progress: Int?,
    @SerialName("status")
    val status: NotBlankString,
    @SerialName("aircraft_type")
    val aircraftType: NotBlankString?,
    @SerialName("route_distance")
    val routeDistance: Int?,
    /* Filed IFR airspeed (knots). */
    @SerialName("filed_airspeed")
    val filedAirSpeed: Int?,
    /* Filed IFR altitude (100s of feet). */
    @SerialName("filed_altitude")
    val filedAltitude: Int?,
    /* The textual description of the flight's route. */
    @SerialName("route")
    val route: NotBlankString?,
    @SerialName("baggage_claim")
    val baggageClaim: NotBlankString?,
    @SerialName("seats_cabin_business")
    val seatsCabinBusiness: Int?,
    @SerialName("seats_cabin_coach")
    val seatsCabinCoach: Int?,
    @SerialName("seats_cabin_first")
    val seatsCabinFirst: Int?,
    @SerialName("gate_origin")
    val gateOrigin: NotBlankString?,
    @SerialName("gate_destination")
    val gateDestination: NotBlankString?,
    @SerialName("terminal_origin")
    val terminalOrigin: NotBlankString?,
    @SerialName("terminal_destination")
    val terminalDestination: NotBlankString?,
    @SerialName("type")
//    val type: FlightType,
    val type: String,
    /* Scheduled gate departure time. */
    @SerialName("scheduled_out")
    val scheduledOut: NotBlankString?,
    /* Estimated gate departure time. */
    @SerialName("estimated_out")
    val estimatedOut: NotBlankString?,
    /* Actual gate departure time. */
    @SerialName("actual_out")
    val actualOut: NotBlankString?,
    /* Scheduled runway departure time. */
    @SerialName("scheduled_off")
    val scheduledOff: NotBlankString?,
    /* Estimated runway departure time. */
    @SerialName("estimated_off")
    val estimatedOff: NotBlankString?,
    /* Actual runway departure time. */
    @SerialName("actual_off")
    val actualOff: NotBlankString?,
    /* Scheduled runway arrival time. */
    @SerialName("scheduled_on")
    val scheduledOn: NotBlankString?,
    /* Estimated runway arrival time. */
    @SerialName("estimated_on")
    val estimatedOn: NotBlankString?,
    /* Actual runway arrival time. */
    @SerialName("actual_on")
    val actualOn: NotBlankString?,
    /*Scheduled gate arrival time. */
    @SerialName("scheduled_in")
    val scheduledIn: NotBlankString?,
    /* Estimated gate arrival time. */
    @SerialName("estimated_in")
    val estimatedIn: NotBlankString?,
    /* Actual gate arrival time. */
    @SerialName("actual_in")
    val actualIn: NotBlankString?
) : Serializable
