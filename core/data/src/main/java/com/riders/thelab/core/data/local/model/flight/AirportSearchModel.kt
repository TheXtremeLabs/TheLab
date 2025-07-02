package com.riders.thelab.core.data.local.model.flight

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.AirportSearch
import org.kotools.types.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString

/**
 * Represents the local model of [AirportSearch] to be used in app
 */
@Stable
@Immutable
data class AirportSearchModel(
    val city: NotBlankString? = null,
    val name: NotBlankString? = null,
    val description: NotBlankString? = null,
    val icaoCode: NotBlankString? = null,
    val iataCode: NotBlankString? = null
)

@OptIn(ExperimentalKotoolsTypesApi::class)
fun AirportSearch.toAirportSearchModel(): AirportSearchModel = AirportSearchModel(
    city = this.cityName?.let { it.toNotBlankString().getOrThrow() },
    name = this.description?.let { it.toNotBlankString().getOrThrow() },
    description = this.description?.let { it.toNotBlankString().getOrThrow() },
    icaoCode = this.icaoCode?.let { it.toNotBlankString().getOrThrow() },
    iataCode = this.iataCode?.let {
        if (it.isNotBlank()) {
            it.toNotBlankString().getOrThrow()
        } else {
            null
        }
    }
)