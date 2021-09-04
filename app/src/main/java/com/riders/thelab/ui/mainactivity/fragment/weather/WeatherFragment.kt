package com.riders.thelab.ui.mainactivity.fragment.weather

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private var  _viewBinding: FragmentWeatherBinding? =  null

    private val binding get() = _viewBinding!!


    private val mWeatherViewModel: WeatherViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWeatherViewModel
            .getProgressBarVisibility()
            .observe(viewLifecycleOwner, {
                if (!it) UIManager.hideView(binding.progressBar)
                else UIManager.showView(binding.progressBar)
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
        super.onDestroyView()
        _viewBinding = null
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

        binding.cityWeather = cityWeather

        UIManager.showView(binding.weatherDataContainer)

        // Load weather icon
        UIManager.loadImage(
            requireActivity(),
            WeatherUtils.getWeatherIconFromApi(cityWeather.weatherIconURL),
            binding.ivWeatherIcon
        )

        binding.tvWeatherCityTemperature.text =
            String.format(Locale.getDefault(), "%d", cityWeather.cityTemperature.roundToInt())
        binding.tvDegreePlaceholder.visibility = View.VISIBLE
    }

    override fun onLocationChanged(location: Location) {
        Timber.e("onLocationChanged() $location")
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}