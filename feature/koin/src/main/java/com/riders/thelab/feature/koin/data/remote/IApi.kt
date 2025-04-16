package com.riders.thelab.feature.koin.data.remote

import com.riders.thelab.core.data.utils.Resource
import com.riders.thelab.feature.koin.data.remote.dto.PowerBook
import kotlinx.coroutines.flow.Flow

interface IApi {
    // Koin
    suspend fun fetchPowerBooks(): Flow<Resource<List<PowerBook>>>
    suspend fun fetchGoogle(): Resource<String>
}