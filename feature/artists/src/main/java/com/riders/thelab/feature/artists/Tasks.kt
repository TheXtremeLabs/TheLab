package com.riders.thelab.feature.artists

import com.google.android.gms.tasks.Task
import com.riders.thelab.core.data.utils.Resource
import com.riders.thelab.core.data.utils.getClassDeclaredConstructorInstance
import com.riders.thelab.core.data.utils.toErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi
import timber.log.Timber
import kotlin.coroutines.resumeWithException

// TODO : Refactor to an extensions/utils file / class

inline fun <reified T : Task<T>> getTaskResult(
    crossinline onTaskFailure: (Exception) -> Unit,
    crossinline onTaskSuccess: (T) -> Unit
) {
    val task = T::class.java.newInstance()
    task
        .addOnFailureListener { throwable ->
            Timber.e("task | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
            onTaskFailure(throwable)
        }
        .addOnSuccessListener {
            Timber.d("task | addOnSuccessListener | value: $it")
        }
        .addOnCompleteListener {
            if (!task.isSuccessful) {
                Timber.e("task | addOnCompleteListener | Google Sign In Failed")
            } else {
                Timber.i("task | addOnCompleteListener | Sign in successful")
                val result = task.result

                if (null != result) {
                    onTaskSuccess(result)
                }
            }
        }
}

suspend inline fun <reified T> Task<T?>.getTaskResult(
    crossinline onTaskFailure: (Exception) -> Unit,
    crossinline onTaskSuccess: (T) -> Unit
) {
    val task: Task<T?> = this.getClassDeclaredConstructorInstance()

    suspendCancellableCoroutine<T?> { cancellableContinuation ->
        task
            .addOnFailureListener { throwable ->
                Timber.e("task | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                cancellableContinuation.resumeWithException(exception = throwable)
                // onTaskFailure(throwable)
            }
            .addOnSuccessListener {
                Timber.d("task | addOnSuccessListener | value: $it")
            }
            .addOnCompleteListener {
                if (!this.isSuccessful) {
                    Timber.e("task | addOnCompleteListener | Google Sign In Failed")
                } else {
                    Timber.i("task | addOnCompleteListener | Sign in successful")
                    val result = this.result

                    if (null != result) {
                        cancellableContinuation.resume(value = result) { cause, value, context ->
                        }
                        // onTaskSuccess(result)
                    }
                }
            }
    }
}


@OptIn(ExperimentalKotoolsTypesApi::class)
inline fun <reified T : Any> Task<T>.getTaskFlow(): Flow<Resource<T>> = callbackFlow<Resource<T>> {
    this@getTaskFlow
        .addOnCanceledListener {
            Timber.d("task | addOnCanceledListener")
            trySend(Resource.Error(message = NotBlankString.create("Task canceled")))
        }
        .addOnFailureListener { throwable ->
            Timber.e("task | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")

            throwable.message?.let {
                trySend(
                    Resource.Error(
                        message = it.toNotBlankString().getOrThrow(),
                        throwable = throwable
                    )
                )
            } ?: trySend(Resource.ErrorWithType(throwable.toErrorType()))
        }
        .addOnSuccessListener {
            Timber.d("task | addOnSuccessListener | value: $it")
        }
        .addOnCompleteListener {
            if (!this@getTaskFlow.isSuccessful) {
                Timber.e("task | addOnCompleteListener | Task execution failed")
            } else {
                Timber.i("task | addOnCompleteListener | Task execution successful")
                val result = this@getTaskFlow.result

                if (null != result) {
                    trySend(Resource.Success(result))
                }
            }
        }

    awaitClose {
        Timber.d("task | awaitClose")
    }
}
    .catch {
        Timber.e("task | catch | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
    }
    .flowOn(Dispatchers.Main)