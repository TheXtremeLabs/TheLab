package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

@SuppressLint("NonConstantResourceId")
public class WeatherView extends BaseViewImpl<WeatherPresenter>
        implements WeatherContract.View, TextView.OnEditorActionListener, AdapterView.OnItemClickListener,
        LocationListener {

    private WeatherActivity context;

    // Views
    @BindView(R.id.autocompleteTV_city)
    AppCompatAutoCompleteTextView autoCompleteCityName;
    /*@BindView(R.id.sp_city_list)
    AppCompatSpinner spinner;*/
    @BindView(R.id.iv_weather_icon)
    ImageView ivWeatherIcon;
    @BindView(R.id.tv_weather_city_name)
    TextView tvWeatherCityName;
    @BindView(R.id.tv_weather_city_country)
    TextView tvWeatherCityCountry;
    @BindView(R.id.tv_weather_main_description)
    TextView tvWeatherDescription;
    @BindView(R.id.tv_weather_city_temperature)
    TextView tvWeatherCityTemperature;
    @BindView(R.id.tv_weather_extra_cloudiness)
    TextView tvWeatherExtraCloudiness;
    @BindView(R.id.tv_weather_extra_humidity)
    TextView tvWeatherExtraHumidity;
    @BindView(R.id.tv_weather_extra_pressure)
    TextView tvWeatherExtraPressure;
    @BindView(R.id.tv_weather_extra_wind)
    TextView tvWeatherExtraWind;
    @BindView(R.id.btn_current_location)
    Button currentLocationButton;

    @Inject
    WeatherView(WeatherActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getPresenter().attachView(this);

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();

    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onNoConnectionDetected() {

    }

    @Override
    public void onFetchedSuccessful(ArrayList<Video> youtubeList) {

    }

    @Override
    public void onFetchError() {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
