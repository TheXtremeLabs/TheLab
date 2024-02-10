package com.riders.thelab.ui.recycler

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.ArtistsUiState
import com.riders.thelab.core.data.remote.dto.artist.Artist
import com.riders.thelab.core.ui.data.SnackBarType
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecyclerViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    val artistThumbnails = mutableListOf<String>()

    private val jsonURLFetched: MutableLiveData<String> = MutableLiveData()
    private val jsonURLError: MutableLiveData<Boolean> = MutableLiveData()
    private val artistsThumbnails: MutableLiveData<List<String>> = MutableLiveData()
    private val artistsThumbnailsError: MutableLiveData<Boolean> = MutableLiveData()
    private val artists: MutableLiveData<List<Artist>> = MutableLiveData()
    private val artistsError: MutableLiveData<Boolean> = MutableLiveData()

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _artistUiState = MutableStateFlow<ArtistsUiState>(ArtistsUiState.Loading)
    val artistUiState = _artistUiState

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }

    fun getJSONURLFetched(): LiveData<String> {
        return jsonURLFetched
    }

    fun getJSONURLError(): LiveData<Boolean> {
        return jsonURLError
    }

    fun getArtistsThumbnailsSuccessful(): LiveData<List<String>> {
        return artistsThumbnails
    }

    fun getArtistsThumbnailsError(): LiveData<Boolean> {
        return artistsThumbnailsError
    }

    fun getArtists(): LiveData<List<Artist>> {
        return artists
    }

    fun getArtistsError(): LiveData<Boolean> {
        return artistsError
    }

    fun getFirebaseJSONURL(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            try {

                val storageReference: StorageReference? = repository.getStorageReference(activity)

                // Create a child reference
                // imagesRef now points to "images"
                storageReference?.let {
                    val artistsRef: StorageReference = it.child("bulk/artists.json")

                    withContext(Dispatchers.Main) {
                        artistsRef
                            .downloadUrl
                            .addOnCompleteListener { artistTask: Task<Uri> ->
                                Timber.d("result : %s", artistTask.result.toString())
                                val result = artistTask.result.toString()
                                var url = ""
                                try {
                                    url = result.replace("%3D", "?")
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }

                                jsonURLFetched.value = url
                            }
                    }
                }

            } catch (throwable: Exception) {
                Timber.e(throwable)
                withContext(Dispatchers.Main) {
                    jsonURLError.value = true
                }
            }
        }
    }

    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    fun getFirebaseFiles(activity: Activity) {
        Timber.d("getFirebaseFiles()")
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {

            try {
                val storageReference: StorageReference? = repository.getStorageReference(activity)

                Timber.d("signInAnonymously:success")

                storageReference?.let { ref ->
                    // Create a child reference
                    // imagesRef now points to "images"
                    val imagesRef: StorageReference = ref.child("images/artists")

                    imagesRef
                        .listAll()
                        .addOnSuccessListener { Timber.d("onSuccess()") }
                        .addOnFailureListener { t: java.lang.Exception? -> Timber.e(t) }
                        .addOnCompleteListener { taskResult: Task<ListResult> ->
                            if (!taskResult.isSuccessful) {
                                Timber.e("error occurred. Please check logs.")
                            } else {
                                Timber.d(
                                    "onComplete() - with size of : %d element(s)",
                                    taskResult.result.items.size
                                )

                                viewModelScope.launch {
                                    // links: List<String>
                                    val job = buildArtistsThumbnailsList(taskResult.result.items)

                                    if (null == job) {
                                        withContext(Dispatchers.Main) {
                                            artistsThumbnailsError.value = true
                                        }
                                    } else {
                                        Timber.d("Links : %s", job.toString())
                                        if (taskResult.result.items.size == job.size) {
                                            withContext(Dispatchers.Main) {
                                                artistThumbnails.addAll(job)
                                                job.also { artistsThumbnails.value = it }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }

            } catch (throwable: Exception) {
                Timber.e(throwable)
            }
        }
    }

    @SuppressLint("NewApi")
    private suspend fun buildArtistsThumbnailsList(storageReferences: List<StorageReference>): List<String> {
        val thumbnailsLinks: MutableList<String> = ArrayList()
        if (!LabCompatibilityManager.isNougat()) {
            for (element in storageReferences) {
                element
                    .downloadUrl
                    .addOnSuccessListener { artistThumbUrl: Uri ->
                        thumbnailsLinks.add(
                            artistThumbUrl.toString()
                        )
                    }
                    .addOnFailureListener { throwable: java.lang.Exception? ->
                        Timber.e(throwable)
                        return@addOnFailureListener
                    }
                    .addOnCompleteListener {
                        return@addOnCompleteListener

                    }
            }
        } else {
            storageReferences
                .stream()
                .forEach { itemReference: StorageReference ->
                    itemReference
                        .downloadUrl
                        .addOnSuccessListener { artistThumbUrl: Uri ->
                            thumbnailsLinks.add(
                                artistThumbUrl.toString()
                            )
                        }
                        .addOnFailureListener { throwable: java.lang.Exception? ->
                            Timber.e(throwable)
                            return@addOnFailureListener
                        }
                        .addOnCompleteListener {
                            return@addOnCompleteListener
                        }
                }
        }
        delay(4_000)
        return thumbnailsLinks
    }

    fun fetchArtists(urlPath: String) {
        Timber.d("fetchArtists()")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val job = repository.getArtists(urlPath)

                _artistUiState.value = ArtistsUiState.Success(job)

                /*withContext(Dispatchers.Main) {
                    artists.value = job
                }*/
            } catch (throwable: Exception) {
                Timber.e(throwable)
                _artistUiState.value = ArtistsUiState.Error(throwable)
                /*withContext(Dispatchers.Main) {
                    artistsError.value = true
                }*/
            }
        }
    }


    fun onDetailClick(
        activity: Activity,
        item: Artist,
        sharedImageView: ShapeableImageView
    ) {
        Timber.d("onDetailClick(item, sharedImageView, position)")

        val intent = Intent(activity, RecyclerViewDetailActivity::class.java)

        intent.putExtra(RecyclerViewDetailActivity.EXTRA_RECYCLER_ITEM, Json.encodeToString(item))

        Timber.d("Apply activity transition")
        intent.putExtra(
            RecyclerViewDetailActivity.EXTRA_TRANSITION_ICON_NAME,
            ViewCompat.getTransitionName(sharedImageView)
        )

        // Apply activity transition
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity,
            sharedImageView,
            "icon"
        )
        // navigator: Navigator
        activity.startActivity(intent, options.toBundle())
    }
}