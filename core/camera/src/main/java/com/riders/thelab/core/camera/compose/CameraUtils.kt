package com.riders.thelab.core.camera.compose

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import com.riders.thelab.core.ui.utils.executor
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this)
        .also { future ->
            future.addListener(
                { continuation.resume(future.get()) },
                executor
            )
        }
}

suspend fun Context.getCameraProvider(listenerBlock: Runnable): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this)
            .also { future ->
                future.addListener(
                    {
                        continuation.resume(future.get())
                        listenerBlock.run()
                    },
                    executor
                )
            }
    }

suspend fun Context.getCameraProvider(executor: Executor): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this)
            .also { future ->
                future.addListener(
                    { continuation.resume(future.get()) },
                    executor
                )
            }
    }

suspend fun Context.getCameraProvider(
    executor: Executor,
    listenerBlock: Runnable
): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this)
            .also { future ->
                future.addListener(
                    {
                        continuation.resume(future.get())
                        listenerBlock.run()
                    },
                    executor
                )
            }
    }