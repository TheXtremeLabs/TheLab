package com.riders.thelab.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.remote.dto.weather.DailyWeather;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastViewHolder> {

    private final Context context;
    private final List<DailyWeather> dailyWeatherList;

    public WeatherForecastAdapter(Context context, List<DailyWeather> dailyWeatherList) {
        this.context = context;
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @NotNull
    @Override
    public WeatherForecastViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new WeatherForecastViewHolder(
                context,
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.row_weather_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WeatherForecastViewHolder holder, int position) {
        DailyWeather dailyWeather = dailyWeatherList.get(position);

        holder.bindData(dailyWeather);
    }

    @Override
    public int getItemCount() {
        if (!dailyWeatherList.isEmpty())
            return dailyWeatherList.size();

        return 0;
    }
}
