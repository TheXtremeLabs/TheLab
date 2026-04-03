package com.riders.thelab.core.location

import android.content.Context
import androidx.work.WorkerParameters
import com.riders.thelab.core.common.worker.BaseCoroutineWorker
import kotlinx.coroutines.flow.first
import timber.log.Timber

class LabLocationWorker(
    val context: Context,
    workerParams: WorkerParameters
) : BaseCoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Timber.d("dWork()")

        fetchInputData().also { data ->
            Timber.d("doWork() | inout data: ${data.toString()}")
        }

        val location = LabLocationManager.getInstance(context).lastKnownLocationFlow.first()
        if (null == location) {
            Timber.e("doWork() | Location object is null")
            return Result.failure(
                createOutputData(
                    "KEY_1" to 1,
                    "KEY_2" to 2,
                    EXTRA_LOCATION_RESULT to null
                )
            )
        } else {
            Timber.d("dWork() | location : ${location.toString()}")
            return Result.success(
                createOutputData(
                    "KEY_1" to 1,
                    "KEY_2" to 2,
                    EXTRA_LOCATION_LATITUDE to location.latitude,
                    EXTRA_LOCATION_LONGITUDE to location.longitude
                )
            )
        }
    }

    companion object {
        val TAG: String = LabLocationWorker::class.java.simpleName
        const val EXTRA_LOCATION_RESULT: String = "EXTRA_LOCATION_RESULT"
        const val EXTRA_LOCATION_LATITUDE: String = "EXTRA_LOCATION_LATITUDE"
        const val EXTRA_LOCATION_LONGITUDE: String = "EXTRA_LOCATION_LONGITUDE"
    }
}