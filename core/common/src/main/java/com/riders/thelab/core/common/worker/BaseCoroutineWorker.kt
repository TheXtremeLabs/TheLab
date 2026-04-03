package com.riders.thelab.core.common.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import timber.log.Timber

abstract class BaseCoroutineWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    fun fetchInputData(): Data = inputData.also { data ->
        Timber.d("fetchInputData() | inout data: ${data.toString()}")
    }

    /**
     * Creates output data to send back to the activity / presenter which is listening to it
     *
     * @return
     */
    @SuppressLint("RestrictedApi")
    fun createOutputData(vararg extras: Pair<String, Any?>): Data = Data.Builder()
        .apply {
            extras.forEach {
                this.put(key = it.first, value = it.second)
            }
        }
        .build()
}