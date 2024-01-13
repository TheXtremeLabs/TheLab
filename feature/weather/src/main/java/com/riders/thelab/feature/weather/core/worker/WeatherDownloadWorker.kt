package com.riders.thelab.feature.weather.core.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.riders.thelab.core.common.storage.LabFileManager
import com.riders.thelab.core.common.utils.LabParser
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.weather.WeatherData
import com.riders.thelab.core.data.remote.dto.weather.City
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@SuppressLint("RestrictedApi")
@HiltWorker
class WeatherDownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val mRepository: IRepository
) : CoroutineWorker(context, workerParams) {

    private var taskDataString: String? = null
    private var outputData: Data? = null

    override suspend fun doWork(): Result {
        Timber.d("doWork()")

        inputData.getString(MESSAGE_STATUS)?.let {
            taskDataString = it

            Timber.d("doWork() | taskDataString: $taskDataString")
        }

        return runCatching {
            // First step
            // Call repository to check if there is data in database
            val weatherData = mRepository.getWeatherData()

            if (null == weatherData || !weatherData.isWeatherData) {
                // In this case record's return is null
                // then we have to call our Worker to perform
                // the web service call to retrieve data from api
                Timber.e("List is empty. No Record found in database")

                val responseFile = mRepository.getBulkWeatherCitiesFile().execute().body()
                Timber.d("observer.onSuccess(responseFile)")

                if (null == responseFile) {
                    Result.failure()
                } else {
                    Timber.d("Unzipped downloaded file...")

                    // Step 1 : Unzip
                    val unzippedGZipResult = LabFileManager.unzipGzip(responseFile)
                    if (unzippedGZipResult?.isEmpty() == true) {
                        Timber.e("String unzippedGZipResult is empty")
                        return Result.failure()
                    }

                    // Step 2 : Parse JSON File
                    val dtoCities: List<City>? = unzippedGZipResult?.let {
                        LabParser.parseJsonFile<List<City>>(it)
                    }

                    if (dtoCities.isNullOrEmpty()) {
                        Timber.e("List<City> dtoCities is empty")
                        return Result.failure()
                    }

                    Timber.d("Save in database...")

                    // Step 3 save in database
                    saveCities(dtoCities)

                    outputData = createOutputData(WORK_SUCCESS)
                    Result.success(outputData!!)
                }
            } else {
                // In this case data already exists in database
                // Load data then let the the user perform his request
                Timber.d("Record found in database. Continue...")
                outputData = createOutputData(WORK_SUCCESS)
                Result.success(outputData!!)
            }
        }
            .onFailure { Timber.e("onFailure | ${it.message}") }
            .onSuccess { }
            .getOrElse {
                Timber.e("getOrElse | $WORK_DOWNLOAD_FAILED | ${it.message}")
                outputData = createOutputData(WORK_DOWNLOAD_FAILED)
                Result.failure(outputData!!)
            }
    }

    /**
     * Creates ouput data to send back to the activity / presenter which is listening to it
     *
     * @param message
     * @return
     */
    private fun createOutputData(message: String): Data {
        Timber.d("createOutputData()")
        return Data.Builder()
            .put("work_result", message)
            .build()
    }


    @SuppressLint("CheckResult")
    fun saveCities(dtoCities: List<City>) {
        Timber.d("saveCities()")

        try {
            runBlocking {
                mRepository.saveCities(dtoCities)
                mRepository.insertWeatherData(WeatherData(0, true))
            }
        } catch (throwable: Exception) {
            Timber.e(throwable)
        }
    }

    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED = "Error while downloading zip file"
        const val WORK_RESULT = "work_result"
    }
}