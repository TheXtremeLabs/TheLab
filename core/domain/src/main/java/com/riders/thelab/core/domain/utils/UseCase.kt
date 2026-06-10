package com.riders.thelab.core.domain.utils

import com.riders.thelab.core.common.utils.Resource

interface UseCase<in Params, out Type> {

    suspend operator fun invoke(params: Params): Resource<Type>
}