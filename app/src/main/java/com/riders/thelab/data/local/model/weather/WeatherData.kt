package com.riders.thelab.data.local.model.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherData constructor(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: Int = 0,

    @ColumnInfo(name = "isWeatherData")
    val isWeatherData: Boolean = false
) {
    constructor(isWeatherData: Boolean) : this(0, isWeatherData)
}
