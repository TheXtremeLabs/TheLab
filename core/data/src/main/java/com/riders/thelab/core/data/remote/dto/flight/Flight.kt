package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import kotools.types.text.NotBlankString
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Flight(
    @SerialName("ident")
    val operatorID: NotBlankString,
    @SerialName("ident_icao")
    val identICAO: NotBlankString? = null,
    @SerialName("ident_iata")
    val identIATA: NotBlankString? = null,
    @SerialName("fa_flight_id")
    val faFlightID: NotBlankString,
    @SerialName("origin")
    val origin: Origin? = null,
    @SerialName("destination")
    val destination: Destination? = null,
    @SerialName("waypoints")
    val waypoints: Int? = null,
    @SerialName("first_position_time")
    val firstTimePosition: NotBlankString?,
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
    /* Actual runway departure time. */
    @SerialName("actual_off")
    val actualOff: NotBlankString? = null,
    /* Actual runway arrival time. */
    @SerialName("actual_on")
    val actualOn: NotBlankString? = null,
    @SerialName("foresight_predictions_available")
    val foresightPredictionsAvailable: Boolean,
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
