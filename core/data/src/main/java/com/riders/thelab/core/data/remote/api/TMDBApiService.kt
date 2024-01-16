package com.riders.thelab.core.data.remote.api

import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBTvShowsResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TMDBApiService {

    @GET("movie/now_playing")
    suspend fun getTrendingMovies(): TMDBMovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(): TMDBMovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): TMDBMovieResponse

    @GET("tv/airing_today")
    suspend fun getTrendingTvShows(): TMDBTvShowsResponse

    @GET("tv/popular")
    suspend fun getPopularTvShows(): TMDBTvShowsResponse

    @GET("movie/{movieID}/videos")
    suspend fun getMovieVideos(@Path("movieID") movieID: Int): TMDBVideoResponse?

    @GET("tv/{tvShowID}/videos")
    suspend fun getTvShowVideos(@Path("tvShowID") tvShowID: Int): TMDBVideoResponse?
}