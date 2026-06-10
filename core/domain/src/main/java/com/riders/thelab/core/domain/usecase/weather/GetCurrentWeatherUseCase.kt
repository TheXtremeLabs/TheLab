package com.riders.thelab.core.domain.usecase.weather

import android.location.Location
import com.riders.thelab.core.common.utils.DateTimeUtils
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.model.weather.Weather
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.domain.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: IWeatherRepository
) : UseCase<Location, Weather> {

    override suspend fun invoke(params: Location): Resource<Weather> = runCatching {
        val result = repository.getCurrentWeather(params)
            ?: return@runCatching Resource.Error("No data found")
        Resource.Success(result)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("GetCurrentWeatherUseCase.invoke() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("GetCurrentWeatherUseCase.invoke() | onSuccess | $it")
        }
        .getOrElse { exception -> Resource.Error(exception.message ?: "Unknown error", exception) }

}