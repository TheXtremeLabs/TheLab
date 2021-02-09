package com.riders.thelab.data.local.model;

import com.riders.thelab.data.remote.dto.weather.City;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;
import com.squareup.moshi.FromJson;

import java.util.List;

/**
 * Moshi implementation (Gson library replacement)
 * <p>
 * Moshi is a modern JSON library for Android and Java.
 * It makes it easy to parse JSON into Java objects:
 * <p>
 * Reference : https://github.com/square/moshi
 */
public class CitiesEventJsonAdapter {

    @FromJson
    List<City> citiesFromJson(List<City> citiesEventJson) {
        return citiesEventJson;
    }
}
