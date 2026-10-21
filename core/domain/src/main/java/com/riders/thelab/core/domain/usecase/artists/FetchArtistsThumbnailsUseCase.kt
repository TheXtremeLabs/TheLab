package com.riders.thelab.core.domain.usecase.artists

import android.net.Uri
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.extensions.getTaskFlow
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.common.utils.runCatching
import com.riders.thelab.core.domain.usecase.GetStorageReferenceUseCase
import com.riders.thelab.core.domain.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FetchArtistsThumbnailsUseCase @Inject constructor(
    private val storageReferenceUseCase: GetStorageReferenceUseCase
) : UseCase<String, List<String>> {

    private val thumbnailsLinks: MutableList<String> = mutableListOf()

    override suspend fun invoke(params: String): Resource<List<String>> = runCatching(
        block = {
            when (val result = storageReferenceUseCase.invoke(params = params)) {
                is Resource.Error -> {
                    Resource.Error(result.message, throwable = result.throwable)
                }

                is Resource.Success -> {
                    val results: StorageReference? = result.data
                    val list: ListResult? = results?.listAll()?.await()
                    val items: List<StorageReference>? = list?.items

                    items?.let {
                        val thumbs = buildThumbnailList(it).first()
                        Resource.Success(thumbs)
                    } ?: Resource.Success(emptyList())
                }
            }
        },
        exceptionBlock = { throwable ->
            Timber.e("FetchArtistsThumbnailsUseCase.invoke | exceptionBlock | Error caught with message : ${throwable.message} (class : ${throwable.javaClass.canonicalName})")
        }
    )
        .getOrElse { exception -> Resource.Error(exception.message.toString(), exception) }


    private fun buildThumbnailList(
        storageReferences: List<StorageReference>
    ): Flow<List<String>> = callbackFlow {

        if (!LabCompatibilityManager.isNougat()) {
            Timber.i("buildArtistsThumbnailsListWithFlow() | below Android Nougat (API 24)")

            for (element in storageReferences) {
                element
                    .downloadUrl
                    .getTaskFlow<Uri>()
                    .collect { resourceResult ->
                        when (resourceResult) {
                            is Resource.Error -> {
                                Timber.e("buildArtistsThumbnailsListWithFlow | addOnFailureListener | message: ${resourceResult.message} (class: ${resourceResult.throwable?.javaClass?.canonicalName})")
                                return@collect
                            }

                            /*is Resource.ErrorWithType -> {
                                Timber.e("buildArtistsThumbnailsListWithFlow | addOnFailureListener | message: ${resourceResult.error} (class: ${resourceResult.error::class.java.canonicalName})")
                                return@collect
                            }*/

                            is Resource.Success -> {
                                Timber.i("buildArtistsThumbnailsListWithFlow() | addOnSuccessListener | uri: ${resourceResult.data.toString()}")
                                thumbnailsLinks.add(resourceResult.data.toString())
                                if (storageReferences.size == thumbnailsLinks.size) {
                                    trySend(
                                        thumbnailsLinks
                                            .also { Timber.d("buildArtistsThumbnailsListWithFlow | thumbnails Links size: ${thumbnailsLinks.size}") }
                                            .run { this.toList() }
                                    )
                                }
                            }
                        }
                    }
            }
        } else {
            Timber.i("buildArtistsThumbnailsListWithFlow() | above Android Nougat (API 24+)")

            storageReferences
                .stream()
                .forEach { itemReference: StorageReference ->
                    itemReference
                        .downloadUrl
                        .addOnSuccessListener { artistThumbUrl: Uri ->
                            Timber.i("buildArtistsThumbnailsListWithFlow() | addOnSuccessListener | uri: $artistThumbUrl")
                            thumbnailsLinks.add(artistThumbUrl.toString())
                            if (storageReferences.size == thumbnailsLinks.size) {
                                trySend(
                                    thumbnailsLinks
                                        .also { Timber.d("buildArtistsThumbnailsListWithFlow | thumbnails Links size: ${thumbnailsLinks.size}") }
                                        .run { this.toList() }
                                )

                            }
                        }
                        .addOnFailureListener { throwable ->
                            Timber.e(throwable)
                            Timber.e("buildArtistsThumbnailsListWithFlow | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                            return@addOnFailureListener
                        }
                        .addOnCompleteListener {
                            return@addOnCompleteListener
                        }
                }
        }

        awaitClose {

        }
    }
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
}