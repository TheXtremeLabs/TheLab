package com.riders.thelab.core.domain.usecase.artists

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.common.utils.runCatching
import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.core.domain.repository.IArtistsRepository
import com.riders.thelab.core.domain.usecase.GetStorageReferenceUseCase
import com.riders.thelab.core.domain.utils.UseCase
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FetchArtistsUseCase @Inject constructor(
    private val getStorageReferenceUseCase: GetStorageReferenceUseCase,
    private val repository: IArtistsRepository
) : UseCase<Unit?, List<Artist>?> {

    override suspend fun invoke(params: Unit?): Resource<List<Artist>?> = runCatching(
        block = {
            val result: Resource<StorageReference?> =
                getStorageReferenceUseCase.invoke(params = "bulk/artists.json")

            when (result) {
                is Resource.Error -> {
                    Resource.Error(result.message, result.throwable)
                }

                is Resource.Success -> {
                    val artistUri: Uri? = result.data?.downloadUrl?.await()

                    val url: String? = try {
                        artistUri?.toString()?.replace("%3D", "?")//?.replace("%2F", "_")
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        Timber.e("FetchArtistsUseCase.invoke | Error caught with message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                        null
                    }

                    url?.let {
                        val artists: List<Artist>? = repository.fetchArtists(it)
                        Resource.Success(artists)
                    } ?: Resource.Error("Error occurred while getting value", null)
                }
            }
        },
        exceptionBlock = { throwable: Throwable ->
            Timber.e("FetchArtistsUseCase.invoke | exceptionBlock | Error caught with message : ${throwable.message} (class : ${throwable.javaClass.canonicalName})")
        }
    )
        .getOrElse { exception -> Resource.Error(exception.message.toString(), exception) }


    /*override suspend fun invoke(params: Unit?): Resource<String?> {

        var bucketUrl: String? = null

        try {
            when (val mStorageReferenceResource = repository.getStorageReferenceAsResource()) {
                is Resource.Success -> {
                    // Create a child reference
                    // imagesRef now points to "images"
                    val artistsRef: StorageReference = mStorageReferenceResource
                        .data
                        .also { Timber.d("FetchArtistsUseCase.invoke() | storage reference : ${it.name}") }
                        .child("bulk/artists.json")

                    val artistUri: Uri = artistsRef.downloadUrl.await()

                    val url = try {
                        bucketUrl = artistUri.toString().replace("%3D", "?")
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        Timber.e("FetchArtistsUseCase.invoke | Error caught with message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                    }
                    return Resource.Success(bucketUrl)
                }

                is Resource.Error -> {
                    return Resource.Error(
                        message = mStorageReferenceResource.message,
                        throwable = mStorageReferenceResource.throwable
                    )
                }
            }
        } catch (throwable: Exception) {
            Timber.e(throwable)
            return Resource.Error(
                message = throwable.message ?: "Error occurred while getting value",
                throwable = throwable
            )
        }
    }*/
}