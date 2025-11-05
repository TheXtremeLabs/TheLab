package com.riders.thelab.core.ui.utils

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.Serializable

class LabNavigator private constructor(private val activity: Activity) {

    fun callIntentActivity(
        targetClass: Class<out Activity?>?,
        activityResultLauncher: ActivityResultLauncher<Intent>? = null,
        vararg extras: Pair<String, Any?>,
        flags: Int? = null
    ) {
        if (null == targetClass) {
            Timber.e("target class is null")
            return
        }

        Intent(activity, targetClass::class.java)
            .runCatching {
                Timber.d("callIntentActivity()")
                if (extras.isNotEmpty()) {
                    buildIntentExtras(this, extras = extras)
                }
                flags?.let { this.flags = it }
                this
            }
            .onFailure { exception ->
                exception.printStackTrace()
                Timber.e("callIntentActivity() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("callIntentActivity() | Attempt to start ${targetClass.javaClass.simpleName}")
                activityResultLauncher?.launch(it) ?: activity.startActivity(it)
            }
    }

    fun callIntentForPackageName(
        packageName: String,
        activityResultLauncher: ActivityResultLauncher<Intent>? = null,
        vararg extras: Pair<String, Any?>,
        flags: Int? = null
    ) = activity.packageManager.getLaunchIntentForPackage(packageName)
        ?.runCatching {
            Timber.d("callIntentFromPackageName() | package name : $packageName")
            if (extras.isNotEmpty()) {
                buildIntentExtras(this, extras = extras)
            }
            flags?.let { this.flags = it }
            this
        }
        ?.onFailure { exception ->
            exception.printStackTrace()
            Timber.e("callIntentFromPackageName() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        ?.onSuccess {
            Timber.i("callIntentFromPackageName() | Package $packageName successfully found. Attempt to start target package")
            activityResultLauncher?.launch(it) ?: activity.startActivity(it)
        } ?: run { UIManager.showToast(activity, "Package $packageName is not installed") }


    private fun buildIntentExtras(intent: Intent, vararg extras: Pair<String, Any?>) {
        Timber.d("buildIntentExtras() | extras count : ${extras.size}")

        extras.forEach { pair ->
            pair.second?.let { value ->
                Timber.d("buildIntentExtras() | add ${pair.first} in extras with value : $value")

                when (value) {
                    is String -> intent.putExtra(pair.first, value)
                    is Double -> intent.putExtra(pair.first, value)
                    is Int -> intent.putExtra(pair.first, value)
                    is Float -> intent.putExtra(pair.first, value)
                    is Serializable -> Json.encodeToString(value).apply {
                        intent.putExtra(pair.first, this)
                    }
                }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: LabNavigator? = null

        @JvmStatic
        @Synchronized
        fun getInstance(activity: Activity): LabNavigator = instance ?: synchronized(this) {
            instance ?: LabNavigator(activity = activity)
        }
    }

}