package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riders.thelab.data.remote.dto.weather.DailyWeather
import com.riders.thelab.databinding.RowWeatherForecastBinding
import com.riders.thelab.utils.DateTimeUtils

class WeatherForecastViewHolder(
    val context: Context,
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val viewBinding: RowWeatherForecastBinding = RowWeatherForecastBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bindData(dailyWeather: DailyWeather) {
        viewBinding.dailyWeather = dailyWeather
        // now bind the companion object to the variable declared in the XML
        viewBinding.dateUtils = DateTimeUtils.Companion

        /*viewBinding.rowTvForecastDay.text =
            DateTimeUtils.formatMillisToTimeDayMonth(dailyWeather.dateTimeUTC)*/

        // Load weather icon
        /*Glide.with(context)
            .load(
                WeatherUtils.getWeatherIconFromApi(
                    dailyWeather
                        .weather[0]
                        .icon
                )
            )
            .into(viewBinding.rowIvForecastWeather)*/

        /* viewBinding.rowTvForecastTemperature.text =
             "${dailyWeather.temperature.day.toInt()} ${context.getString(R.string.degree_placeholder)}"*/
    }
}