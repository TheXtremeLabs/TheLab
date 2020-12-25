package com.riders.thelab.ui.mainactivity.fragment.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class WeatherFragment extends Fragment {

    private Context context;

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

    Unbinder unbinder;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    LabService service;


    @Inject
    public WeatherFragment() {
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

        new LabLocationManager(context, getActivity());
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

                        getWeather(city);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e(Objects.requireNonNull(e.getMessage()));
                    }
                });
    }

    public void getWeather(String city) {

//        getView().showLoader();

        Disposable disposable =
                service.getWeather(city)
                        .subscribe(
                                weatherResponse -> {

                                    if (200 != weatherResponse.getCode()) {
                                        Timber.e("error code : %s", weatherResponse.getCode());
                                    } else {
//                                        getView().hideLoader();
                                        updateUI(weatherResponse);
                                    }
                                },
                                Timber::e);

        compositeDisposable.add(disposable);
    }

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
        tvWeatherCityCountry.setText(weatherResponse.getSys().getCountry());
        tvWeatherDescription.setText(weatherResponse.getWeather().get(0).getDescription());

        String temperature = (int) Math.round(weatherResponse.getMain().getTemperature()) +
                context.getResources().getString(R.string.degree_placeholder);
        tvWeatherCityTemperature.setText(temperature);
    }

    public String getWeatherIconFromApi(String weatherIconId) {
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
