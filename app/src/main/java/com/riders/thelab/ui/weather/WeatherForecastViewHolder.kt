package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.remote.dto.weather.DailyWeather
import com.riders.thelab.databinding.RowWeatherForecastBinding
import com.riders.thelab.utils.DateTimeUtils

class WeatherForecastViewHolder(
    private val context: Context,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val viewBinding: RowWeatherForecastBinding = RowWeatherForecastBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bindData(dailyWeather: DailyWeather) {
        viewBinding.dailyWeather = dailyWeather
        // now bind the companion object to the variable declared in the XML
        viewBinding.dateUtils = DateTimeUtils.Companion
    }
}