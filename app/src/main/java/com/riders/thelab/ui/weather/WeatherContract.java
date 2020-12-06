package com.riders.thelab.ui.weather;

import com.riders.thelab.data.local.model.weather.City;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface WeatherContract {

    interface View extends BaseView {

        void onStart();

        void onStop();

        void showLoader();

        void hideLoader();

        void updateUI(WeatherResponse weatherResponse);

        void onNoConnectionDetected();

        void onFetchCitySuccessful(List<City> cityList);

        void onFetchCityError();

        void onFetchDataFromFileError(String errorMessage);
    }

    interface Presenter {

        void getCityDataFromFile();

        void getWeather(String city);

        void clearDisposables();

    }
}
