package com.riders.thelab.ui.weather

import com.github.mikephil.charting.charts.LineChart
import com.riders.thelab.data.local.bean.Hours
import com.riders.thelab.data.remote.dto.weather.CurrentWeather
import com.riders.thelab.utils.Constants
import com.riders.thelab.utils.DateTimeUtils
import timber.log.Timber
import java.util.*

class WeatherUtils private constructor() {

    companion object {

        fun getWeatherIconFromApi(weatherIconId: String): String {
            return Constants.BASE_ENDPOINT_WEATHER_ICON + weatherIconId + Constants.WEATHER_ICON_SUFFIX
        }

        fun stylingChartGrid(chart: LineChart, whiteColor: Int) {

            // Styling
            chart.isAutoScaleMinMaxEnabled = true
            chart.setTouchEnabled(false)
            chart.isClickable = false
            chart.isDoubleTapToZoomEnabled = false
            chart.isDoubleTapToZoomEnabled = false
            chart.setDrawBorders(false)
            chart.setDrawGridBackground(false)
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chart.axisLeft.setDrawGridLines(false)
            chart.axisLeft.setDrawLabels(false)
            chart.axisLeft.setDrawAxisLine(false)
            chart.xAxis.setDrawGridLines(false)
            chart.xAxis.setDrawLabels(true)
            chart.xAxis.setDrawAxisLine(false)
            chart.axisRight.setDrawGridLines(false)
            chart.axisRight.setDrawLabels(false)
            chart.axisRight.setDrawAxisLine(false)
            chart.axisLeft.textColor = whiteColor // left y-axis
            chart.xAxis.textColor = whiteColor
            chart.legend.textColor = whiteColor
            chart.description.textColor = whiteColor
            val l = chart.legend
            l.isEnabled = false
        }

        fun getWeatherTemperaturesForNextHours(hourlyWeather: List<CurrentWeather>): List<Float> {
            val temperatures: MutableList<Float> = ArrayList()
            for (i in hourlyWeather.indices) {
                val hour: String =
                        DateTimeUtils
                                .formatMillisToTimeHoursMinutes(hourlyWeather[i].dateTimeUTC)
                                .split(":")[0]
                if (hour == "00") {
                    break
                } else {
                    for (element in Hours.values()) if (element.hourValue == hour) temperatures.add(hourlyWeather[i].temperature.toFloat())
                }
            }
            return temperatures
        }

        fun getWeatherTemperaturesQuarters(hourlyWeather: List<CurrentWeather>): Array<String> {
            val temperaturesQuarters: MutableList<String> = ArrayList()
            for (i in hourlyWeather.indices) {
                val hour: String =
                        DateTimeUtils
                                .formatMillisToTimeHoursMinutes(hourlyWeather[i].dateTimeUTC)
                val hourSplit: String =
                        DateTimeUtils
                                .formatMillisToTimeHoursMinutes(hourlyWeather[i].dateTimeUTC)
                                .split(":")[0]
                if (hourSplit == "00") {
                    Timber.e("hour.equals(\"00\")")
                    break
                } else {
                    for (element in Hours.values()) if (element.hourValue == hourSplit) {
                        temperaturesQuarters.add(hour)
                    }
                }
            }
            return temperaturesQuarters.toTypedArray()
        }

    }

}