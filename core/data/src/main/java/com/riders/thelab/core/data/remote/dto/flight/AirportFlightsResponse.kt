package com.riders.thelab.core.data.remote.dto.flight

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class AirportFlightsResponse(
    @SerialName("links")
    val links: Link,
    @SerialName("num_pages")
    val numPages: Int,
    @SerialName("scheduled_arrivals")
    val scheduledArrivals: List<ScheduledArrivals>,
    @SerialName("scheduled_departures")
    val scheduledDepartures: List<ScheduledDepartures>,
    @SerialName("arrivals")
    val arrivals: List<Arrivals>,
    @SerialName("departures")
    val departures: List<Departures>,
) : Serializable
