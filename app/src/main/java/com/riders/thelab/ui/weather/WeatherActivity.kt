package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.app.SearchManager
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.location.Address
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.riders.thelab.R
import com.riders.thelab.core.bus.LocationFetchedEvent
import com.riders.thelab.core.utils.LabLocationUtils
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.local.bean.WindDirection
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.remote.dto.weather.CurrentWeather
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.databinding.ActivityWeatherBinding
import com.riders.thelab.utils.DateTimeUtils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity(), WeatherClickListener {

    private lateinit var viewBinding: ActivityWeatherBinding

    private val mWeatherViewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var repositoryImpl: RepositoryImpl

    private lateinit var context: WeatherActivity
    private lateinit var mSearchView: SearchView

    private lateinit var listener: WeatherClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        context = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.activity_title_weather)

        initViewModelObservers()
        listener = this
    }

    override fun onPause() {
        Timber.d("onPause()")
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
        EventBus.getDefault().register(this)
        mWeatherViewModel.fetchCities()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_weather, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = this.getSystemService(SEARCH_SERVICE) as SearchManager
        mSearchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        mSearchView.maxWidth = Int.MAX_VALUE

        (mSearchView.findViewById<View>(R.id.search_src_text) as AutoCompleteTextView)
            .threshold = 3

        // listening to search query text change
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getCitiesFromDb(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                // Set the minimum number of characters, to show suggestions
                getCitiesFromDb(query)
                return true
            }

            @SuppressLint("CheckResult")
            private fun getCitiesFromDb(queryText: String) {
                val searchText = "%$queryText%"

                Observable
                    .just(searchText)
                    .map { searchQueryText: String ->
                        repositoryImpl.getCitiesCursor(searchQueryText)
                    }
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { cursor: Any ->
                            handleResults(cursor as Cursor)
                        },
                        { t: Throwable ->
                            handleError(t)
                        })
            }

            private fun handleResults(cursor: Cursor) {
                mSearchView.suggestionsAdapter =
                    WeatherSearchViewAdapter(
                        context,
                        cursor,
                        mSearchView,
                        listener
                    )
            }

            private fun handleError(t: Throwable) {
                Timber.e(t)
                Toast.makeText(
                    context, "Problem in Fetching Deals",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_position -> {
                if (mWeatherViewModel.canGetLocation(this, context))
                    mWeatherViewModel.getCurrentWeather()
                else
                    UIManager.showActionInSnackBar(
                        this,
                        findViewById(android.R.id.content),
                        "Cannot get location please enable device's position setting.",
                        SnackBarType.ALERT,
                        getString(R.string.action_ok)
                    ) { v -> }
            }
            R.id.action_search -> Timber.d("noinspection SimplifiableIfStatement")

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        Timber.e("onDestroy()")
        mWeatherViewModel.clearDisposables()
        super.onDestroy()
    }

    /////////////////////////////////////
    //
    // BUS
    //
    /////////////////////////////////////
    @SuppressLint("BinaryOperationInTimber")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationFetchedEventResult(event: LocationFetchedEvent) {
        Timber.e(
            "onLocationFetchedEvent() - " +
                    "${event.getLocation().latitude}," +
                    " ${event.getLocation().longitude}"
        )

        mWeatherViewModel.fetchWeather(
            LabLocationUtils.buildTargetLocationObject(
                event.getLocation().latitude,
                event.getLocation().longitude
            )
        )
    }


    fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")

        mWeatherViewModel.getProgressBarVisibility().observe(
            this,
            {
                if (!it) UIManager.hideView(viewBinding.progressBar)
                else UIManager.showView(viewBinding.progressBar)
            })

        mWeatherViewModel.getConnectionStatus().observe(
            this,
            {
                if (!it) layoutInflater.inflate(
                    R.layout.no_internet_connection,
                    viewBinding.root,
                    true
                )
            })

        mWeatherViewModel.getDownloadStatus().observe(
            this,
            { statusMessage ->
                viewBinding.tvDownloadStatus.text = statusMessage
            })

        mWeatherViewModel.getDownloadDone().observe(this, {
            viewBinding.tvDownloadStatus.visibility = View.GONE
        })
        mWeatherViewModel.getIsWeatherData().observe(this, {
            if (!it) {
                mWeatherViewModel.startWork(this)
            } else {
                viewBinding.tvDownloadStatus.visibility = View.GONE
                viewBinding.weatherDataContainer.visibility = View.VISIBLE
            }
        })
        mWeatherViewModel.getWorkerStatus().observe(this, {

        })
        mWeatherViewModel.getOneCalWeather().observe(this, {
            updateOneCallUI(it)
        })
    }


    fun updateOneCallUI(oneCallWeatherResponse: OneCallWeatherResponse) {
        Timber.d("updateOneCallUI()")

        // Load weather icon
        Glide.with(context)
            .load(
                WeatherUtils.getWeatherIconFromApi(
                    oneCallWeatherResponse
                        .currentWeather
                        .weather[0]
                        .icon
                )
            )
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("error %s", e!!.message)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("Image weather bien chargée")
                    return false
                }
            })
            .into(viewBinding.ivWeatherIcon)

        val address: Address =
            mWeatherViewModel.getCityNameWithCoordinates(
                this,
                oneCallWeatherResponse.latitude,
                oneCallWeatherResponse.longitude
            )!!

        // Load city name
        val cityName =
            address.locality + context.resources.getString(R.string.separator_placeholder)
        viewBinding.tvWeatherCityName.text = cityName
        val country = address.countryName
        viewBinding.tvWeatherCityCountry.text = country
        viewBinding.tvWeatherMainDescription.text =
            oneCallWeatherResponse
                .currentWeather
                .weather[0]
                .description

        supportActionBar?.title = "$cityName $country"

        // Temperatures
        val temperature =
            "${oneCallWeatherResponse.currentWeather.temperature.roundToInt()} ${getString(R.string.degree_placeholder)}"
        viewBinding.tvWeatherCityTemperature.text = temperature

        val realFeels =
            "${oneCallWeatherResponse.currentWeather.feelsLike.roundToInt()} ${
                resources.getString(
                    R.string.degree_placeholder
                )
            }"
        viewBinding.tvWeatherCityRealFeels.text = realFeels

        viewBinding.tvSunrise.text =
            DateTimeUtils.formatMillisToTimeHoursMinutes(
                oneCallWeatherResponse.currentWeather.sunrise
            )
        viewBinding.tvSunset.text = DateTimeUtils.formatMillisToTimeHoursMinutes(
            oneCallWeatherResponse.currentWeather.sunset
        )

        val hourlyWeather: List<CurrentWeather> = oneCallWeatherResponse.hourlyWeather

        // Build chart with hourly weather data
        buildChart(hourlyWeather)
        val cloudiness: String = oneCallWeatherResponse.currentWeather.clouds
            .toString() + " " + resources.getString(R.string.percent_placeholder)
        viewBinding.tvWeatherExtraCloudiness.text = cloudiness

        val humidity: String = oneCallWeatherResponse.currentWeather.humidity
            .toString() + " " + resources.getString(R.string.percent_placeholder)
        viewBinding.tvWeatherExtraHumidity.text = humidity

        val pressure: String = oneCallWeatherResponse.currentWeather.pressure
            .toString() + " " + resources.getString(R.string.pressure_unit_placeholder)
        viewBinding.tvWeatherExtraPressure.text = pressure

        // Wind
        val wind: String =
            (oneCallWeatherResponse.currentWeather.windSpeed.toString() + " "
                    + resources.getString(R.string.meter_unit_placeholder))
        viewBinding.tvWeatherExtraWindSpeed.text = wind
        val windDirection: WindDirection = WindDirection.getWindDirectionToTextualDescription(
            oneCallWeatherResponse.currentWeather.windDegree
        )

        Glide.with(this)
            .load(ContextCompat.getDrawable(this, windDirection.icon))
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("error %s", e!!.message)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("Image wind bien chargée")
                    return false
                }
            })
            .into(viewBinding.ivWeatherExtraWindDirectionIcon)

        val windDirectionShortName: String = windDirection.shortName + " "
        viewBinding.tvWeatherExtraWindDirectionShortName.text = windDirectionShortName

        val mAdapter = WeatherForecastAdapter(
            this,
            oneCallWeatherResponse
                .dailyWeather
                .subList(1, oneCallWeatherResponse.dailyWeather.size - 2)
        )

        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.rvForecastFiveDays.layoutManager = linearLayoutManager
        viewBinding.rvForecastFiveDays.adapter = mAdapter
    }

    fun buildChart(hourlyWeather: List<CurrentWeather>) {
        Timber.d("buildChart()")

        // in this example, a LineChart is initialized from xml
        val chart = context.findViewById<LineChart>(R.id.weather_hourly_chart)
        val whiteColor = ContextCompat.getColor(context, R.color.white)

        // Styling
        WeatherUtils.stylingChartGrid(chart, whiteColor)

        val temperatureForNextHours =
            WeatherUtils.getWeatherTemperaturesForNextHours(hourlyWeather)
        val integers = HashMap<Float, Float>()

        for (i in temperatureForNextHours.indices) {
            integers[i.toFloat()] = temperatureForNextHours[i]
        }

        val entries: MutableList<Entry> = ArrayList()
        for ((key, value) in integers) {
            // turn your data into Entry objects
            entries.add(Entry(key, value))
        }

        val dataSet = LineDataSet(entries, "Label") // add entries to dataset
        dataSet.color = whiteColor
        dataSet.valueTextSize = 12f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getPointLabel(entry: Entry): String {
                return "${entry.y.toInt()} + ${getString(R.string.degree_placeholder)}"
            }
        }
        dataSet.valueTextColor = whiteColor
        dataSet.getValueTextColor(whiteColor)

        // the labels that should be drawn on the XAxis
        val quarters =
            WeatherUtils.getWeatherTemperaturesQuarters(hourlyWeather as List<CurrentWeather>)
        Timber.d("quarters value : %d", quarters.size)
        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return quarters[value.toInt()]
            }
        }
        val xAxis = chart.xAxis
        xAxis.granularity = 1f // minimum axis-step (interval) is 1
        xAxis.valueFormatter = if (quarters.size > 1) formatter else DefaultAxisValueFormatter(3)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate() // refresh
    }

    override fun onWeatherItemClicked(cityModel: CityModel) {
        Timber.d("onWeatherItemClicked()")
        UIManager.hideKeyboard(this, findViewById(android.R.id.content))

        mWeatherViewModel.fetchWeather(
            LabLocationUtils.buildTargetLocationObject(
                cityModel.latitude,
                cityModel.longitude
            )
        )
    }
}