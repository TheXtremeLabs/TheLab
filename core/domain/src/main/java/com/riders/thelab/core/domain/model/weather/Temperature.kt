package com.riders.thelab.core.domain.model.weather

@kotlinx.serialization.Serializable
data class Temperature(
    val temperature: Double = 0.0,
    val realFeels: Double = 0.0,
    val day: Double = 0.0,
    var min: Double = 0.0,
    var max: Double = 0.0,
    val night: Double = 0.0,
    val evening: Double = 0.0,
    val morning: Double = 0.0
)