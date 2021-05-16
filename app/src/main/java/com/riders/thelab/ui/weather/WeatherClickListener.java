package com.riders.thelab.ui.weather;

import com.riders.thelab.data.local.model.weather.CityModel;

public interface WeatherClickListener {
    void onWeatherItemClicked(final CityModel cityModel);
}
