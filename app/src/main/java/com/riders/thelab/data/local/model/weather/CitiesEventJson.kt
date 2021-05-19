package com.riders.thelab.data.local.model.weather


import com.riders.thelab.data.remote.dto.weather.City
import lombok.Getter

/**
 * Moshi implementation (Gson library replacement)
 * <p>
 * Moshi is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects:
 * <p>
 * Reference : https://github.com/square/moshi
 */
@Getter
data class CitiesEventJson(val citiesList: List<City>) {
}