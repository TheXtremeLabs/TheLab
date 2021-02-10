package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.bus.LocationFetchedEvent;
import com.riders.thelab.core.utils.LabLocationManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.local.model.weather.CityModel;
import com.riders.thelab.data.remote.dto.weather.WeatherResponse;
import com.riders.thelab.ui.base.BaseViewImpl;
import com.riders.thelab.utils.Constants;
import com.riders.thelab.utils.DateTimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class WeatherView extends BaseViewImpl<WeatherPresenter>
        implements WeatherContract.View, MaterialTextView.OnEditorActionListener,
        AdapterView.OnItemClickListener, LocationListener {

    private WeatherActivity context;

    // Views
    @BindView(R.id.weather_root_view)
    CoordinatorLayout rootView;
    // Loading
    @BindView(R.id.progressBar)
    LinearProgressIndicator progressBar;
    @BindView(R.id.tvDownloadStatus)
    MaterialTextView tvDownloadStatus;

    // Weather container
    @BindView(R.id.weather_data_container)
    RelativeLayout weatherDataContainer;
    @BindView(R.id.ac_tv_Weather)
    MaterialAutoCompleteTextView acTvWeather;
    // City
    @BindView(R.id.tv_weather_city_name)
    MaterialTextView tvWeatherCityName;
    @BindView(R.id.tv_weather_city_country)
    MaterialTextView tvWeatherCityCountry;
    // Icon / Temperatures
    @BindView(R.id.iv_weather_icon)
    ShapeableImageView ivWeatherIcon;
    @BindView(R.id.tv_weather_main_description)
    MaterialTextView tvWeatherDescription;
    @BindView(R.id.tv_weather_city_temperature)
    MaterialTextView tvWeatherCityTemperature;
    @BindView(R.id.tv_weather_city_real_feels)
    MaterialTextView tvWeatherRealFeels;
    // Sunrise / Sunset
    @BindView(R.id.tv_sunrise)
    MaterialTextView tvWeatherSunrise;
    @BindView(R.id.tv_sunset)
    MaterialTextView tvWeatherSunset;
    // Extras
    @BindView(R.id.tv_weather_extra_cloudiness)
    MaterialTextView tvWeatherExtraCloudiness;
    @BindView(R.id.tv_weather_extra_pressure)
    MaterialTextView tvWeatherExtraPressure;
    @BindView(R.id.tv_weather_extra_wind_speed)
    MaterialTextView tvWeatherExtraWindSpeed;
    @BindView(R.id.tv_weather_extra_wind_direction)
    MaterialTextView tvWeatherExtraWindDirection;
    @BindView(R.id.tv_weather_extra_humidity)
    MaterialTextView tvWeatherExtraHumidity;


    private SearchView searchView;

    private List<CityModel> citiesList;
    private WeatherCityAdapter mAdapter;

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

        getPresenter().getCitiesData();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu) {
        context.getMenuInflater()
                .inflate(R.menu.menu_weather, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(context.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 3)
                    // filter recycler view when query submitted
                    mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() >= 3)
                    // filter recycler view when text is changed
                    mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_search:
                Timber.d("noinspection SimplifiableIfStatement");
                break;

            case R.id.action_position:
                getPresenter().getCurrentWeather();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
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
        progressBar.setVisibility(View.GONE);
        tvDownloadStatus.setVisibility(View.GONE);
    }

    @Override
    public void updateDownloadStatus(String statusMessage) {
        tvDownloadStatus.setText(statusMessage);
    }

    @Override
    public void updateUI(WeatherResponse weatherResponse) {
        Timber.d("updateUI()");

        // Load weather icon
        Glide.with(context)
                .load(getWeatherIconFromApi(
                        weatherResponse
                                .getWeather()
                                .get(0)
                                .getIcon()))
                .into(ivWeatherIcon);

        // Load city name
        String cityName = weatherResponse.getName() +
                context.getResources().getString(R.string.separator_placeholder);
        tvWeatherCityName.setText(cityName);
        tvWeatherCityCountry.setText(weatherResponse.getSystem().getCountry());
        tvWeatherDescription.setText(weatherResponse.getWeather().get(0).getDescription());

        // Temperatures
        String temperature = (int) Math.round(weatherResponse.getMain().getTemperature()) +
                context.getResources().getString(R.string.degree_placeholder);
        tvWeatherCityTemperature.setText(temperature);
        String realFeels = (int) Math.round(weatherResponse.getMain().getFeelsLike()) +
                context.getResources().getString(R.string.degree_placeholder);
        tvWeatherRealFeels.setText(realFeels);

        tvWeatherSunrise.setText(
                DateTimeUtils.formatMillisToTimeHoursMinutes(
                        weatherResponse
                                .getSystem()
                                .getSunrise()));

        tvWeatherSunset.setText(
                DateTimeUtils.formatMillisToTimeHoursMinutes(
                        weatherResponse
                                .getSystem()
                                .getSunset()));

        String cloudiness = weatherResponse.getClouds().getCloudiness() + " " +
                context.getResources().getString(R.string.percent_placeholder);
        tvWeatherExtraCloudiness.setText(cloudiness);

        String humidity = weatherResponse.getMain().getHumidity() + " " +
                context.getResources().getString(R.string.percent_placeholder);
        tvWeatherExtraHumidity.setText(humidity);

        String pressure = weatherResponse.getMain().getPressure() + " " +
                context.getResources().getString(R.string.pressure_unit_placeholder);
        tvWeatherExtraPressure.setText(pressure);

        String wind =
                weatherResponse.getWind().getSpeed() + " " +
                        context.getResources().getString(R.string.kilometer_unit_placeholder);
        tvWeatherExtraWindSpeed.setText(wind);

        String windDirection =
                weatherResponse.getWind().getDegree() + " ";
        tvWeatherExtraWindDirection.setText(windDirection);
    }

    @Override
    public void onNoConnectionDetected() {
        context
                .getLayoutInflater()
                .inflate(R.layout.no_internet_connection, rootView, true);
    }

    @Override
    public void onFetchCitySuccessful(List<CityModel> cityList) {
        Timber.d("onFetchCitySuccessful()");

        weatherDataContainer.setVisibility(View.VISIBLE);

        this.citiesList = cityList;

        mAdapter = new WeatherCityAdapter(
                context,
                R.layout.row_city_spinner,
                (ArrayList<CityModel>) citiesList);

        // Set the minimum number of characters, to show suggestions
        acTvWeather.setThreshold(3);
        acTvWeather.setAdapter(mAdapter);
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
        acTvWeather.setOnItemClickListener(this);
        acTvWeather.setOnEditorActionListener(this);
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
            inputManager.hideSoftInputFromWindow(
                    v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        CityModel city = (CityModel) parent.getItemAtPosition(position);

        String szCity = city.getName() + "," + city.getCountry();
        Timber.d("position selected, with value of %s", city);

        getPresenter().getWeather(szCity.toLowerCase());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                || (actionId == EditorInfo.IME_ACTION_DONE)) {
            Timber.e("Done pressed");

            String cityEntered = acTvWeather.getText().toString();
            getPresenter().getWeather(cityEntered);
        }
        return false;
    }
}
