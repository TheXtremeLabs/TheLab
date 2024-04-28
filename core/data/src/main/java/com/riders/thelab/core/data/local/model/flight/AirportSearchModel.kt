package com.riders.thelab.core.data.local.model.flight

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.flight.AirportSearch
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

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
fun AirportSearch.toModel(): AirportSearchModel = AirportSearchModel(
    city = this.cityName?.let { NotBlankString.create(it) },
    name = this.description?.let { NotBlankString.create(it) },
    description = this.description?.let { NotBlankString.create(it) },
    icaoCode = this.icaoCode?.let { NotBlankString.create(it) },
    iataCode = this.iataCode?.let {
        if (it.isNotBlank()) {
            NotBlankString.create(it)
        } else {
            null
        }
    }
)