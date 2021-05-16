package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.remote.dto.weather.DailyWeather;
import com.riders.thelab.utils.Constants;
import com.riders.thelab.utils.DateTimeUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class WeatherForecastViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    @BindView(R.id.row_tv_forecast_day)
    MaterialTextView tvForecastDay;
    @BindView(R.id.row_iv_forecast_weather)
    ShapeableImageView ivForecastWeather;
    @BindView(R.id.row_tv_forecast_temperature)
    MaterialTextView tvForecastTemperature;

    public WeatherForecastViewHolder(@NonNull Context context,
                                     @NonNull @NotNull View itemView) {
        super(itemView);

        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("SetTextI18n")
    public void bindData(DailyWeather dailyWeather) {

        tvForecastDay.setText(
                DateTimeUtils.formatMillisToTimeDayMonth(dailyWeather.getDateTimeUTC()));

        // Load weather icon
        Glide.with(context)
                .load(WeatherUtils.getWeatherIconFromApi(
                        dailyWeather
                                .getWeather()
                                .get(0)
                                .getIcon()))
                .into(ivForecastWeather);

        tvForecastTemperature.setText(
                (int) dailyWeather.getTemperature().getDay() +
                        context.getString(R.string.degree_placeholder));
    }
}
