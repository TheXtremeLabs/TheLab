package com.riders.thelab.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.WorkInfo
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.R
import com.riders.thelab.core.bus.KotlinBus
import com.riders.thelab.core.bus.Listen
import com.riders.thelab.core.bus.LocationFetchedEvent
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.*
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.local.model.compose.WeatherUIState
import com.riders.thelab.data.remote.dto.weather.CurrentWeather
import com.riders.thelab.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WeatherActivity : BaseActivity() {

    private val mWeatherViewModel: WeatherViewModel by viewModels()

    private var labLocationManager: LabLocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        checkLocationPermissions()

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            WeatherContent(mWeatherViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        Timber.d("onPause()")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
        mWeatherViewModel.fetchCities(this)
    }

    override fun onDestroy() {
        Timber.e("onDestroy()")
        super.onDestroy()
    }

    /////////////////////////////////////
    //
    // BUS
    //
    /////////////////////////////////////
    @Listen
    fun onLocationFetchedEventResult() {
        Timber.e("onLocationFetchedEventResult()")
        lifecycleScope.launch {
            KotlinBus.getInstance().subscribe<LocationFetchedEvent> {
                Timber.e(
                    "onLocationFetchedEvent() | ${it.location.latitude}, ${it.location.longitude}"
                )

                mWeatherViewModel.updateUIState(WeatherUIState.SuccessWeatherData(true))

                mWeatherViewModel.fetchWeather(
                    it.location.run { latitude to longitude }.toLocation()
                )
            }
        }
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun checkLocationPermissions() {
        Timber.d("checkLocationPermissions()")
        // run dexter permission
        Dexter
            .withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // do you work now
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permanently, navigate user to app settings
                    }

                    initViewModelObservers()

                    labLocationManager = LabLocationManager(this@WeatherActivity)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener { error: DexterError ->
                UIManager.showActionInToast(this, "Error occurred! $error")
            }
            .onSameThread()
            .check()
    }

    @SuppressLint("NewApi")
    fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")

        mWeatherViewModel.getIsWeatherData().observe(this) {
            if (!it) {
                mWeatherViewModel.startWork(this)
            } else {
//                binding.tvDownloadStatus.visibility = View.GONE
//                binding.weatherDataContainer.visibility = View.VISIBLE
            }
        }

        mWeatherViewModel.getWorkerStatus().observe(this) {

            when (it) {
                WorkInfo.State.SUCCEEDED -> {
//                    binding.progressBar.visibility = View.GONE
//                    binding.weatherDataContainer.visibility = View.VISIBLE
                }

                WorkInfo.State.FAILED -> {
//                    binding.progressBar.isIndeterminate = false
//                    binding.progressBar.setProgress(100, true)
//                    binding.progressBar.setIndicatorColor(
//                        ContextCompat.getColor(
//                            this@WeatherActivity,
//                            R.color.error
//                        )
//                    )
                }

                else -> {
                    Timber.e("else branch")
                }
            }
        }

    }

    fun fetchCurrentLocation() {
        Timber.d("fetchCurrentLocation()")

        if (labLocationManager?.canGetLocation() == true) {
            val location = labLocationManager?.getLocation() ?: return

            mWeatherViewModel.fetchWeather((location.latitude to location.longitude).toLocation())
        } else
            UIManager.showActionInSnackBar(
                this,
                findViewById(android.R.id.content),
                "Cannot get location please enable device's position setting.",
                SnackBarType.ALERT,
                getString(R.string.action_ok)
            ) { }
    }


    private fun buildChart(hourlyWeather: List<CurrentWeather>) {
        Timber.d("buildChart()")

        // in this example, a LineChart is initialized from xml
        val chart = this@WeatherActivity.findViewById<LineChart>(R.id.weather_hourly_chart)
        val whiteColor = ContextCompat.getColor(this@WeatherActivity, R.color.white)

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
                return "${entry.y.toInt()}" + getString(R.string.degree_placeholder)
            }
        }
        dataSet.valueTextColor = whiteColor
        dataSet.getValueTextColor(whiteColor)

        // the labels that should be drawn on the XAxis
        val quarters = WeatherUtils.getWeatherTemperaturesQuarters(hourlyWeather)
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
}