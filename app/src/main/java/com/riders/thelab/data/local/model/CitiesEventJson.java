package com.riders.thelab.data.local.model;

import com.riders.thelab.data.remote.dto.weather.City;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * Moshi implementation (Gson library replacement)
 * <p>
 * Moshi is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects:
 * <p>
 * Reference : https://github.com/square/moshi
 */
@Getter
@ToString
public class CitiesEventJson {

    List<City> citiesList;
}
