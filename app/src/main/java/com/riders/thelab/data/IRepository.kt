package com.riders.thelab.data

import com.riders.thelab.data.local.IDb
import com.riders.thelab.data.local.model.App
import com.riders.thelab.data.remote.IApi

interface IRepository : IDb, IApi {

    /**
     * Get all packages and check if the returned list contains the target package
     */
    fun getPackageList(): List<App>
}