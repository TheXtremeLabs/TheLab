package com.riders.thelab.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.LabParser
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.PackageApp
import timber.log.Timber

data class MockApp(
    @StringRes val title: Int,
    val description: String,
    @DrawableRes val drawable: Int,
    val date: String
)

object LabAppManager {
    private val TARGET_PACKAGES = arrayOf(
        "com.riders",
        "com.reepling",
        "com.praeter"
    )

    val mockApps = listOf<MockApp>(
        // Colors
        MockApp(
            R.string.activity_title_colors,
            "Change color programmatically...",
            R.drawable.logo_colors,
            "2015/01/20"
        ),
        // Jetpack Compose
        MockApp(
            R.string.activity_title_compose,
            "Jetpack Compose is Android’s modern toolkit for building native UI with less code, powerful tools, and intuitive Kotlin APIs...",
            R.drawable.jetpack_compose,
            "2023/01/29"
        ),

        // Screen shot
        MockApp(
            R.string.activity_title_screen_shot,
            "Screen Shot the device display programmatically...",
            R.drawable.ic_fullscreen,
            "2021/10/13"
        )
    )

    /**
     * @return the list of activities developed in TheLab App
     */
    fun getActivityList(context: Context): List<App> {
        return ArrayList(AppBuilderUtils.buildActivities(context))
    }


    /**
     * Get all packages and check if the returned list contains the target package
     */
    fun getAppListFromAssets(context: Context): List<App> {
        val list = LabParser.parseJsonFile<List<App>>(
            context = context,
            filename = "app_list.json"
        )!!

        Timber.d("getAppListFromAssets() | size: ${list.size}")
        return list
    }

    /**
     * Get all packages and check if the returned list contains the target package
     */
    fun getPackageList(context: Context): List<App> {

        val installedAppList: List<ApplicationInfo> = ArrayList()

        val appList: MutableList<App> = ArrayList()

        if (isPackageExists(context, TARGET_PACKAGES)) {
            for (appInfo in installedAppList) {
                Timber.e("package found : %s", appInfo.packageName)
                try {

                    val icon: Drawable =
                        context.packageManager.getApplicationIcon(appInfo.packageName)
                    val pInfo: PackageInfo =
                        context.packageManager.getPackageInfo(appInfo.packageName, 0)
                    val version = pInfo.versionName
                    val packageName = appInfo.packageName
                    appList.add(
                        PackageApp(
                            context.packageManager.getApplicationLabel(appInfo).toString(),
                            icon,
                            version,
                            packageName
                        )
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else {
            Timber.e("package %s not found.", TARGET_PACKAGES.contentToString())
            //installPackage(directory, targetApkFile);
        }

        return appList
    }


    @SuppressLint("QueryPermissionsNeeded")
    fun isPackageExists(
        context: Context,
        targetPackages: Array<String>
    ): Boolean {
        var isPackageFound = false

        // First Method
        val packages: List<ApplicationInfo>
        val packageManager: PackageManager = context.packageManager
        packages = packageManager.getInstalledApplications(0)

        for (packageInfo in packages) {
            for (packageItem in targetPackages) {
                if (packageInfo.packageName.contains(packageItem)) {

                    // Store found app package name
                    val appToAdd = packageInfo.packageName

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