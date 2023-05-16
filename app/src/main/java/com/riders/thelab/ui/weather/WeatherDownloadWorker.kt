package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.riders.thelab.core.parser.LabParser
import com.riders.thelab.core.storage.LabFileManager
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.City
import com.riders.thelab.utils.Validator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("RestrictedApi")
@HiltWorker
class WeatherDownloadWorker @AssistedInject constructor(
    @Assisted @NonNull val context: Context,
    @Assisted @NonNull val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED = "Error while downloading zip file"
        const val WORK_RESULT = "work_result"
    }

    private var taskData: Data? = null
    var taskDataString: String? = null
    var outputData: Data? = null

    @Inject
    lateinit var mRepository: IRepository

    override suspend fun doWork(): Result {
        Timber.d("startWork()")

        taskData = inputData
        if (null == taskData) {
            Timber.e("Input Data is null")
        }
        taskDataString = taskData!!.getString(MESSAGE_STATUS)

        try {

            val responseFile = mRepository.getBulkWeatherCitiesFile().execute().body()
            Timber.d("observer.onSuccess(responseFile)")

            if (null == responseFile) {
                return Result.failure()
            }

            try {
                Timber.d("Unzipped downloaded file...")

                // Step 1 : Unzip
                val unzippedGZipResult = LabFileManager.unzipGzip(responseFile)
                if (Validator.isEmpty(unzippedGZipResult)) {
                    Timber.e("String unzippedGZipResult is empty")
                    return Result.failure()
                }

                // Step 2 : Parse JSON File
                val dtoCities: List<City>? = unzippedGZipResult?.let {
                    LabParser.parseJsonFileListWithMoshi(it)
                }

                if (dtoCities.isNullOrEmpty()) {
                    Timber.e("List<City> dtoCities is empty")
                    return Result.failure()
                }

                Timber.d("Save in database...")

                // Step 3 save in database
                saveCities(dtoCities)

                outputData = createOutputData(WORK_RESULT, WORK_SUCCESS)
                return Result.success(outputData!!)

            } catch (e: Exception) {
                Timber.e(e)
                return Result.failure()
            }
        } catch (throwable: Exception) {
            Timber.e(WORK_DOWNLOAD_FAILED)
            Timber.e(throwable)
            outputData = createOutputData(
                WORK_RESULT,
                WORK_DOWNLOAD_FAILED
            )
            return Result.failure(outputData!!)
        }
    }

    /**
     * Creates ouput data to send back to the activity / presenter which is listening to it
     *
     * @param outputDataKey
     * @param message
     * @return
     */
    private fun createOutputData(outputDataKey: String, message: String): Data {
        Timber.d("createOutputData()")
        return Data.Builder()
            .put(outputDataKey, message)
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
}