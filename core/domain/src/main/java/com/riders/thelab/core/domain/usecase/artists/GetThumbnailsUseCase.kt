package com.riders.thelab.core.domain.usecase.artists

import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.utils.UseCase
import javax.inject.Inject

class GetThumbnailsUseCase @Inject constructor(): UseCase<Unit?, Boolean> {
    override suspend fun invoke(params: Unit?): Resource<Boolean> = runCatching(block = {
        TODO("Not yet implemented")
    })
        .getOrElse { exception->Resource.Error(exception.message.toString(), exception) }
}