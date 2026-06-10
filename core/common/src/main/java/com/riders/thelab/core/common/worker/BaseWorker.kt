package com.riders.thelab.core.common.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import timber.log.Timber

/**
 * Base class for [CoroutineWorker] in the application.
 * Provides utility methods common to all workers.
 *
 * @param context The application context.
 * @param workerParameters Parameters to setup the worker.
 */
abstract class BaseCoroutineWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {


    /**
     * Creates output data to send back to the activity or component observing the worker's status.
     *
     * @param elements Vararg of [Pair] containing key-value pairs to be included in the [Data] output.
     * @return A [Data] object containing the provided elements.
     */
    @SuppressLint("RestrictedApi")
    fun createOutputData(vararg elements: Pair<String, Any?>): Data = Data.Builder().apply {
        Timber.d("createOutputData() | build output data for ${elements.size} element(s)")

        elements.forEach { (key, value) ->
            when (value) {
                is List<*> -> {
                    if (value.all { it is String }) {
                        this.putStringArray(key, value.filterIsInstance<String>().toTypedArray())
                    } else if (value.all { it is Int }) {
                        this.putIntArray(key, value.filterIsInstance<Int>().toIntArray())
                    } else {
                        Timber.e("createOutputData() | Unsupported list type for key: $key. Data only supports primitives and Strings.")
                    }
                }

                else -> this.put(key, value)
            }
        }
    }
        .build()
}