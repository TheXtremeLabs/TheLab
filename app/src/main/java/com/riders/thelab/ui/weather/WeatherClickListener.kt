package com.riders.thelab.ui.weather

import com.riders.thelab.data.local.model.weather.CityModel

interface WeatherClickListener {
    fun onWeatherItemClicked(cityModel: CityModel)
}