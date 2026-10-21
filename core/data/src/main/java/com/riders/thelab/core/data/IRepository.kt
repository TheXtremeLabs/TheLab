package com.riders.thelab.core.data

import androidx.lifecycle.LiveData
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.data.local.IDb
import com.riders.thelab.core.data.preferences.IPreferences
import com.riders.thelab.core.data.remote.IApi
import com.riders.thelab.core.data.remote.dto.artist.Artist

interface IRepository : IDb, IApi, IPreferences {

    fun getLocationStatusData(): LiveData<Boolean>

    fun addLocationStatusDataSource(data: LiveData<Boolean>)

    fun removeLocationStatusDataSource(data: LiveData<Boolean>)


    /////////////////////////////////////////////////////
    //
    // API
    //
    /////////////////////////////////////////////////////
    suspend fun getArtistsResource(url: String): Resource<List<Artist>>
}