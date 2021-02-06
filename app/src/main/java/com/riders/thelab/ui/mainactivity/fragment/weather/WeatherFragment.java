package com.riders.thelab.ui.mainactivity.fragment.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.bus.LocationFetchedEvent;
import com.riders.thelab.core.utils.LabLocationManager;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;
import com.riders.thelab.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class WeatherFragment extends Fragment {

    private Context context;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.weather_data_container)
    LinearLayout weatherDataContainer;
    @BindView(R.id.iv_weather_icon)
    ShapeableImageView ivWeatherIcon;
    @BindView(R.id.tv_weather_city_name)
    MaterialTextView tvWeatherCityName;
    @BindView(R.id.tv_weather_city_country)
    MaterialTextView tvWeatherCityCountry;
    @BindView(R.id.tv_weather_main_description)
    MaterialTextView tvWeatherDescription;
    @BindView(R.id.tv_weather_city_temperature)
    MaterialTextView tvWeatherCityTemperature;

    Unbinder unbinder;

    private final CompositeDisposable compositeDisposable;

    @Inject
    LabService service;


    @Inject
    public WeatherFragment() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause()");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume()");
        EventBus.getDefault().register(this);

        LabLocationManager labLocationManager = new LabLocationManager(context);

        if (!labLocationManager.canGetLocation()) {

            Timber.e("Cannot get location please enable position");
            labLocationManager.showSettingsAlert();
        } else {
            labLocationManager.setActivity(getActivity());
            labLocationManager.setLocationListener();
            labLocationManager.getLocation();
        }
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

        getWeather(location);
    }

    public void getWeather(Location location) {

        showLoader();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Disposable disposable =
                service.getWeather(latitude, longitude)
                        .subscribe(
                                weatherResponse -> {

                                    if (200 != weatherResponse.getCode()) {
                                        Timber.e("error code : %s", weatherResponse.getCode());
                                        hideLoader();
                                    } else {

                                        hideLoader();
                                        updateUI(weatherResponse);
                                    }
                                }, throwable -> {
                                    Timber.e(throwable);
                                    hideLoader();
                                });

        compositeDisposable.add(disposable);
    }


    private void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }

    public void updateUI(WeatherResponse weatherResponse) {
        Timber.d("updateUI()");

        if (weatherDataContainer.getVisibility() == View.GONE) {
            weatherDataContainer.setVisibility(View.VISIBLE);
        }

        // Load weather icon
        Glide.with(context)
                .load(getWeatherIconUrl(weatherResponse.getWeather().get(0).getIcon()))
                .into(ivWeatherIcon);

        // Load city name
        String cityName = weatherResponse.getName() +
                context.getResources().getString(R.string.separator_placeholder);
        tvWeatherCityName.setText(cityName);
        tvWeatherCityCountry.setText(weatherResponse.getSystem().getCountry());
        tvWeatherDescription.setText(weatherResponse.getWeather().get(0).getDescription());

        tvWeatherCityTemperature.setText((int) Math.round(weatherResponse.getMain().getTemperature()) + "");
    }

    public String getWeatherIconUrl(String weatherIconId) {
        return Constants.BASE_ENDPOINT_WEATHER_ICON + weatherIconId + Constants.WEATHER_ICON_SUFFIX;
    }


    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView()");
        if (null != unbinder)
            unbinder.unbind();

        if (null != compositeDisposable)
            // don't send events once the activity is destroyed
            compositeDisposable.clear();

        super.onDestroyView();
    }
}
