package com.riders.thelab.core.data.remote

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.location.Location
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.data.local.model.Download
import com.riders.thelab.core.data.local.model.SpotifyRequestToken
import com.riders.thelab.core.data.local.model.Video
import com.riders.thelab.core.data.remote.dto.ApiResponse
import com.riders.thelab.core.data.remote.dto.UserDto
import com.riders.thelab.core.data.remote.dto.artist.Artist
import com.riders.thelab.core.data.remote.dto.spotify.SpotifyResponse
import com.riders.thelab.core.data.remote.dto.spotify.SpotifyToken
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBCreditsResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBTvShowsResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBVideoResponse
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call

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
        clientId: String = "client_id",
        clientSecret: String = "client_secret",
    ): SpotifyToken

    suspend fun getTrackInfo(bearerToken: String, trackId: String): SpotifyResponse


    // Download
    fun getDownloadManager(context: Context): DownloadManager
    fun downloadFile(context: Context, url: String): Long
    fun cancelDownload(downloadId: Long): Int
    fun cancelDownloads(downloadIds: List<Long>): Int

    // TMDBModel
    suspend fun getTrendingMovies(): TMDBMovieResponse
    suspend fun getPopularMovies(): TMDBMovieResponse
    suspend fun getUpcomingMovies(): TMDBMovieResponse
    suspend fun getTrendingTvShows(): TMDBTvShowsResponse
    suspend fun getPopularTvShows(): TMDBTvShowsResponse
    suspend fun getMovieVideos(movieID: Int): TMDBVideoResponse?
    suspend fun getTvShowVideos(thShowID: Int): TMDBVideoResponse?
    suspend fun getMovieCredits(movieID: Int): TMDBCreditsResponse?
}