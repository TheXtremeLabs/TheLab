package com.riders.thelab.core.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import timber.log.Timber

object LabAppManager {

    val TARGET_PACKAGES = arrayOf(
        "com.riders",
        "com.reepling",
        "com.praeter"
    )

    /**
     * Get all packages and check if the returned list contains the target package
     */
    inline fun <reified T> Context.getAppListFromAssets(): List<T>? = LabParser
        .parseJsonFile<List<T>>(
            context = this,
            filename = "app_list.json"
        )
        ?.also { list -> Timber.d("getAppListFromAssets() | size: ${list.size}") }
        ?.run { this }

    /**
     * Get all packages and check if the returned list contains the target package
     */
    inline fun <reified T> Context.getPackageList(onProcessAppInfo: (ApplicationInfo) -> Unit): List<T> {
        Timber.d("getPackageList()")
        val installedAppList: List<ApplicationInfo> = ArrayList()
        val appList: MutableList<T> = ArrayList()

        if (this.isPackageExists(TARGET_PACKAGES)) {
            for (appInfo in installedAppList) {
                Timber.e("package found : %s", appInfo.packageName)
                try {
                    onProcessAppInfo.invoke(appInfo)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else {
            Timber.e("package %s not found.", TARGET_PACKAGES.contentToString())
        }

        return appList
    }


    @SuppressLint("QueryPermissionsNeeded")
    fun Context.isPackageExists(targetPackages: Array<String>): Boolean {
        var isPackageFound = false

        // First Method
        val packages: List<ApplicationInfo>
        val packageManager: PackageManager = this.packageManager
        packages = packageManager.getInstalledApplications(0)

        for (packageInfo in packages) {
            for (packageItem in targetPackages) {
                if (packageInfo.packageName.contains(packageItem)) {

                    // Store found app package name
                    // val appToAdd = packageInfo.packageName

                    // Check if it does equal to The Lab package name
                    // because we don't don't want to display it
                    // TODO: Refactor
                    /*if (appToAdd != TheLabApplication.getInstance().getLabPackageName())
                        installedAppList.add(packageInfo)*/
                    isPackageFound = true
                }
            }
        }
        return isPackageFound

        // Second method
        /*try {
            PackageInfo info = packageManager
                    .getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return false
        }
        return true;
        */
    }
}