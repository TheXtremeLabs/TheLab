package com.riders.thelab.ui.weather;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.riders.thelab.data.local.bean.Hours;
import com.riders.thelab.data.remote.dto.weather.CurrentWeather;
import com.riders.thelab.utils.Constants;
import com.riders.thelab.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class WeatherUtils {

    private WeatherUtils() {
    }

    public static String getWeatherIconFromApi(String weatherIconId) {
        return Constants.BASE_ENDPOINT_WEATHER_ICON + weatherIconId + Constants.WEATHER_ICON_SUFFIX;
    }

    public static void stylingChartGrid(LineChart chart, final int whiteColor) {

        // Styling
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setTouchEnabled(false);
        chart.setClickable(false);

        chart.setDoubleTapToZoomEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawBorders(false);
        chart.setDrawGridBackground(false);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisLeft().setDrawAxisLine(false);

        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setDrawAxisLine(false);

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.getAxisLeft().setTextColor(whiteColor); // left y-axis
        chart.getXAxis().setTextColor(whiteColor);
        chart.getLegend().setTextColor(whiteColor);
        chart.getDescription().setTextColor(whiteColor);

        Legend l = chart.getLegend();
        l.setEnabled(false);
    }

    public static List<Float> getWeatherTemperaturesForNextHours(List<CurrentWeather> hourlyWeather) {
        List<Float> temperatures = new ArrayList<>();

        for (int i = 0; i < hourlyWeather.size(); i++) {
            String hour =
                    DateTimeUtils
                            .formatMillisToTimeHoursMinutes(hourlyWeather.get(i).getDateTimeUTC())
                            .split(":")[0];

            if (hour.equals("00")) {
                break;
            } else {
                for (Hours element : Hours.values())
                    if (element.getHourValue().equals(hour))
                        temperatures.add((float) hourlyWeather.get(i).getTemperature());
            }
        }

        return temperatures;
    }

    public static String[] getWeatherTemperaturesQuarters(List<CurrentWeather> hourlyWeather) {
        List<String> temperaturesQuarters = new ArrayList<>();

        for (int i = 0; i < hourlyWeather.size(); i++) {

            String hour =
                    DateTimeUtils
                            .formatMillisToTimeHoursMinutes(hourlyWeather.get(i).getDateTimeUTC());

            String hourSplit =
                    DateTimeUtils
                            .formatMillisToTimeHoursMinutes(hourlyWeather.get(i).getDateTimeUTC())
                            .split(":")[0];

            if (hourSplit.equals("00")) {
                Timber.e("hour.equals(\"00\")");
                break;
            } else {
                for (Hours element : Hours.values())
                    if (element.getHourValue().equals(hourSplit)) {
                        temperaturesQuarters.add(hour);
                    }
            }
        }

        return temperaturesQuarters.toArray(new String[0]);
    }
}
