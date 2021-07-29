package com.riders.thelab.ui.mainactivity.fragment.weather

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.riders.thelab.core.bus.LocationFetchedEvent
import com.riders.thelab.core.utils.LabLocationManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.weather.CityWeather
import com.riders.thelab.databinding.FragmentWeatherBinding
import com.riders.thelab.ui.weather.WeatherUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment(), LocationListener {

    companion object {
        fun newInstance(): WeatherFragment {
            val args = Bundle()

            val fragment = WeatherFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var viewBinding: FragmentWeatherBinding

    private val mWeatherViewModel: WeatherViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWeatherViewModel
            .getProgressBarVisibility()
            .observe(viewLifecycleOwner, {
                if (!it) UIManager.hideView(viewBinding.progressBar)
                else UIManager.showView(viewBinding.progressBar)
            })

        mWeatherViewModel
            .getWeatherFailed()
            .observe(viewLifecycleOwner, {
                Timber.e("getWeatherFailed")
            })

        mWeatherViewModel
            .getWeather()
            .observe(viewLifecycleOwner, {
                updateUI(it)
            })
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

        val labLocationManager = LabLocationManager(requireActivity(), requireContext(), this)

        if (!labLocationManager.canGetLocation()) {
            Timber.e("Cannot get location please enable position")
            labLocationManager.showSettingsAlert()
        } else {
            labLocationManager.setLocationListener()
            labLocationManager.getLocation()
        }
    }

    override fun onDestroyView() {
        mWeatherViewModel.clearDisposable()
        super.onDestroyView()
    }


    /////////////////////////////////////
    //
    // BUS
    //
    /////////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationFetchedEventResult(event: LocationFetchedEvent) {
        Timber.e("onLocationFetchedEvent()")
        val location: Location = event.location
        val latitude = location.latitude
        val longitude = location.longitude
        Timber.e("$latitude, $longitude")

        mWeatherViewModel.getCityWeather(requireActivity(), location)
    }

    private fun updateUI(cityWeather: CityWeather) {
        Timber.d("updateUI()")
        UIManager.showView(viewBinding.weatherDataContainer)

        // Load weather icon
        Glide.with(requireActivity())
            .load(WeatherUtils.getWeatherIconFromApi(cityWeather.weatherIconURL))
            .into(viewBinding.ivWeatherIcon)

        viewBinding.tvWeatherCityTemperature.text =
            String.format(Locale.getDefault(), "%d", cityWeather.cityTemperature.roundToInt())
        viewBinding.tvDegreePlaceholder.visibility = View.VISIBLE

        // Load city name
        viewBinding.tvWeatherCityName.text = cityWeather.cityName
        viewBinding.tvWeatherCityCountry.text = cityWeather.cityCountry
        viewBinding.tvWeatherMainDescription.text = cityWeather.cityWeatherDescription
    }

    override fun onLocationChanged(location: Location) {
        Timber.e("onLocationChanged() $location")
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}