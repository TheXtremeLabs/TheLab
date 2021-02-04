package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.bumptech.glide.Glide;
import com.riders.thelab.R;
import com.riders.thelab.core.bus.LocationFetchedEvent;
import com.riders.thelab.core.utils.LabLocationManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.local.model.weather.City;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;
import com.riders.thelab.ui.base.BaseViewImpl;
import com.riders.thelab.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class WeatherView extends BaseViewImpl<WeatherPresenter>
        implements WeatherContract.View, TextView.OnEditorActionListener,
        AdapterView.OnItemClickListener, LocationListener {

    private WeatherActivity context;

    // Views
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
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
        Timber.d("onCreate()");
        getPresenter().attachView(this);

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_weather));

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        setListeners();
    }

    @Override
    public void onStart() {
        Timber.d("onStart()");

        getPresenter().getCityDataFromFile();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        Timber.d("onStop()");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        Timber.e("onDestroy()");
        getPresenter().detachView();
        context = null;
    }

    @Override
    public void showLoader() {
        Timber.d("showLoader()");
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        Timber.d("hideLoader()");
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateUI(WeatherResponse weatherResponse) {
        Timber.d("updateUI()");

        // Load weather icon
        Glide.with(context)
                .load(getWeatherIconFromApi(weatherResponse.getWeather().get(0).getIcon()))
                .into(ivWeatherIcon);

        // Load city name
        String cityName = weatherResponse.getName() +
                context.getResources().getString(R.string.separator_placeholder);
        tvWeatherCityName.setText(cityName);
        tvWeatherCityCountry.setText(weatherResponse.getSystem().getCountry());
        tvWeatherDescription.setText(weatherResponse.getWeather().get(0).getDescription());

        String temperature = (int) Math.round(weatherResponse.getMain().getTemperature()) +
                context.getResources().getString(R.string.degree_placeholder);
        tvWeatherCityTemperature.setText(temperature);

        long sunriseMillis = weatherResponse.getSystem().getSunrise();
        long sunsetMillis = weatherResponse.getSystem().getSunset();

        Timber.d("sunrise time : %s", getPresenter().formatMillisToTimeHoursMinutesSeconds(sunriseMillis));

        Timber.d("cloudiness : %s", weatherResponse.getClouds().getCloudiness());

        String cloudiness = weatherResponse.getClouds().getCloudiness() + " " +
                context.getResources().getString(R.string.percent_placeholder);
        tvWeatherExtraCloudiness.setText(cloudiness);

        String humidity = weatherResponse.getMain().getHumidity() + " " +
                context.getResources().getString(R.string.percent_placeholder);
        tvWeatherExtraHumidity.setText(humidity);

        String pressure = weatherResponse.getMain().getPressure() + " " +
                context.getResources().getString(R.string.pressure_unit_placeholder);
        tvWeatherExtraPressure.setText(pressure);

        String wind = weatherResponse.getMain().getHumidity() + " " +
                context.getResources().getString(R.string.kilometer_unit_placeholder);
        tvWeatherExtraWind.setText(wind);
    }

    @Override
    public void onNoConnectionDetected() {

    }

    @Override
    public void onFetchCitySuccessful(List<City> cityList) {
        Timber.d("onFetchCitySuccessful()");

//        mSpinnerAdapter = new WeatherCityAdapter(context, cityList, this);
//        autoCompleteCityName.setAdapter(mSpinnerAdapter);
//        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(mSpinnerAdapter);

        WeatherCityAdapter mSpinnerAdapter =
                new WeatherCityAdapter(context, R.layout.row_city_spinner, (ArrayList<City>) cityList);
        // Set the minimum number of characters, to show suggestions
        autoCompleteCityName.setThreshold(1);
        autoCompleteCityName.setAdapter(mSpinnerAdapter);
    }

    @Override
    public void onFetchCityError() {
        Timber.e("onFetchCityError()");

    }

    @Override
    public void onFetchDataFromFileError(String errorMessage) {
        Timber.e("onFetchDataFromFileError()");
        UIManager.showActionInToast(
                context,
                "Unable to find file : " + errorMessage
        );

        autoCompleteCityName.setEnabled(false);
    }


    /////////////////////////////////////
    //
    // BUTTERKNIFE
    //
    /////////////////////////////////////
    @OnClick(R.id.btn_current_location)
    void onCurrentLocationButtonClicked() {

        new LabLocationManager(context, context);
    }

    /////////////////////////////////////
    //
    // BUS
    //
    /////////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationFetchedEventResult(LocationFetchedEvent event) {
        Timber.e("onLocationFetchedEvent()");

        Location location = event.getLocation();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Timber.e(latitude + ", " + longitude);

        LabLocationManager
                .getDeviceLocationWithRX(location, context)
                .subscribe(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull String city) {
                        Timber.e("final string city returned : %s", city);

                        getPresenter().getWeather(city);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e(Objects.requireNonNull(e.getMessage()));
                    }
                });
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private void setListeners() {
        Timber.d("setListeners()");
        autoCompleteCityName.setOnItemClickListener(this);
        autoCompleteCityName.setOnEditorActionListener(this);
    }


    public String getWeatherIconFromApi(String weatherIconId) {
        return Constants.BASE_ENDPOINT_WEATHER_ICON + weatherIconId + Constants.WEATHER_ICON_SUFFIX;
    }


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(context, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(context, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        View v = context.getCurrentFocus();

        if (v != null) {
            // Dismiss keyboard
            InputMethodManager inputManager =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        City city = (City) parent.getItemAtPosition(position);

        String szCity = city.getName() + "," + city.getCountry();
        Timber.d("position selected, with value of %s", city);

        getPresenter().getWeather(szCity.toLowerCase());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                || (actionId == EditorInfo.IME_ACTION_DONE)) {
            Timber.e("Done pressed");

            String cityEntered = autoCompleteCityName.getText().toString();
            getPresenter().getWeather(cityEntered);
        }
        return false;
    }
}
