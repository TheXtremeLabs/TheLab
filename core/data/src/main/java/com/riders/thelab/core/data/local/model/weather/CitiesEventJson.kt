package com.riders.thelab.core.data.local.model.weather

import com.riders.thelab.core.data.remote.dto.weather.City

/**
 * Moshi implementation (Gson library replacement)
 * <p>
 * Moshi is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects:
 * <p>
 * Reference : https://github.com/square/moshi
 */
data class CitiesEventJson(val citiesList: List<City>)
