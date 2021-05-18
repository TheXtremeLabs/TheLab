package com.riders.thelab.data.local.model.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import lombok.Getter
import lombok.Setter
import lombok.ToString

@Entity(tableName = "weather_data")
@Setter
@Getter
@ToString
data class WeatherData constructor(

        @PrimaryKey
        @ColumnInfo(name = "_id")
        var id: Int = 0,

        @ColumnInfo(name = "isWeatherData")
        var isWeatherData: Boolean = false
) {
    constructor(isWeatherData: Boolean) : this()
}
