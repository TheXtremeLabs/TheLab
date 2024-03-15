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
import com.riders.thelab.core.data.local.model.weather.CityAppSearch
import com.riders.thelab.core.data.local.model.weather.WeatherData
import com.riders.thelab.core.data.remote.dto.weather.City
import com.riders.thelab.core.data.utils.WeatherSearchManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.ResponseBody
import okio.gzip
import okio.source
import timber.log.Timber
import java.io.InputStream
import java.util.UUID


@SuppressLint("RestrictedApi")
@HiltWorker
class WeatherDownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val mRepository: IRepository
) : CoroutineWorker(context, workerParams) {

    private var taskDataString: String? = null
    private var outputData: Data? = null

    private var isSavingInAppSearchDatabase: Boolean = false

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

                mRepository.getBulkWeatherCitiesFile().run {

                    var responseBody: ResponseBody? = null
                    var stream: InputStream? = null

                    /*try {
                        responseBody = this.execute().body()
                        stream = responseBody?.byteStream()
                        Timber.d(
                            "doWork() | getBulkWeatherCitiesFile onSuccess(responseFile size: ${
                                responseBody?.bytes()?.size
                            })"
                        )
                        if (stream != null) {
                            LabFileManager.unzipGzip(stream)
                        }
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                    } finally {
                        responseBody?.close()
                        stream?.close()
                    }*/


                    // Step 1 : Unzip
                    val unzipResult: String? = LabFileManager.unzipGzip(
                        mRepository.getBulkWeatherCitiesFile().execute().body()?.byteStream()
                            ?.source()?.gzip()!!
                    )
                    /*try {
                    responseBody = this.execute().body()
                    stream = responseBody?.byteStream()
                    Timber.d(
                        "doWork() | getBulkWeatherCitiesFile onSuccess(responseFile size: ${
                            responseBody?.bytes()?.size
                        })"
                    )
                    if (null == stream) {
                        null
                    } else {
                        LabFileManager.unzipGzip(stream)
                    }
                } catch (exception: IOException) {
                    exception.printStackTrace()
                    null
                } finally {
                    responseBody?.close()
                    stream?.close()
                }*/

                    if (unzipResult.isNullOrEmpty()) {
                        Timber.e("doWork() | String unzippedGZipResult is empty")
                        Result.failure()
                    } else {
                        Timber.d("doWork() | Unzipped downloaded file length: ${unzipResult.length}")

                        // Step 2 : Parse JSON File
                        val dtoCities: List<City>? = unzipResult.let {
                            LabParser.parseJsonFile<List<City>>(it)
                        }

                        if (dtoCities.isNullOrEmpty()) {
                            Timber.e("doWork() | List<City> dtoCities is empty")
                            return Result.failure()
                        }

                        Timber.d("doWork() | Save in database...")

                        // Step 3: Save in databases, app Search and Room
                        // Step 3 save in database
                        saveCities(dtoCities)

                        outputData = createOutputData(WORK_SUCCESS)
                        Result.success(outputData!!)
                    }
                }
            } else {
                // In this case data already exists in database
                // Load data then let the the user perform his request
                Timber.d("Record found in database. Continue...")
                outputData = createOutputData(WORK_SUCCESS)
                Result.success(outputData!!)
            }
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("doWork() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.simpleName})")
            }
            .onSuccess { }
            .getOrElse {
                Timber.e("getOrElse | error caught with message: $WORK_DOWNLOAD_FAILED | ${it.message} (class: ${it.javaClass.simpleName})")
                outputData = createOutputData(WORK_DOWNLOAD_FAILED)
                Result.failure(outputData!!)
            }
    }

    /**
     * Creates output data to send back to the activity / presenter which is listening to it
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
    suspend fun saveCities(dtoCities: List<City>) = runCatching {
        Timber.d("saveCities()")
        mRepository.saveCities(dtoCities)
        mRepository.insertWeatherData(WeatherData(0, true))
    }
        .onFailure {
            it.printStackTrace()
            Timber.e("saveCities() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.simpleName})")
        }
        .onSuccess {
            Timber.d("saveCities() | onSuccess | is success: $it")
        }


    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED = "Error while downloading zip file"
        const val WORK_RESULT = "work_result"
    }
}