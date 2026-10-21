package com.riders.thelab.core.data.repository

import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.IDb
import com.riders.thelab.core.data.mapper.toModel
import com.riders.thelab.core.data.mapper.toDomain
import com.riders.thelab.core.data.remote.IApi
import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.core.domain.repository.IArtistsRepository
import kotlinx.coroutines.flow.map

class ArtistsRepository(private val db: IDb, private val api: IApi) : IArtistsRepository {

    override suspend fun insertAllArtists(artists: List<Artist>) = artists
        .mapIndexed { index, artist -> artist.toModel(index.toByte()) }
        .run { db.insertAllArtists(this) }


    override suspend fun getArtists(): List<Artist>? =
        db.getArtistsSync().map { it.toDomain() }

    override suspend fun fetchArtists(url: String): List<Artist>? = api.getArtists(url).map { it.toDomain() }

    override suspend fun getStorageReferenceAsResource(): Resource<StorageReference> =
        api.getStorageReferenceAsResource()
}