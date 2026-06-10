package com.riders.thelab.feature.weather.core.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.riders.thelab.core.common.storage.LabFileManager
import com.riders.thelab.core.common.utils.LabParser
import com.riders.thelab.core.common.worker.BaseCoroutineWorker
import com.riders.thelab.core.domain.model.weather.City
import com.riders.thelab.core.domain.repository.IWeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.ResponseBody
import okio.gzip
import okio.source
import retrofit2.Response
import timber.log.Timber
import java.io.InputStream


@SuppressLint("RestrictedApi")
@HiltWorker
class WeatherDownloadWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val repository: IWeatherRepository
) : BaseCoroutineWorker(context, workerParams) {

    private var taskDataString: String? = null
    private var outputData: Data? = null

    private var isSavingInAppSearchDatabase: Boolean = false

    override suspend fun doWork(): Result {
        Timber.d("doWork()")

        inputData.getString(MESSAGE_STATUS)?.let {
            taskDataString = it

            Timber.d("doWork() | taskDataString: $taskDataString")
        }

        // First step
        // Call repository to check if there is data in database
        val weatherData: Boolean = repository.hasWeatherData()

        if (!weatherData) {
            // In this case record's return is null
            // then we have to call our Worker to perform
            // the web service call to retrieve data from api
            Timber.e("List is empty. No Record found in database")

            return runCatching {
                val response: Response<ResponseBody> = repository.getBulkDownload().execute()

                if (!response.isSuccessful) {
                    return@runCatching Result.failure()
                }


                var responseBody: ResponseBody? = response.body()
                var stream: InputStream? = responseBody?.byteStream()

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
                val unzipResult: String? = LabFileManager.unzipGzip(stream?.source()?.gzip()!!)

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

                    Result.success(
                        createOutputData(
                            WORK_RESULT to WORK_SUCCESS
                        )
                    )
                }
            }
                .onFailure {
                    it.printStackTrace()
                    Timber.e("doWork() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
                }
                .onSuccess { }
                .getOrElse {
                    Timber.e("getOrElse | error caught with message: $WORK_DOWNLOAD_FAILED | ${it.message} (class: ${it.javaClass.canonicalName})")
                    Result.failure(createOutputData(WORK_RESULT to WORK_DOWNLOAD_FAILED))
                }
        } else {
            // In this case data already exists in database
            // Load data then let the the user perform his request
            Timber.v("Record found in database. Continue...")

            return Result.success(
                createOutputData(
                    WORK_RESULT to WORK_SUCCESS
                )
            )
        }
    }


    @SuppressLint("CheckResult")
    suspend fun saveCities(dtoCities: List<City>) {
        runCatching {
            repository.saveCities(dtoCities)
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("saveCities() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("saveCities() | onSuccess | IDs saved: ${it.take(4).joinToString()}")
            }
    }


    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED = "Error while downloading zip file"
        const val WORK_RESULT = "work_result"
        const val EXTRA_CITIES = "EXTRA_CITIES"
    }
}