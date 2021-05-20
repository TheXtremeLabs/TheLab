package com.riders.thelab.ui.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.R
import com.riders.thelab.data.remote.dto.weather.DailyWeather

class WeatherForecastAdapter constructor(
    val context: Context,
    val dailyWeatherList: List<DailyWeather>
) :
    RecyclerView.Adapter<WeatherForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        return WeatherForecastViewHolder(
            context,
            LayoutInflater
                .from(context)
                .inflate(R.layout.row_weather_forecast, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        val dailyWeather: DailyWeather = dailyWeatherList[position]
        holder.bindData(dailyWeather)
    }

    override fun getItemCount(): Int {
        return if (dailyWeatherList.isNotEmpty()) dailyWeatherList.size else 0
    }
}