package com.riders.thelab.core.common.utils

import android.content.Context
import android.content.pm.PackageManager
import timber.log.Timber

class LabPackageManager(private val applicationContext: Context) {

    fun getActivityPackageName(activityName: String): String? {
        Timber.d("getActivityPackageName()")

        val pManager: PackageManager = applicationContext.packageManager
        val packageName: String = applicationContext.applicationContext.packageName

        var returnedActivityPackageToString: String? = null

        return try {
            val list =
                pManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities

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
}