package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CurrentWeather {
    @Json(name = "dt")
    private long dateTimeUTC;

    @Json(name = "sunrise")
    private long sunrise;

    @Json(name = "sunset")
    private long sunset;

    @Json(name = "moonrise")
    private long moonrise;

    @Json(name = "moonset")
    private long moonset;

    @Json(name = "moon_phase")
    private double moonPhase;

    @Json(name = "temp")
    private double temperature;

    @Json(name = "feels_like")
    private double feelsLike;

    @Json(name = "pressure")
    private int pressure;

    @Json(name = "humidity")
    private int humidity;

    @Json(name = "dew_point")
    private double dewPoint;

    // UV index
    @Json(name = "uvi")
    private double UVIndex;

    @Json(name = "clouds")
    private int clouds;

    // Average visibility, metres
    @Json(name = "visibility")
    private int visibility;

    @Json(name = "wind_speed")
    private double windSpeed;

    @Json(name = "wind_deg")
    private int windDegree;

    @Json(name = "wind_gust")
    private double windGust;

    @Json(name = "weather")
    private List<Weather> weather;

    @Json(name = "rain")
    private Rain rain;

    @Json(name = "snow")
    private Snow snow;

    // Probability of precipitation
    @Json(name = "pop")
    private double pop;
}
