package com.riders.thelab.core.common.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import kotlinx.serialization.json.Json
import timber.log.Timber

class LabPackageManager private constructor(private val applicationContext: Context) {

    fun getInstalledPackages(): List<ApplicationInfo>? = runCatching {
        applicationContext.packageManager.getInstalledApplications(0)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("getInstalledPackages() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .getOrNull()


    fun isInstalled(packageName: String): Boolean {
        val packageManager = applicationContext.packageManager
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (nameNotFoundException: PackageManager.NameNotFoundException) {
            Timber.e("isInstalled() | Package with name $packageName not found.")
            false
        }
    }

    fun getActivityPackageName(activityName: String): String? {
        Timber.d("getActivityPackageName()")

        val pManager: PackageManager = applicationContext.packageManager
        val packageName: String = applicationContext.applicationContext.packageName

        var returnedActivityPackageToString: String? = null

        return try {
            val list = pManager
                .getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                .activities

            list?.let {
                for (activityInfo in it.iterator()) {
                    val activityNameFound = activityInfo.name
                    Timber.d("ActivityInfo = " + activityInfo.name)
                    if (activityNameFound.lowercase().contains(activityName.lowercase())) {
                        returnedActivityPackageToString = activityInfo.name
                        break
                    }
                }
            }

            returnedActivityPackageToString
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get all packages
     */
    fun getFilteredPackageList(
        vararg filters: String,
        onProcessAppInfo: (List<ApplicationInfo>) -> Unit
    ) {
        Timber.d("getFilteredPackageList()")

        val installedAppList: List<ApplicationInfo> = getInstalledPackages() ?: emptyList()
        val filterPredicates: List<String> = (filters).asList()

        val appList = mutableListOf<ApplicationInfo>()

        installedAppList.forEach { appInfo ->
            if (filterPredicates.isEmpty()) {
                appList.add(appInfo)
            } else {
                filterPredicates.forEach { filter ->
                    if (appInfo.packageName.contains(filter)) {
                        appList.add(appInfo)
                    }
                }
            }
        }

        Timber.d("getFilteredPackageList() | appList size: ${appList.size}")

        onProcessAppInfo.invoke(appList)
    }


    fun callIntentForPackageActivity(
        activityResultLauncher: ActivityResultLauncher<Intent>? = null,
        intentPackageName: String,
        vararg extras: Pair<String, Any?>
    ) = applicationContext.packageManager.getLaunchIntentForPackage(intentPackageName)
        ?.runCatching {
            Timber.d("callIntentForPackageActivity()")
            extras.forEach { pair ->
                pair.second?.let { value ->
                    when (value) {
                        is String -> this.putExtra(pair.first, value)
                        else -> this.putExtra(pair.first, Json.encodeToString(pair.second))
                    }
                }
            }
            this
        }
        ?.onFailure { exception ->
            exception.printStackTrace()
            when (exception) {
                is PackageManager.NameNotFoundException -> Timber.e("Package $intentPackageName is not installed")
                else -> Timber.e("callIntentForPackageActivity() | onFailure | Error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
            }
        }
        ?.onSuccess {
            Timber.d("callIntentForPackageActivity() | Package $intentPackageName successfully found. Attempt to start target package")
            activityResultLauncher?.launch(it) ?: applicationContext.startActivity(it)
        }

    companion object {
        private var INSTANCE: LabPackageManager? = null

        @Synchronized
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: LabPackageManager(context).also { INSTANCE = it }
        }
    }
}