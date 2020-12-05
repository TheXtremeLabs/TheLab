package com.riders.thelab.data.remote.api;

import com.riders.thelab.data.local.model.Video;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface YoutubeApiService{

    //Method to retrieve the youtube content
    @GET("/florent37/MyYoutube/master/myyoutube.json")
    Single<List<Video>> fetchYoutubeVideos();
}
