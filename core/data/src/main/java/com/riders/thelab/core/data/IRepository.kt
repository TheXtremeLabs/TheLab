package com.riders.thelab.core.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.riders.thelab.core.data.local.IDb
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.preferences.IPreferences
import com.riders.thelab.core.data.remote.IApi

interface IRepository : IDb, IApi, IPreferences {

    /**
     * Get all packages and check if the returned list contains the target package
     */
    fun getAppListFromAssets(context: Context): List<App>

    /**
     * Get all packages and check if the returned list contains the target package
     */
    fun getPackageList(context: Context): List<App>


    fun getLocationStatusData(): LiveData<Boolean>

    fun addLocationStatusDataSource(data: LiveData<Boolean>)

    fun removeLocationStatusDataSource(data: LiveData<Boolean>)
}