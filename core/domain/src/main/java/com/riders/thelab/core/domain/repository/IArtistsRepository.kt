package com.riders.thelab.core.domain.repository

import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.model.artist.Artist

interface IArtistsRepository {
    suspend fun insertAllArtists(artists: List<Artist>)
    suspend fun getArtists(): List<Artist>?
    suspend fun fetchArtists(url: String): List<Artist>?
    suspend fun getStorageReferenceAsResource(): Resource<StorageReference>
}