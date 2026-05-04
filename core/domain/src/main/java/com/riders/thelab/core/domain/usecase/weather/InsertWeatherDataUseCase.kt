package com.riders.thelab.core.domain.usecase.weather

import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.domain.utils.UseCase
import timber.log.Timber
import javax.inject.Inject

class InsertWeatherDataUseCase @Inject constructor(
    private val repository: IWeatherRepository
) : UseCase<List<City>, List<Long>> {

    override suspend fun invoke(params: List<City>): Resource<List<Long>> = runCatching {
        val result = repository.saveCities(params)
        Resource.Success(result)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("InsertWeatherDataUseCase.invoke() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("InsertWeatherDataUseCase.invoke() | onSuccess | $it")
        }
        .getOrElse { exception -> Resource.Error(exception.message ?: "Unknown error", exception) }
}