package com.riders.thelab.feature.weather.utils

fun String.toWeatherIconFullUrl(): String =
    "${Constants.BASE_ENDPOINT_WEATHER_ICON}${this}${Constants.WEATHER_ICON_SUFFIX}"