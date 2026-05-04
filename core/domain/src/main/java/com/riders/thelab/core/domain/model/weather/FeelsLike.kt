package com.riders.thelab.core.domain.model.weather

@kotlinx.serialization.Serializable
data class FeelsLike(
    val feelsLike: Double = 0.0,
    val day: Double = 0.0,
    val night: Double = 0.0,
    val evening: Double = 0.0,
    val morning: Double = 0.0
)
