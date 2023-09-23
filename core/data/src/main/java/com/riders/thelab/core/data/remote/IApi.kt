package com.riders.thelab.core.data.remote

import android.app.Activity
import android.content.Context
import android.location.Location
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.data.local.model.Download
import com.riders.thelab.core.data.local.model.SpotifyRequestToken
import com.riders.thelab.core.data.local.model.Video
import com.riders.thelab.core.data.remote.dto.ApiResponse
import com.riders.thelab.core.data.remote.dto.SpotifyToken
import com.riders.thelab.core.data.remote.dto.UserDto
import com.riders.thelab.core.data.remote.dto.artist.Artist
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Query

interface IApi {

    // Google Cloud Auth
    suspend fun getStorageReference(activity: Activity): StorageReference?


    // GET
    suspend fun getArtists(url: String): List<Artist>
    suspend fun getVideos(): List<Video>
    suspend fun getWeatherOneCallAPI(location: Location): OneCallWeatherResponse?
    fun getBulkWeatherCitiesFile(): Call<ResponseBody>
    suspend fun getBulkDownload(context: Context): Flow<Download>
    suspend fun getApi(): ApiResponse


    // POST
    suspend fun login(user: UserDto): ApiResponse
    suspend fun saveUser(user: UserDto): ApiResponse

    // Spotify
    suspend fun getToken(requestToken: SpotifyRequestToken): SpotifyToken
    suspend fun getToken(
        @Query("client_id") clientId: String = "client_id",
        @Query("client_secret") clientSecret: String = "client_secret",
    ): SpotifyToken
}