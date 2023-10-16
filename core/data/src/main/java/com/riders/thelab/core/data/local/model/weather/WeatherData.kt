package com.riders.thelab.core.data.local.model.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherData constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("_id")
    var id: Int = 0,

    @ColumnInfo(name = "isWeatherData")
    var isWeatherData: Boolean = false
) {
    constructor(isWeatherData: Boolean) : this(0, isWeatherData)

}
