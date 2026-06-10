package com.riders.thelab.core.domain.usecase.weather

import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.domain.utils.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: IWeatherRepository
) : UseCase<Unit?, List<City>> {

    override suspend fun invoke(params: Unit?): Resource<List<City>> = runCatching {
        val result = repository.getCities()
        Resource.Success(result)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("GetCitiesUseCase.invoke() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("GetCitiesUseCase.invoke() | onSuccess | cities size : ${it.data.size}")
        }
        .getOrElse { exception -> Resource.Error(exception.message ?: "Unknown error", exception) }
}