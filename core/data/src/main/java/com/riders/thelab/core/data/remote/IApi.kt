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
import com.riders.thelab.core.data.remote.dto.flight.Airport
import com.riders.thelab.core.data.remote.dto.flight.AirportFlightsResponse
import com.riders.thelab.core.data.remote.dto.flight.AirportsResponse
import com.riders.thelab.core.data.remote.dto.flight.AirportsSearchResponse
import com.riders.thelab.core.data.remote.dto.flight.Operator
import com.riders.thelab.core.data.remote.dto.flight.OperatorResponse
import com.riders.thelab.core.data.remote.dto.flight.SearchFlightResponse
import com.riders.thelab.core.data.remote.dto.spotify.SpotifyResponse
import com.riders.thelab.core.data.remote.dto.spotify.SpotifyToken
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBCreditsResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBMovieResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBTvShowsResponse
import com.riders.thelab.core.data.remote.dto.tmdb.TMDBVideoResponse
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotools.types.text.NotBlankString
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

    // FlightAware
    suspend fun getAirports(maxPages: Int = 1, cursor: String? = null): AirportsResponse
    suspend fun searchAirportById(query: String): AirportsSearchResponse

    suspend fun getAirportById(airportID: String): Airport
    suspend fun getAirportFlightsById(
        airportID: String,
        maxPages: Int = 1,
        cursor: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        type: String? = null
    ): AirportFlightsResponse

    suspend fun getOperators(maxPages: Int = 1, cursor: String? = null): OperatorResponse
    suspend fun getOperatorById(operatorID: String): Operator

    /**
     *
     * @param flightID The fa_flight_id to fetch. If looking for data from more than 10 days ago, please use the corresponding historical endpoint. Example: UAL1234-1234567890-airline-0123
     * @param height Height of requested image (pixels). Default: 480. Min 1┃Max 1500
     * @param width Width of requested image (pixels). Default:Default: 640. Min 1┃Max 1500
     * @param layerOn List of map layers to enable. Allowed: US Cities ┃ european country boundaries ┃ asia country boundaries ┃ major airports ┃ country boundaries ┃ US state boundaries ┃ water ┃ US major roads ┃ radar ┃ track ┃ flights ┃ airports
     * @param layerOff List of map layers to disable. Allowed: US Cities ┃ european country boundaries ┃ asia country boundaries ┃ major airports ┃ country boundaries ┃ US state boundaries ┃ water ┃ US major roads ┃ radar ┃ track ┃ flights ┃ airports
     * @param showDataBlock Whether a textual caption containing the ident, type, heading, altitude, origin, and destination should be displayed by the flight's position.
     * @param airportExpandView Whether to force zoom area to ensure origin/destination airports are visible. Enabling this flag forcefully enables the show_airports flag as well.
     * @param showAirports Whether to show the origin/destination airports for the flight as labeled points on the map.
     * @param boundingBox Manually specify the zoom area of the map using custom bounds. Should be a list of 4 coordinates representing the top, right, bottom, and left sides of the area (in that order).
     *
     * @return A flight's track as a base64-encoded image.
     * Image can contain a variety of additional data layers beyond just the track.
     * Data from up to 10 days ago can be obtained.
     * If looking for older data, please use the corresponding historical endpoint.
     */
    suspend fun getFlightOnMap(
        flightID: NotBlankString,
        height: Int = 1,
        width: Int = 1,
        layerOn: Array<NotBlankString>? = null,
        layerOff: Array<NotBlankString>? = null,
        showDataBlock: Boolean = false,
        airportExpandView: Boolean = false,
        showAirports: Boolean = false,
        boundingBox: Boolean = false,
    ): ByteArray

    /**
     * Search for airborne flights by matching against various parameters including geospatial data.
     * Uses a simplified query syntax compared to /flights/search/advanced.
     *
     * -prefix STRING
     * -type STRING
     * -idents STRING
     * -identOrReg STRING
     * -airline STRING
     * -destination STRING
     * -origin STRING
     * -originOrDestination STRING
     * -aboveAltitude INTEGER
     * -belowAltitude INTEGER
     * -aboveGroundspeed INTEGER
     * -belowGroundspeed INTEGER
     * -latlong "MINLAT MINLON MAXLAT MAXLON"
     * -filter {ga|airline}
     */
    suspend fun searchFlight(
        query: NotBlankString,
        maxPages: Int = 1,
        cursor: String? = null
    ): SearchFlightResponse
}