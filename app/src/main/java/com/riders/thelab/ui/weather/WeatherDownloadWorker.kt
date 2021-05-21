package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import androidx.concurrent.futures.ResolvableFuture
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.riders.thelab.core.parser.LabParser
import com.riders.thelab.core.utils.LabFileManager
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.City
import com.riders.thelab.utils.Validator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("RestrictedApi")
@HiltWorker
class WeatherDownloadWorker @AssistedInject constructor(
    @Assisted context: Context?,
    @Assisted workerParams: WorkerParameters?
) : ListenableWorker(context!!, workerParams!!) {

    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val URL_REQUEST = "url_request"

        const val WORK_SUCCESS = "Loading finished"
        const val WORK_DOWNLOAD_FAILED = "Error while downloading zip file"
        const val WORK_RESULT = "work_result"
    }

    var future: ResolvableFuture<Result>? = null
    var taskData: Data? = null
    var taskDataString: String? = null
    var outputData: Data? = null

    @Inject
    lateinit var mRepository: RepositoryImpl

    init {
        future = ResolvableFuture.create()
    }

    override fun startWork(): ListenableFuture<Result> {
        Timber.d("startWork()")

        taskData = inputData
        if (null == taskData) {
            Timber.e("Input Data is null")
        }
        taskDataString = taskData!!.getString(MESSAGE_STATUS)

        val urlRequest = taskData!!.getString(URL_REQUEST)
        if (urlRequest == null) {
            future!!.set(Result.failure())
        }


        mRepository.getBulkWeatherCitiesFile()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { responseFile ->
                    Timber.d("observer.onSuccess(responseFile)")
                    try {
                        Timber.d("Unzipped downloaded file...")
                        // Step 1 : Unzip
                        val unzippedGZipResult = LabFileManager.unzipGzip(responseFile)
                        if (Validator.isEmpty(unzippedGZipResult)) {
                            Timber.e("String unzippedGZipResult is empty")
                            return@subscribe
                        }
                        val dtoCities: List<City>? = unzippedGZipResult?.let {
                            LabParser
                                .getInstance()
                                .parseJsonFileListWithMoshi(it)
                        }
                        if (Validator.isNullOrEmpty(dtoCities)) {
                            Timber.e("List<City> dtoCities is empty")
                            return@subscribe
                        }
                        Timber.d("Save in database...")
                        // Step 3 save in database
                        if (dtoCities != null) {
                            saveCities(dtoCities)
                        }
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                },
                { throwable ->
                    Timber.e(WORK_DOWNLOAD_FAILED)
                    Timber.e(throwable)
                    outputData = createOutputData(
                        WORK_RESULT,
                        WORK_DOWNLOAD_FAILED
                    )
                    future!!.set(Result.failure(outputData!!))
                })

        return future!!
    }

    /**
     * Creates ouput data to send back to the activity / presenter which's listening to it
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
        mRepository
            .saveCities(dtoCities)
            .subscribe(
                { longs ->

                    mRepository.insertWeatherData(WeatherData(0,true))
                    outputData = createOutputData(WORK_RESULT, WORK_SUCCESS)
                    future!!.set(Result.success(outputData!!))
                },
                Timber::e
            )
    }
}