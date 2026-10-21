package com.riders.thelab.core.domain.usecase.artists

import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.common.utils.runCatching
import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.core.domain.repository.IArtistsRepository
import com.riders.thelab.core.domain.utils.UseCase
import javax.inject.Inject

class SaveArtistsUseCase @Inject constructor(
    private val repository: IArtistsRepository
) : UseCase<List<Artist>, Boolean> {

    override suspend fun invoke(params: List<Artist>): Resource<Boolean> = runCatching(
        block = {
            repository.insertAllArtists(params)
            Resource.Success(true)
        },
        exceptionBlock = {}
    )
        .getOrElse { exception -> Resource.Error(exception.message.toString(), exception) }
}