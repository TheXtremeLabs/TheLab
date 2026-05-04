package com.riders.thelab.core.domain.usecase.weather

import android.content.Context
import android.os.Environment
import com.riders.thelab.core.common.network.DownloadState
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.repository.IWeatherRepository
import com.riders.thelab.core.domain.utils.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.time.Clock

class DownloadWeatherDataUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: IWeatherRepository
) : UseCase<Unit?, Flow<DownloadState>> {

    override suspend fun invoke(params: Unit?): Resource<Flow<DownloadState>> = runCatching {
        Timber.d("invoke(params = $params)")
        val timestamp: Long = Clock.System.now().toEpochMilliseconds()
        val filePath =
            "${context.filesDir.absolutePath + File.separator + Environment.DIRECTORY_DOWNLOADS}/TMP/AndroidXWorker/tmp_$timestamp.json"

        val result = repository.getBulkDownloadAsFlow(context)
        Resource.Success(result)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("DownloadWeatherDataUseCase.invoke() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("DownloadWeatherDataUseCase.invoke() | onSuccess | $it")
        }
        .getOrElse { exception -> Resource.Error(exception.message ?: "Unknown error", exception) }
}