package com.riders.thelab.core.domain.usecase.artists

import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.common.utils.runCatching
import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.core.domain.repository.IArtistsRepository
import com.riders.thelab.core.domain.utils.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetArtistsUseCase @Inject constructor(
    private val repository: IArtistsRepository
) : UseCase<Unit?, List<Artist>?> {

    override suspend fun invoke(params: Unit?): Resource<List<Artist>?> = runCatching(
        block = {
            val artists = repository.getArtists()
            Resource.Success(artists)
        },
        exceptionBlock = { exception ->
            Timber.e("GetArtistsUseCase() | onFailure | Error caught with message ${exception.message}")
        }
    )
        .getOrElse { exception ->
            Resource.Error(message = exception.message.toString(), throwable = exception)
        }
}