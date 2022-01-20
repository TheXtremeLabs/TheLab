package com.riders.thelab.data.remote

import android.app.Activity
import android.location.Location
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.riders.thelab.TheLabApplication
import com.riders.thelab.data.local.model.Download
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.data.remote.api.*
import com.riders.thelab.data.remote.dto.LoginResponse
import com.riders.thelab.data.remote.dto.UserDto
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import retrofit2.Call
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.Continuation

class ApiImpl @Inject constructor(
    artistsAPIService: ArtistsAPIService,
    googleAPIService: GoogleAPIService,
    youtubeApiService: YoutubeApiService,
    weatherApiService: WeatherApiService,
    weatherBulkApiService: WeatherBulkApiService,
    userApiService: UserApiService,
) : IApi {

    private var mArtistsAPIService: ArtistsAPIService = artistsAPIService
    private var mGoogleAPIService: GoogleAPIService = googleAPIService
    private var mYoutubeApiService: YoutubeApiService = youtubeApiService
    private var mWeatherApiService: WeatherApiService = weatherApiService
    private var mWeatherBulkApiService: WeatherBulkApiService = weatherBulkApiService
    private var mUserApiService: UserApiService = userApiService


    override suspend fun getStorageReference(activity: Activity): StorageReference? {
        Timber.e("getStorageReference()")
        val storage = arrayOfNulls<FirebaseStorage>(1)
        var storageRef: StorageReference? = null

        return try {

            // Initialize Firebase Auth
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

            val task = mAuth.signInAnonymously().await()

            task.user?.let {
                Timber.d("signInAnonymously:success : $it")
            }

            if (null != mAuth.currentUser) {
                // Sign in success, update UI with the signed-in user's information
                Timber.d("signInAnonymously:success")
                val user = mAuth.currentUser
                val bucketName = "gs://the-lab-3920e.appspot.com"
                storage[0] = FirebaseStorage.getInstance(bucketName)
                // Create a storage reference from our app
                storageRef = storage[0]!!.reference
            }
            storageRef

        } catch (exception: Exception) {

            // If sign in fails, display a message to the user.
            Timber.w("signInAnonymously:failure %s", exception.toString())
            Toast.makeText(
                activity,
                "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()

            null
        }
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

    override fun getBulkWeatherCitiesFile(): Call<ResponseBody> {
        Timber.e("get cities bulk file()")
        return mWeatherBulkApiService.getCitiesZipFile()
    }


    override suspend fun getBulkDownload(): Flow<Download> {
        return mWeatherBulkApiService.getCitiesGZipFile()
            .downloadCitiesFile(
                TheLabApplication.getInstance().getContext().externalCacheDir!!,
                "my_file"
            )
    }


    private suspend fun ResponseBody.downloadCitiesFile(
        directory: File,
        filename: String
    ): Flow<Download> = flow {

        emit(Download.Started(true))
        Timber.d("downloadToFileWithProgress()")
        emit(Download.Progress(0))

        // flag to delete file if download errors or is cancelled
        var deleteFile = true
        val file = File(directory, "${filename}.${contentType()?.subtype}")

        try {

            byteStream().use { inputStream ->
                file.outputStream().use { outputStream ->
                    val totalBytes = contentLength()
                    val data = ByteArray(8_192)
                    var progressBytes = 0L

                    while (true) {
                        val bytes = inputStream.read(data)

                        if (bytes == -1) {
                            break
                        }

                        outputStream.channel
                        outputStream.write(data, 0, bytes)
                        progressBytes += bytes

                        emit(Download.Progress(percent = ((progressBytes * 100) / totalBytes).toInt()))
                    }

                    when {
                        progressBytes < totalBytes -> {
                            emit(Download.Done(true))
                            throw Exception("missing bytes")
                        }
                        progressBytes > totalBytes -> {
                            emit(Download.Done(true))
                            throw Exception("too many bytes")
                        }
                        else ->
                            deleteFile = false
                    }
                }
            }

            emit(Download.Finished(file))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Download.Error(true))
        } finally {
            // check if download was successful
            emit(Download.Done(true))

            if (deleteFile) {
                file.delete()
            }
        }
    }
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()


    override suspend fun login(user: UserDto): LoginResponse = mUserApiService.login(user)
}