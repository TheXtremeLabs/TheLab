package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.local.model.Video

import retrofit2.http.GET

interface YoutubeApiService {

    //Method to retrieve the youtube content
    @GET("/florent37/MyYoutube/master/myyoutube.json")
    suspend fun fetchYoutubeVideos(): List<Video>
}