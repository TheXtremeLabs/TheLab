package com.riders.thelab.core.data.local.model.flight

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.Flight
import com.riders.thelab.core.data.remote.dto.flight.FlightType
import com.riders.thelab.core.data.remote.dto.flight.Segment
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import java.io.Serializable
@Stable
@Immutable
@kotlinx.serialization.Serializable
data class SearchFlightModel(

    val faFlightID: NotBlankString,
    val flightNumber: NotBlankString? = null,

    val operatorID: NotBlankString,
    val operatorName: NotBlankString? = null,
    val operatorICAO: NotBlankString? = null,
    val operatorIATA: NotBlankString? = null,

    val origin: OriginDestinationModel? = null,
    val destination: OriginDestinationModel? = null,

    val waypoints: Int? = null,
    val firstTimePosition: NotBlankString? = null,

    /*
     * The percent completion of a flight, based on runway departure/arrival.
     * Null for en route position-only flights.
     * Constraints: Min 0┃Max 100
     */
    val progress: Int?,
    val status: NotBlankString,
    /* List of 4 coordinates representing the edges of a box that entirely contains this flight's positions.
     * The order of the coordinates are the top, left, bottom, and right sides of the box.
     */
    val boundingBox: List<Int>? = null,
    val identPrefix: NotBlankString? = null,
    val aircraftType: NotBlankString? = null,
    val type: FlightType,
    /* Filed IFR airspeed (knots). */
    val filedAirSpeed: Int? = null,
    /* Filed IFR altitude (100s of feet). */
    val filedAltitude: Int? = null,
    val filedEte: Int? = null,

    val registration: NotBlankString? = null,
    /* The ident of the flight for Air Traffic Control purposes, when known and different than ident.*/
    val atcIdent: NotBlankString? = null,
    val inboundFaFlightID: NotBlankString? = null,
    /* Flag indicating whether this flight is blocked from public viewing.*/
    val blocked: Boolean? = null,
    val diverted: Boolean? = null,
    val cancelled: Boolean? = null,
    val positionOnly: Boolean? = null,
    val departureDelay: Int? = null,
    val arrivalDelay: Int? = null,
    val routeDistance: Int? = null,
    /* The textual description of the flight's route. */
    val route: NotBlankString? = null,
    val baggageClaim: NotBlankString? = null,
    val seatsCabinBusiness: Int? = null,
    val seatsCabinCoach: Int? = null,
    val seatsCabinFirst: Int? = null,

    val gateOrigin: NotBlankString? = null,
    val gateDestination: NotBlankString? = null,
    val terminalOrigin: NotBlankString? = null,
    val terminalDestination: NotBlankString? = null,
    /* Scheduled gate departure time. */
    val scheduledOut: NotBlankString? = null,
    /* Estimated gate departure time. */
    val estimatedOut: NotBlankString? = null,
    /* Actual gate departure time. */
    val actualOut: NotBlankString? = null,
    /* Scheduled runway departure time. */
    val scheduledOff: NotBlankString? = null,
    /* Estimated runway departure time. */
    val estimatedOff: NotBlankString? = null,
    /* Scheduled runway arrival time. */
    val scheduledOn: NotBlankString? = null,
    /* Estimated runway arrival time. */
    val estimatedOn: NotBlankString? = null,
    /*Scheduled gate arrival time. */
    val scheduledIn: NotBlankString? = null,
    /* Estimated gate arrival time. */
    val estimatedIn: NotBlankString? = null,
    /* Actual gate arrival time. */
    val actualIn: NotBlankString? = null,
    /* Actual runway departure time. */
    val actualOff: NotBlankString? = null,
    /* Actual runway arrival time. */
    val actualOn: NotBlankString? = null,
    /* Actual departure runway at origin, when known */
    val actualRunwayOff: NotBlankString? = null,
    /* Actual arrival runway at destination, when known */
    val actualRunwayOn: NotBlankString? = null,

    val foresightPredictionsAvailable: Boolean = false,
) : Serializable

@OptIn(ExperimentalKotoolsTypesApi::class)
fun Flight.toSearchFlightModel(): SearchFlightModel = SearchFlightModel(
    faFlightID = this.faFlightID ?: NotBlankString.create("N/A"),
    operatorID = this.operatorID ?: NotBlankString.create("N/A"),
    operatorName = this.operatorID ?: NotBlankString.create("N/A"),
    operatorICAO = this.identICAO,
    operatorIATA = this.identIATA,
    origin = this.origin?.toOriginDestinationModel(),
    destination = this.destination?.toOriginDestinationModel(),
    status = this.status ?: NotBlankString.create("N/A"),
    progress = this.progress,
    waypoints = this.waypoints,
    firstTimePosition = this.firstTimePosition,
    boundingBox = this.boundingBox,
    identPrefix = this.identPrefix,
    aircraftType = this.aircraftType,
    type = this.type?.let { FlightType.valueOf(it) } ?: FlightType.UNKNOWN,
    gateOrigin = this.gateOrigin,
    gateDestination = this.gateDestination,
    terminalOrigin = this.terminalOrigin,
    terminalDestination = this.terminalDestination,
    /* Scheduled gate departure time. */
    scheduledOut = this.scheduledOut,
    /* Estimated gate departure time. */
    estimatedOut = this.estimatedOut,
    /* Actual gate departure time. */
    actualOut = this.actualOut,
    /* Scheduled runway departure time. */
    scheduledOff = this.scheduledOff,
    /* Estimated runway departure time. */
    estimatedOff = this.estimatedOff,
    /* Scheduled runway arrival time. */
    scheduledOn = this.scheduledOn,
    /* Estimated runway arrival time. */
    estimatedOn = this.estimatedOn,
    /*Scheduled gate arrival time. */
    scheduledIn = this.scheduledIn,
    /* Estimated gate arrival time. */
    estimatedIn = this.estimatedIn,
    /* Actual gate arrival time. */
    actualIn = this.actualIn,
    actualOff = this.actualOff,
    actualOn = this.actualOn,
    foresightPredictionsAvailable = this.foresightPredictionsAvailable ?: false
)

fun Segment.toSearchFlightModel(): SearchFlightModel = SearchFlightModel(
    faFlightID = this.faFlightID,
    operatorName = this.operator,
    operatorID = this.operatorID,
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
    actualIn = this.actualIn,
    actualRunwayOff = this.actualRunwayOff,
    actualRunwayOn = this.actualRunwayOn,
)