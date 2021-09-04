package com.riders.thelab.ui.mainactivity.fragment.weather

import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabAddressesUtils
import com.riders.thelab.core.utils.LabLocationUtils
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.weather.CityWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    var progressVisibility: MutableLiveData<Boolean> = MutableLiveData()

    var weather: MutableLiveData<CityWeather> = MutableLiveData()
    var weatherFailed: MutableLiveData<Boolean> = MutableLiveData()

    fun getProgressBarVisibility(): LiveData<Boolean> {
        return progressVisibility
    }

    fun getWeather(): LiveData<CityWeather> {
        return weather
    }

    fun getWeatherFailed(): LiveData<Boolean> {
        return weatherFailed
    }

    fun getCityWeather(context: FragmentActivity, location: Location) {
        Timber.i("getCityWeather()")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getWeatherOneCallAPI(location)

                response?.let {

                    val address: Address? =
                        LabAddressesUtils.getDeviceAddress(
                            Geocoder(context, Locale.getDefault()),
                            LabLocationUtils.buildTargetLocationObject(
                                it.latitude,
                                it.longitude
                            )
                        )

                    val cityName: String =
                        address?.locality.orEmpty() +
                                context.resources.getString(R.string.separator_placeholder)
                    val cityCountry: String = address?.countryName.orEmpty()
                    val cityTemperature: Double = it.currentWeather.temperature
                    val weatherIconURL: String = it.currentWeather.weather[0].icon
                    val cityWeatherDescription: String =
                        it.currentWeather.weather[0].description

                    val cityWeather = CityWeather(
                        cityName,
                        cityCountry,
                        cityTemperature,
                        weatherIconURL,
                        cityWeatherDescription
                    )

                    progressVisibility.value = false
                    weather.value = cityWeather
                }

            } catch (throwable: Exception) {
                Timber.e(throwable)
                progressVisibility.value = false
                weatherFailed.value = true
            }
        }
    }
}