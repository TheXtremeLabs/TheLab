package com.riders.thelab.data.remote

import android.app.Activity
import android.location.Location
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.data.remote.api.*
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class ApiImpl @Inject constructor(
    artistsAPIService: ArtistsAPIService,
    googleAPIService: GoogleAPIService,
    youtubeApiService: YoutubeApiService,
    weatherApiService: WeatherApiService,
    weatherBulkApiService: WeatherBulkApiService
) : IApi {

    private var mArtistsAPIService: ArtistsAPIService = artistsAPIService
    private var mGoogleAPIService: GoogleAPIService = googleAPIService
    private var mYoutubeApiService: YoutubeApiService = youtubeApiService
    private var mWeatherApiService: WeatherApiService = weatherApiService
    private var mWeatherBulkApiService: WeatherBulkApiService = weatherBulkApiService


    override suspend fun getStorageReference(activity: Activity): StorageReference {
        Timber.e("getStorageReference()")
        val storage = arrayOfNulls<FirebaseStorage>(1)
        var storageRef: StorageReference? = null

        // Initialize Firebase Auth
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInAnonymously()
            .addOnCompleteListener(activity) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInAnonymously:success")
                    val user = mAuth.currentUser
                    val bucketName = "gs://the-lab-3920e.appspot.com"
                    storage[0] = FirebaseStorage.getInstance(bucketName)
                    // Create a storage reference from our app
                    storageRef = storage[0]!!.reference
                    return@addOnCompleteListener

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w(
                        "signInAnonymously:failure %s",
                        task.exception.toString()
                    )
                    Toast.makeText(
                        activity,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@addOnCompleteListener
                }
            }

        return storageRef!!
    }

    override suspend fun getArtists(url: String): List<Artist> {
        Timber.e("getArtists()")
        return mArtistsAPIService.getArtists(url)
    }

    override suspend fun getVideos(): List<Video> {
        Timber.e("getVideos()")
        return mYoutubeApiService.fetchYoutubeVideos()
    }

    override suspend fun getWeatherOneCallAPI(location: Location): OneCallWeatherResponse {
        Timber.e("getWeatherOneCallAPI()")

        return mWeatherApiService
            .getCurrentWeatherWithNewOneCallAPI(
                location.latitude,
                location.longitude
            )
    }

    override suspend fun getBulkWeatherCitiesFile(): ResponseBody {
        Timber.e("get cities bulk file()")
        return mWeatherBulkApiService.getCitiesGZipFile()
    }
}