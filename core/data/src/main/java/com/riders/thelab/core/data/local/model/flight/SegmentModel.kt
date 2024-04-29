package com.riders.thelab.core.data.local.model.flight

import com.riders.thelab.core.data.remote.dto.flight.FlightType
import com.riders.thelab.core.data.remote.dto.flight.Segment
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class SegmentModel(
    val operatorID: NotBlankString,
    val identICAO: NotBlankString? = null,
    val identIATA: NotBlankString? = null,
    /* Actual departure runway at origin, when known */
    val actualRunwayOff: NotBlankString? = null,
    /* Actual arrival runway at destination, when known */
    val actualRunwayOn: NotBlankString? = null,
    val faFlightID: NotBlankString,
    val operator: NotBlankString? = null,
    val operatorICAO: NotBlankString? = null,
    val operatorIATA: NotBlankString? = null,
    val flightNumber: NotBlankString? = null,
    val registration: NotBlankString? = null,
    /* The ident of the flight for Air Traffic Control purposes, when known and different than ident.*/
    val atcIdent: NotBlankString? = null,
    val inboundFaFlightID: NotBlankString? = null,
    /* Flag indicating whether this flight is blocked from public viewing.*/
    val blocked: Boolean,
    val diverted: Boolean,
    val cancelled: Boolean,
    val positionOnly: Boolean,
    val origin: OriginDestinationModel,
    val destination: OriginDestinationModel? = null,
    val departureDelay: Int?,
    val arrivalDelay: Int?,
    val progress: Int?,
    val status: NotBlankString,
    val aircraftType: NotBlankString?,
    val routeDistance: Int?,
    /* Filed IFR airspeed (knots). */
    val filedAirSpeed: Int?,
    /* Filed IFR altitude (100s of feet). */
    val filedAltitude: Int?,
    /* The textual description of the flight's route. */
    val route: NotBlankString?,
    val baggageClaim: NotBlankString?,
    val seatsCabinBusiness: Int?,
    val seatsCabinCoach: Int?,
    val seatsCabinFirst: Int?,
    val gateOrigin: NotBlankString?,
    val gateDestination: NotBlankString?,
    val terminalOrigin: NotBlankString?,
    val terminalDestination: NotBlankString?,
    val type: FlightType,
    val filedEte: Int? = null,
    /* Scheduled gate departure time. */
    val scheduledOut: NotBlankString?,
    /* Estimated gate departure time. */
    val estimatedOut: NotBlankString?,
    /* Actual gate departure time. */
    val actualOut: NotBlankString?,
    /* Scheduled runway departure time. */
    val scheduledOff: NotBlankString?,
    /* Estimated runway departure time. */
    val estimatedOff: NotBlankString?,
    /* Actual runway departure time. */
    val actualOff: NotBlankString?,
    /* Scheduled runway arrival time. */
    val scheduledOn: NotBlankString?,
    /* Estimated runway arrival time. */
    val estimatedOn: NotBlankString?,
    /* Actual runway arrival time. */
    val actualOn: NotBlankString?,
    /*Scheduled gate arrival time. */
    val scheduledIn: NotBlankString?,
    /* Estimated gate arrival time. */
    val estimatedIn: NotBlankString?,
    /* Actual gate arrival time. */
    val actualIn: NotBlankString?
) : Serializable

fun Segment.toSegmentModel(): SegmentModel = SegmentModel(
    operatorID = this.operatorID,
    identICAO = this.identICAO,
    identIATA = this.identIATA,
    actualRunwayOff = this.actualRunwayOff,
    actualRunwayOn = this.actualRunwayOn,
    faFlightID = this.faFlightID,
    operator = this.operator,
    operatorICAO = this.operatorICAO,
    operatorIATA = this.operatorIATA,
    flightNumber = this.flightNumber,
    registration = this.registration,
    atcIdent = this.atcIdent,
    inboundFaFlightID = this.inboundFaFlightID,
    blocked = this.blocked,
    diverted = this.diverted,
    cancelled = this.cancelled,
    positionOnly = this.positionOnly,
    origin = this.origin.toOriginDestinationModel(),
    destination = this.destination?.toOriginDestinationModel(),
    departureDelay = this.departureDelay,
    arrivalDelay = this.arrivalDelay,
    progress = this.progress,
    status = this.status,
    aircraftType = this.aircraftType,
    routeDistance = this.routeDistance,
    filedAirSpeed = this.filedAirSpeed,
    filedAltitude = this.filedAltitude,
    route = this.route,
    baggageClaim = this.baggageClaim,
    seatsCabinBusiness = this.seatsCabinBusiness,
    seatsCabinCoach = this.seatsCabinCoach,
    seatsCabinFirst = this.seatsCabinFirst,
    gateOrigin = this.gateOrigin,
    gateDestination = this.gateDestination,
    terminalOrigin = this.terminalOrigin,
    terminalDestination = this.terminalDestination,
    type = FlightType.entries.first { it.type == type },
    scheduledOut = this.scheduledOut,
    estimatedOut = this.estimatedOut,
    actualOut = this.actualOut,
    scheduledOff = this.scheduledOff,
    estimatedOff = this.estimatedOff,
    actualOff = this.actualOff,
    scheduledOn = this.scheduledOn,
    estimatedOn = this.estimatedOn,
    actualOn = this.actualOn,
    scheduledIn = this.scheduledIn,
    estimatedIn = this.estimatedIn,
    actualIn = this.actualIn
)