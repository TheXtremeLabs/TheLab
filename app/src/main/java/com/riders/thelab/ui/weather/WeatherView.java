package com.riders.thelab.ui.weather;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.bus.LocationFetchedEvent;
import com.riders.thelab.core.utils.LabLocationManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.local.LabRepository;
import com.riders.thelab.data.local.bean.SnackBarType;
import com.riders.thelab.data.local.bean.WindDirection;
import com.riders.thelab.data.remote.dto.weather.CurrentWeather;
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse;
import com.riders.thelab.ui.base.BaseViewImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@SuppressLint({"NonConstantResourceId", "NewApi"})
public class WeatherView extends BaseViewImpl<WeatherPresenter>
        implements WeatherContract.View, WeatherClickListener {

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
    ConstraintLayout weatherDataContainer;
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
    @BindView(R.id.iv_weather_extra_wind_direction_icon)
    ShapeableImageView ivWeatherExtraWindDirectionIcon;
    @BindView(R.id.tv_weather_extra_wind_direction_short_name)
    MaterialTextView tvWeatherExtraWindDirectionShortName;
    @BindView(R.id.tv_weather_extra_humidity)
    MaterialTextView tvWeatherExtraHumidity;
    @BindView(R.id.rv_forecast_five_days)
    RecyclerView rvForecastFiveDays;

    private SearchView mSearchView;

    CompositeDisposable compositeDisposable;


    @Inject
    LabRepository repository;

    @Inject
    WeatherView(WeatherActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Timber.d("onCreate()");
        getPresenter().attachView(this);

        Objects.requireNonNull(context.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_weather));

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu) {
        context.getMenuInflater()
                .inflate(R.menu.menu_weather, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(context.getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        ((AutoCompleteTextView) mSearchView.findViewById(R.id.search_src_text)).setThreshold(3);

        // listening to search query text change
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getCitiesFromDb(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // Set the minimum number of characters, to show suggestions
                getCitiesFromDb(query);
                return true;
            }

            @SuppressLint("CheckResult")
            private void getCitiesFromDb(String searchText) {
                searchText = "%" + searchText + "%";
                Observable
                        .just(searchText)
                        .map(searchQueryText -> getPresenter().getCityQuery(searchQueryText))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleResults,
                                this::handleError);
            }

            private void handleResults(Cursor cursor) {
                mSearchView.setSuggestionsAdapter(
                        new WeatherSearchViewAdapter(
                                context,
                                cursor,
                                mSearchView,
                                WeatherView.this));
            }

            private void handleError(Throwable t) {
                Timber.e(t);
                Toast.makeText(context, "Problem in Fetching Deals",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_position: {
                if (getPresenter().canGetLocation())
                    getPresenter().getCurrentWeather();
                else
                    UIManager
                            .showActionInSnackBar(
                                    context,
                                    context.findViewById(android.R.id.content),
                                    "Cannot get location please enable device's position setting.",
                                    SnackBarType.ALERT,
                                    context.getString(R.string.action_ok),
                                    v -> {
                                    });
                break;
            }

            //noinspection SimplifiableIfStatement
            case R.id.action_search:
                Timber.d("noinspection SimplifiableIfStatement");
                break;

            default:
                break;
        }
    }

    @Override
    public void onPause() {
        Timber.d("onPause()");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        Timber.d("onResume()");
        EventBus.getDefault().register(this);
        getPresenter().getCitiesData();
    }

    @Override
    public void onDestroy() {
        Timber.e("onDestroy()");
        compositeDisposable.clear();

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
    public void updateOneCallUI(OneCallWeatherResponse oneCallWeatherResponse) {
        Timber.d("updateOneCallUI()");

        // Load weather icon
        Glide.with(context)
                .load(
                        WeatherUtils.getWeatherIconFromApi(
                                oneCallWeatherResponse
                                        .getCurrentWeather()
                                        .getWeather()
                                        .get(0)
                                        .getIcon()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Timber.e("error %s", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Timber.e("Image weather bien chargée");
                        return false;
                    }
                })
                .into(ivWeatherIcon);

        Address address =
                getPresenter()
                        .getCityNameWithCoordinates(
                                oneCallWeatherResponse.getLatitude(),
                                oneCallWeatherResponse.getLongitude());

        // Load city name
        String cityName = address.getLocality() + context.getResources().getString(R.string.separator_placeholder);
        tvWeatherCityName.setText(cityName);
        String country = address.getCountryName();
        tvWeatherCityCountry.setText(country);
        tvWeatherDescription.setText(
                oneCallWeatherResponse
                        .getCurrentWeather()
                        .getWeather()
                        .get(0)
                        .getDescription());


        Objects.requireNonNull(context.getSupportActionBar()).setTitle(
                cityName + " " + country);

        // Temperatures
        String temperature =
                (int) Math.round(oneCallWeatherResponse.getCurrentWeather().getTemperature()) + context.getResources().getString(R.string.degree_placeholder);
        tvWeatherCityTemperature.setText(temperature);
        String realFeels =
                (int) Math.round(oneCallWeatherResponse.getCurrentWeather().getFeelsLike()) + context.getResources().getString(R.string.degree_placeholder);
        tvWeatherRealFeels.setText(realFeels);

        tvWeatherSunrise.setText(
                DateTimeUtils.formatMillisToTimeHoursMinutes(
                        oneCallWeatherResponse.getCurrentWeather().getSunrise()));

        tvWeatherSunset.setText(
                DateTimeUtils.formatMillisToTimeHoursMinutes(
                        oneCallWeatherResponse.getCurrentWeather().getSunset()));

        List<CurrentWeather> hourlyWeather = oneCallWeatherResponse.getHourlyWeather();

        // Build chart with hourly weather data
        buildChart(hourlyWeather);

        String cloudiness = oneCallWeatherResponse.getCurrentWeather().getClouds() + " " + context.getResources().getString(R.string.percent_placeholder);
        tvWeatherExtraCloudiness.setText(cloudiness);

        String humidity = oneCallWeatherResponse.getCurrentWeather().getHumidity() + " " + context.getResources().getString(R.string.percent_placeholder);
        tvWeatherExtraHumidity.setText(humidity);

        String pressure = oneCallWeatherResponse.getCurrentWeather().getPressure() + " " + context.getResources().getString(R.string.pressure_unit_placeholder);
        tvWeatherExtraPressure.setText(pressure);

        // Wind
        String wind =
                oneCallWeatherResponse.getCurrentWeather().getWindSpeed() + " "
                        + context.getResources().getString(R.string.meter_unit_placeholder);
        tvWeatherExtraWindSpeed.setText(wind);

        WindDirection windDirection =
                WindDirection.getWindDirectionToTextualDescription(
                        oneCallWeatherResponse.getCurrentWeather().getWindDegree());

        Glide.with(context)
                .load(ContextCompat.getDrawable(context, windDirection.getIcon()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Timber.e("error %s", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Timber.e("Image wind bien chargée");
                        return false;
                    }
                })
                .into(ivWeatherExtraWindDirectionIcon);

        String windDirectionShortName = windDirection.getShortName() + " ";
        tvWeatherExtraWindDirectionShortName.setText(windDirectionShortName);

        WeatherForecastAdapter mAdapter =
                new WeatherForecastAdapter(
                        context,
                        oneCallWeatherResponse
                                .getDailyWeather()
                                .subList(1, oneCallWeatherResponse.getDailyWeather().size() - 2));

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        rvForecastFiveDays.setLayoutManager(linearLayoutManager);
        rvForecastFiveDays.setAdapter(mAdapter);
    }

    @Override
    public void onNoConnectionDetected() {
        context
                .getLayoutInflater()
                .inflate(R.layout.no_internet_connection, rootView, true);
    }

    @Override
    public void onFetchCitySuccessful() {
        Timber.d("onFetchCitySuccessful()");

        weatherDataContainer.setVisibility(View.VISIBLE);
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
        Timber.e("onLocationFetchedEvent() - "
                + event.getLocation().getLatitude()
                + ", "
                + event.getLocation().getLongitude());

        getPresenter().getWeather(
                LabLocationManager.buildTargetLocationObject(
                        event.getLocation().getLatitude(),
                        event.getLocation().getLongitude()
                ));
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    public void buildChart(List<CurrentWeather> hourlyWeather) {

        // in this example, a LineChart is initialized from xml
        LineChart chart = context.findViewById(R.id.weather_hourly_chart);

        int whiteColor = ContextCompat.getColor(context, R.color.white);
        /*
         * https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
         *
         */
        // Styling
        /*chart.setAutoScaleMinMaxEnabled(true);
        chart.setTouchEnabled(false);
        chart.setClickable(false);

        chart.setDoubleTapToZoomEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawBorders(false);
        chart.setDrawGridBackground(false);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisLeft().setDrawAxisLine(false);

        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setDrawAxisLine(false);

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.getAxisLeft().setTextColor(whiteColor); // left y-axis
        chart.getXAxis().setTextColor(whiteColor);
        chart.getLegend().setTextColor(whiteColor);
        chart.getDescription().setTextColor(whiteColor);

        Legend l = chart.getLegend();
        l.setEnabled(false);*/

        WeatherUtils.stylingChartGrid(chart, whiteColor);

        List<Float> temperatureForNextHours =
                WeatherUtils.getWeatherTemperaturesForNextHours(hourlyWeather);


        HashMap<Float, Float> integers = new HashMap<>();

        for (int i = 0; i < temperatureForNextHours.size(); i++) {
            integers.put((float) i, temperatureForNextHours.get(i));
        }

        List<Entry> entries = new ArrayList<>();

        for (Map.Entry<Float, Float> data : integers.entrySet()) {
            // turn your data into Entry objects
            entries.add(new Entry(data.getKey(), data.getValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(whiteColor);
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getPointLabel(Entry entry) {
                return (int) entry.getY() + context.getString(R.string.degree_placeholder);
            }
        });
        dataSet.setValueTextColor(whiteColor);
        dataSet.getValueTextColor(whiteColor);

        // the labels that should be drawn on the XAxis
        final String[] quarters = WeatherUtils.getWeatherTemperaturesQuarters(hourlyWeather);
        Timber.d("quarters value : %d", quarters.length);

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(
                quarters.length > 1
                        ? formatter
                        : new DefaultAxisValueFormatter(3));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    @SuppressLint("RestrictedApi")
    @Override
    public void onWeatherItemClicked(final CityModel cityModel) {
        UIManager.hideKeyboard(context, context.findViewById(android.R.id.content));

        getPresenter().getWeather(
                LabLocationManager.buildTargetLocationObject(
                        cityModel.getLatitude(),
                        cityModel.getLongitude()
                ));
    }
}
