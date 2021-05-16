package com.riders.thelab.data.local.model.weather;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "weather_data")
@Setter
@Getter
@ToString
public class WeatherData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    public int id;
    @ColumnInfo(name = "isWeatherData")
    public boolean isWeatherData;

    public WeatherData(boolean isWeatherData) {
        setWeatherData(isWeatherData);
    }
}
