package com.riders.thelab.feature.artists

import android.annotation.SuppressLint
import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.data.local.model.music.toModel
import com.riders.thelab.core.data.remote.dto.artist.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

object ArtistsManager {

    fun convertArtistsToModel(
        listOfArtistDto: List<Artist>,
        artistThumbnails: List<String>
    ): List<ArtistModel> {
        Timber.d("convertArtistsToModel() | artists dto size : ${listOfArtistDto.size}, thumbnails size : ${artistThumbnails.size}, ")

        return listOfArtistDto
            .mapIndexed { index: Int, artist: Artist -> artist.toModel(index.toByte()) }
            .onEach { artist ->
                Timber.d("convertArtistsToModel() | url : ${artist.urlThumb}")
                artist.urlThumb = artistThumbnails.firstOrNull {
                    it.contains(artist.urlThumb)
                } ?: ""
            }
    }

    @SuppressLint("NewApi")
    fun buildArtistsThumbnailsList(
        storageReferences: List<StorageReference>
    ): Flow<List<String>> = callbackFlow {
        Timber.d("buildArtistsThumbnailsList()")

        val thumbnailsLinks: MutableList<String> = mutableListOf()

        if (!LabCompatibilityManager.isNougat()) {
            Timber.i("buildArtistsThumbnailsList() | below Android Nougat (API 24)")

            for (element in storageReferences) {
                element
                    .downloadUrl
                    .addOnSuccessListener { artistThumbUrl: Uri ->
                        Timber.i("buildArtistsThumbnailsList() | addOnSuccessListener | uri: ${artistThumbUrl.toString()}")
                        thumbnailsLinks.add(artistThumbUrl.toString())
                        if (storageReferences.size == thumbnailsLinks.size) {
                            trySend(
                                thumbnailsLinks
                                    .also { Timber.d("buildArtistsThumbnailsList | thumbnails Links size: ${thumbnailsLinks.size}") }
                                    .run { this.toList() }
                            )
                        }
                    }
                    .addOnFailureListener { throwable ->
                        Timber.e(throwable)
                        Timber.e("buildArtistsThumbnailsList | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                        return@addOnFailureListener
                    }
                    .addOnCompleteListener {
                        return@addOnCompleteListener
                    }
            }
        } else {
            Timber.i("buildArtistsThumbnailsList() | above Android Nougat (API 24+)")

            storageReferences
                .stream()
                .forEach { itemReference: StorageReference ->
                    itemReference
                        .downloadUrl
                        .addOnSuccessListener { artistThumbUrl: Uri ->
                            Timber.i("buildArtistsThumbnailsList() | addOnSuccessListener | uri: ${artistThumbUrl.toString()}")
                            thumbnailsLinks.add(artistThumbUrl.toString())
                            if (storageReferences.size == thumbnailsLinks.size) {
                                trySend(
                                    thumbnailsLinks
                                        .also { Timber.d("buildArtistsThumbnailsList | thumbnails Links size: ${thumbnailsLinks.size}") }
                                        .run { this.toList() }
                                )
                            }
                        }
                        .addOnFailureListener { throwable ->
                            Timber.e(throwable)
                            Timber.e("buildArtistsThumbnailsList | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                            return@addOnFailureListener
                        }
                        .addOnCompleteListener {
                            return@addOnCompleteListener
                        }
                }
        }

        awaitClose {
            Timber.d("buildArtistsThumbnailsList | awaitClose")
        }
    }
        .catch {
            Timber.e("buildArtistsThumbnailsList | catch | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
        }
        .flowOn(Dispatchers.Main)

}