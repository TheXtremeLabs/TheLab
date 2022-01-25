package com.riders.thelab.data.remote

import android.app.Activity
import android.location.Location
import com.google.firebase.storage.StorageReference
import com.riders.thelab.data.local.model.Download
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.data.remote.dto.LoginResponse
import com.riders.thelab.data.remote.dto.UserDto
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import kotlinx.coroutines.flow.Flow


import okhttp3.ResponseBody
import retrofit2.Call
import kotlin.coroutines.Continuation

interface IApi {

    // Google Cloud Auth
    suspend fun getStorageReference(activity: Activity): StorageReference?


    // GET
    suspend fun getArtists(url: String): List<Artist>
    suspend fun getVideos(): List<Video>
    suspend fun getWeatherOneCallAPI(location: Location): OneCallWeatherResponse?
    fun getBulkWeatherCitiesFile(): Call<ResponseBody>
    suspend fun getBulkDownload(): Flow<Download>


    // POST
    suspend fun login(user: UserDto): LoginResponse
}