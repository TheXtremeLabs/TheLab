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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
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


    override fun getStorageReference(activity: Activity): Single<StorageReference> {
        return object : Single<StorageReference>() {
            override fun subscribeActual(observer: SingleObserver<in StorageReference?>) {
                val storage = arrayOfNulls<FirebaseStorage>(1)

                // Initialize Firebase Auth
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth
                        .signInAnonymously()
                        .addOnCompleteListener(
                                activity
                        ) { task: Task<AuthResult?> ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Timber.d("signInAnonymously:success")
                                val user = mAuth.currentUser
                                val bucketName = "gs://the-lab-3920e.appspot.com"
                                storage[0] = FirebaseStorage.getInstance(bucketName)
                                // Create a storage reference from our app
                                val storageRef = storage[0]!!.reference
                                observer.onSuccess(storageRef)
                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.w("signInAnonymously:failure %s",
                                        task.exception.toString())
                                Toast.makeText(
                                        activity,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                                observer.onError(task.exception!!)
                            }
                        }
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getArtists(url: String): Single<List<Artist>> {
        Timber.e("getArtists()")
        return mArtistsAPIService
                .getArtists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getVideos(): Single<List<Video>> {
        Timber.e("getVideos()")
        return mYoutubeApiService
                .fetchYoutubeVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getWeatherOneCallAPI(location: Location): Single<OneCallWeatherResponse>? {
        Timber.e("getWeatherOneCallAPI()")

        if (null == location) {
            Timber.e("Cannot perform request call object is null")
            return null
        }

        return mWeatherApiService
                .getCurrentWeatherWithNewOneCallAPI(
                        location.latitude,
                        location.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getBulkWeatherCitiesFile(): Single<ResponseBody> {
        Timber.e("get cities bulk file()")
        return mWeatherBulkApiService
                .getCitiesGZipFile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}