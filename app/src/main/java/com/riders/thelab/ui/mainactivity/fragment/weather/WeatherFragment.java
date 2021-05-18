package com.riders.thelab.ui.mainactivity.fragment.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.riders.thelab.R;
import com.riders.thelab.core.bus.LocationFetchedEvent;
import com.riders.thelab.core.utils.LabLocationManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;
import com.riders.thelab.databinding.FragmentWeatherBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class WeatherFragment extends Fragment {

    private Context context;

    FragmentWeatherBinding viewBinding;

    @Inject
    LabService service;

    private final CompositeDisposable compositeDisposable;

    String cityName;
    String cityCountry;
    double cityTemperature;
    String cityWeatherDescription;
    String weatherIconURL;


    public WeatherFragment() {
        compositeDisposable = new CompositeDisposable();
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
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
        viewBinding = FragmentWeatherBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
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

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView()");

        // don't send events once the activity is destroyed
        compositeDisposable.clear();

        super.onDestroyView();
        viewBinding = null;
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

        UIManager.showView(viewBinding.progressBar);

        Disposable concatDisposable =
                Single
                        .concat(
                                service.getWeather(location),
                                service.getWeatherOneCallAPI(location))
                        .doOnComplete(() -> {
                            Timber.e(("OnComplete()"));
                            updateUI();
                        })
                        .doOnError(Timber::e)
                        .subscribe(response -> {
                            UIManager.hideView(viewBinding.progressBar);

                            if (response instanceof WeatherResponse) {
                                cityName = ((WeatherResponse) response).getName();
                                cityCountry = ((WeatherResponse) response).getSystem().getCountry();
                            }

                            if (response instanceof OneCallWeatherResponse) {
                                cityTemperature = ((OneCallWeatherResponse) response).getCurrentWeather().getTemperature();
                                weatherIconURL = ((OneCallWeatherResponse) response).getCurrentWeather().getWeather().get(0).getIcon();
                                cityWeatherDescription = ((OneCallWeatherResponse) response).getCurrentWeather().getWeather().get(0).getDescription();
                            }

                        }, throwable -> {
                            UIManager.hideView(viewBinding.progressBar);
                            Timber.e(throwable);
                        });

        compositeDisposable.add(concatDisposable);
    }


    public void updateUI() {
        Timber.d("updateUI()");

        UIManager.showView(viewBinding.weatherDataContainer);

        // Load weather icon
        Glide.with(context)
                .load(getWeatherIconUrl(weatherIconURL))
                .into(viewBinding.ivWeatherIcon);

        viewBinding.tvWeatherCityTemperature.setText(
                String.format(Locale.getDefault(), "%d", (int) Math.round(cityTemperature)));
        viewBinding.tvDegreePlaceholder.setVisibility(View.VISIBLE);

        // Load city name
        cityName += context.getResources().getString(R.string.separator_placeholder);
        viewBinding.tvWeatherCityName.setText(cityName);
        viewBinding.tvWeatherCityCountry.setText(cityCountry);
        viewBinding.tvWeatherMainDescription.setText(cityWeatherDescription);

    }

    public String getWeatherIconUrl(String weatherIconId) {
        return Constants.BASE_ENDPOINT_WEATHER_ICON + weatherIconId + Constants.WEATHER_ICON_SUFFIX;
    }
}
