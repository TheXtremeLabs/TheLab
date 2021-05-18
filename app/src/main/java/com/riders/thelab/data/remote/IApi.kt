package com.riders.thelab.data.remote

import android.app.Activity
import android.location.Location
import com.google.firebase.storage.StorageReference
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import io.reactivex.rxjava3.core.Single

import okhttp3.ResponseBody

interface IApi {

    // Google Cloud Auth
    fun getStorageReference(activity: Activity): Single<StorageReference>


    // GET
    fun getArtists(url: String): Single<List<Artist>>
    fun getVideos(): Single<List<Video>>
    fun getWeatherOneCallAPI(location: Location): Single<OneCallWeatherResponse>?
    fun getBulkWeatherCitiesFile(): Single<ResponseBody>
}