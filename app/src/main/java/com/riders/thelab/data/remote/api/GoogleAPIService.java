package com.riders.thelab.data.remote.api;

import com.riders.thelab.data.remote.dto.directions.Directions;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleAPIService {


    // Places

    // Directions
    @GET("directions/json")
    Single<Directions> getDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key);
}
