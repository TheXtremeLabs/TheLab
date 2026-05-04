package com.riders.thelab.core.data.mapper

import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.model.weather.Coordinates

////////////////////////////////////////////////
// --- Model
////////////////////////////////////////////////
fun City.toModel(): CityModel = CityModel(
    name = this.name,
    state = this.state ?: "",
    country = this.country,
    longitude = this.coordinates.longitude,
    latitude = this.coordinates.latitude,
    uuid = this.uuid ?: ""
)

////////////////////////////////////////////////
// --- DTO
////////////////////////////////////////////////
fun City.toDTO(): com.riders.thelab.core.data.remote.dto.weather.City =
    com.riders.thelab.core.data.remote.dto.weather.City(
        id = this.id.toDouble(),
        name = this.name,
        state = this.state ?: "",
        country = this.country,
        coordinates = com.riders.thelab.core.data.remote.dto.weather.Coordinates(
            latitude = this.coordinates.latitude,
            longitude = this.coordinates.longitude
        )
    )

////////////////////////////////////////////////
// --- Domain
////////////////////////////////////////////////
fun CityModel.toDomainModel(): City = City(
    name = this.name,
    state = this.state,
    country = this.country,
    coordinates = Coordinates(
        longitude = this.longitude,
        latitude = this.latitude
    ),
    uuid = this.uuid
).apply {
    this.id = this@toDomainModel.id.toDouble()
}