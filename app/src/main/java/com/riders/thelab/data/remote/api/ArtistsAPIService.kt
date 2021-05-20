package com.riders.thelab.data.remote.api

import com.riders.thelab.data.remote.dto.artist.Artist
import io.reactivex.rxjava3.core.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ArtistsAPIService {
    @GET
    fun getArtists(@Url url: String): Single<List<Artist>>

    @GET("{artistsPath}")
    fun getArtistsWithPath(@Path(value = "artistsPath") path: String?): Single<List<Artist>>
}