package com.riders.thelab.ui.weather;

import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.view.Menu;
import android.view.MenuItem;

import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse;
import com.riders.thelab.ui.base.BaseView;

public interface WeatherContract {

    interface View extends BaseView {

        void onPause();

        void onResume();

        void onCreateOptionsMenu(Menu menu);

        void onOptionsItemSelected(MenuItem item);

        void showLoader();

        void hideLoader();

        void updateDownloadStatus(String statusMessage);

        void updateOneCallUI(OneCallWeatherResponse oneCallWeatherResponse);

        void onNoConnectionDetected();

        void onFetchCitySuccessful();

        void onFetchCityError();

        void onFetchDataFromFileError(String errorMessage);
    }

    interface Presenter {

        boolean canGetLocation();

        Cursor getCityQuery(String cityQuery);

        void getCitiesData();

        Address getCityNameWithCoordinates(double latitude, double longitude);

        void getCurrentWeather();

        void getWeather(Location location);

        void clearDisposables();

    }
}
