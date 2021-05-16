package com.riders.thelab.data.remote.api;

import com.riders.thelab.data.remote.dto.artist.Artist;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ArtistsAPIService {

    @GET
    Single<List<Artist>> getArtists(@Url String url);

    @GET("{artistsPath}")
    Single<List<Artist>> getArtistsWithPath(@Path(value = "artistsPath") String path);
}
