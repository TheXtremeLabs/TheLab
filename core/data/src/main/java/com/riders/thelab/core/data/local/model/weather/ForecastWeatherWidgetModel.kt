package com.riders.thelab.core.data.local.model.weather

import java.io.Serializable

@kotlinx.serialization.Serializable
data class ForecastWeatherWidgetModel(
    val temperature: TemperatureModel,
    val icon: String
) : Serializable {

    constructor() : this(TemperatureModel(), "")
}
