package com.riders.thelab.data.remote;

import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.data.remote.api.YoutubeApiService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class LabService {

    YoutubeApiService youtubeApiService;

    @Inject
    public LabService(YoutubeApiService youtubeApiService) {
        Timber.d("LabService()");
        this.youtubeApiService = youtubeApiService;
    }


    /////////////////////////////////////
    //
    // REST API
    //
    /////////////////////////////////////

    /**
     * **********************
     * GET
     * *********************
     */
    public Single<List<Video>> getVideos() {
        Timber.e("getVideos()");
        return youtubeApiService
                .fetchYoutubeVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
