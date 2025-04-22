package com.riders.thelab.feature.koin.data

import com.riders.thelab.core.data.utils.Resource
import com.riders.thelab.core.data.utils.toErrorType
import com.riders.thelab.feature.koin.data.remote.ApiImpl
import com.riders.thelab.feature.koin.data.remote.dto.PowerBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.koin.core.annotation.Single

@Single
class RepositoryImpl(apiImpl: ApiImpl) : IRepository {

    private val mApiImpl: ApiImpl = apiImpl


    override suspend fun fetchPowerBooks(): Flow<Resource<List<PowerBook>>> = mApiImpl
        .fetchPowerBooks()
        .catch { emit(Resource.ErrorWithType(it.toErrorType())) }


    override suspend fun fetchGoogle(): Resource<String> = this.mApiImpl.fetchGoogle()
}