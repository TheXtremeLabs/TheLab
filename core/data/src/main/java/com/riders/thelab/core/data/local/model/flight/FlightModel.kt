package com.riders.thelab.core.data.local.model.flight

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.Flight
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import java.io.Serializable

@Stable
@kotlinx.serialization.Serializable
data class FlightModel(
    val faFlightID: NotBlankString,

    val operatorID: NotBlankString,
    val identICAO: NotBlankString? = null,
    val identIATA: NotBlankString? = null,

    val origin: OriginDestinationModel? = null,
    val destination: OriginDestinationModel? = null,

    val waypoints: Int? = null,
    val firstTimePosition: NotBlankString?,

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

    val foresightPredictionsAvailable: Boolean,

    val segments: List<SegmentModel>? = null,
) : Serializable

@OptIn(ExperimentalKotoolsTypesApi::class)
fun Flight.toModel(): FlightModel = FlightModel(
    faFlightID = this.faFlightID ?: NotBlankString.create("N/A"),
    operatorID = this.operatorID ?: NotBlankString.create("N/A"),
    identICAO = this.identICAO,
    identIATA = this.identIATA,
    origin = this.origin?.toModel(),
    destination = this.destination?.toModel(),
    status = this.status ?: NotBlankString.create("N/A"),
    progress = this.progress,
    waypoints = this.waypoints,
    firstTimePosition = this.firstTimePosition,
    boundingBox = this.boundingBox,
    identPrefix = this.identPrefix,
    aircraftType = this.aircraftType,
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
    foresightPredictionsAvailable = this.foresightPredictionsAvailable ?: false,
    segments = this.segments?.map { it.toModel() },
)
