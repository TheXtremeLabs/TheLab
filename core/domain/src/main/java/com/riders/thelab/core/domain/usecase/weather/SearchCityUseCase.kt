package com.riders.thelab.core.domain.usecase.weather

import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.domain.utils.UseCase
import timber.log.Timber
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: IWeatherRepository
) : UseCase<String, List<City>> {

    override suspend fun invoke(params: String): Resource<List<City>> = runCatching {
        val result = repository.searchCity(params)
        Resource.Success(result)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("SearchCityUseCase.invoke() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("SearchCityUseCase.invoke() | onSuccess | $it")
        }
        .getOrElse { exception -> Resource.Error(exception.message ?: "Unknown error", exception) }
}