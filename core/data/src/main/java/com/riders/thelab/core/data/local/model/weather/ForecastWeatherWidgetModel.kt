package com.riders.thelab.core.data.local.model.weather

import java.io.Serializable

@kotlinx.serialization.Serializable
data class ForecastWeatherWidgetModel(
    val day:String,
    val temperature: TemperatureModel,
    var icon: String
) : Serializable {

    constructor() : this("", TemperatureModel(), "")
}
