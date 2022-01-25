package com.riders.thelab.data

import androidx.lifecycle.LiveData
import com.riders.thelab.data.local.IDb
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.preferences.IPreferences
import com.riders.thelab.data.remote.IApi

interface IRepository : IDb, IApi, IPreferences {

    /**
     * Get all packages and check if the returned list contains the target package
     */
    fun getPackageList(): List<App>


    fun getLocationStatusData(): LiveData<Boolean>

    fun addLocationStatusDataSource(data: LiveData<Boolean>)

    fun removeLocationStatusDataSource(data: LiveData<Boolean>)
}