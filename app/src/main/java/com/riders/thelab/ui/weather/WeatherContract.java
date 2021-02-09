package com.riders.thelab.ui.weather;

import android.view.Menu;
import android.view.MenuItem;

import com.riders.thelab.data.local.model.weather.CityModel;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface WeatherContract {

    interface View extends BaseView {

        void onStart();

        void onStop();

        void onCreateOptionsMenu(Menu menu);

        void onOptionsItemSelected(MenuItem item);

        void onBackPressed();

        void showLoader();

        void hideLoader();

        void updateDownloadStatus(String statusMessage);

        void updateUI(WeatherResponse weatherResponse);

        void onNoConnectionDetected();

        void onFetchCitySuccessful(List<CityModel> cityList);

        void onFetchCityError();

        void onFetchDataFromFileError(String errorMessage);
    }

    interface Presenter {

        void getCitiesData();

        void getCurrentWeather();

        void getWeather(String city);

        void clearDisposables();

    }
}
