package com.riders.thelab.feature.artists

import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.artists.ArtistsUiState
import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalKotoolsTypesApi::class)
@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel(), CoroutineScope, DefaultLifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var mWeakReference: WeakReference<ArtistsActivity>? = null
    private var bucketUrl: String? = null
    private val artistThumbnails = mutableListOf<String>()

    private var fetchJsonJob: Job? = null
    private var fetchArtistsJob: Job? = null
    private var fetchArtistsThumbJob: Job? = null
    private var mStorageReference: StorageReference? = null

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _artistUiState: MutableStateFlow<ArtistsUiState> = MutableStateFlow(
        ArtistsUiState.Loading(NotBlankString.create("Loading..."))
    )
    val artistUiState: StateFlow<ArtistsUiState> = _artistUiState
        .asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = ArtistsUiState.Loading(NotBlankString.create("Loading...")),
            started = SharingStarted.WhileSubscribed(5_000),
        )

    private fun updateArtistUiState(newState: ArtistsUiState) {
        this._artistUiState.value = newState
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }

    //////////////////////////////////////////
    //
    // OVERRIDE
    //
    //////////////////////////////////////////
    override fun onCleared() {
        Timber.e("onCleared()")
        cancelJobs()
        super.onCleared()
    }

    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
    fun initWeakReference(activity: ArtistsActivity) {
        if (null == mWeakReference) {
            mWeakReference = WeakReference(activity)
        }
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    private fun getFirebaseJSONURL() {

        mWeakReference?.get()?.let { activity ->
            Timber.d("getFirebaseJSONURL()")

            fetchJsonJob = viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
                try {
                    if (null == mStorageReference) {
                        updateArtistUiState(ArtistsUiState.Loading(NotBlankString.create("Authenticating to the server...")))

                        mStorageReference = repository.getStorageReference(activity)
                    }

                    // Create a child reference
                    // imagesRef now points to "images"
                    mStorageReference?.let {
                        val artistsRef: StorageReference = it.child("bulk/artists.json")


                        withContext(Dispatchers.Main) {
                            artistsRef
                                .downloadUrl
                                .addOnFailureListener { throwable ->
                                    Timber.e("getFirebaseJSONURL | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                                }
                                .addOnCompleteListener { artistTask: Task<Uri> ->
                                    Timber.d("getFirebaseJSONURL | addOnCompleteListener| result : ${artistTask.result}")
                                    val result = artistTask.result.toString()
                                    val url = try {
                                        bucketUrl = result.replace("%3D", "?")
                                    } catch (exception: Exception) {
                                        exception.printStackTrace()
                                        Timber.e("getFirebaseJSONURL | Error caught with message: ${exception.message} (class: ${exception::class.java.canonicalName})")
                                    }

                                    getFirebaseFiles()
                                }
                        }
                    }

                } catch (throwable: Exception) {
                    Timber.e(throwable)
                    withContext(Dispatchers.Main) {
                        updateArtistUiState(
                            ArtistsUiState.Error(
                                message = NotBlankString.create(
                                    throwable.message
                                        ?: NotBlankString.create("Error occurred while getting value")
                                ),
                                errorResponse = throwable
                            )
                        )
                    }
                }
            }
        } ?: run { Timber.e("getFirebaseJSONURL() | Unable to get activity") }
    }


    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun getFirebaseFiles() {

        mWeakReference?.get()?.let { activity ->
            Timber.d("getFirebaseFiles()")

            fetchArtistsThumbJob =
                viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {

                    try {
                        if (null == mStorageReference) {
                            mStorageReference = repository.getStorageReference(activity)
                        }

                        Timber.i("getFirebaseFiles() | signInAnonymously:success")

                        mStorageReference?.let { ref ->
                            // Create a child reference
                            // imagesRef now points to "images"
                            val imagesRef: StorageReference = ref.child("images/artists")

                            updateArtistUiState(ArtistsUiState.Loading(NotBlankString.create("Fetching Artists data...")))

                            imagesRef
                                .listAll()
                                .addOnSuccessListener { Timber.d("onSuccess()") }
                                .addOnFailureListener { throwable ->
                                    Timber.e("getFirebaseFiles | addOnFailureListener | message: ${throwable.message} (class: ${throwable::class.java.canonicalName})")
                                }
                                .addOnCompleteListener { taskResult: Task<ListResult> ->
                                    if (!taskResult.isSuccessful) {
                                        Timber.e("getFirebaseFiles | addOnCompleteListener | error occurred. Please check logs.")
                                    } else {
                                        Timber.d(
                                            "getFirebaseFiles | addOnCompleteListener |  with size of : %d element(s)",
                                            taskResult.result.items.size
                                        )

                                        viewModelScope.launch {
                                            ArtistsManager
                                                .buildArtistsThumbnailsList(taskResult.result.items)
                                                .collect { thumbList ->
                                                    if (thumbList.isEmpty()) {
                                                        withContext(Dispatchers.Main) {
                                                            updateArtistUiState(
                                                                ArtistsUiState.Error(
                                                                    message = NotBlankString.create(
                                                                        "Thumbnail list is Empty"
                                                                    ),
                                                                    errorResponse = null
                                                                )
                                                            )
                                                        }
                                                    } else {
                                                        Timber.d(
                                                            "getFirebaseFiles | addOnCompleteListener | Links : %s",
                                                            thumbList.toString()
                                                        )

                                                        if (taskResult.result.items.size == thumbList.size) {
                                                            updateArtistUiState(
                                                                ArtistsUiState.Loading(
                                                                    NotBlankString.create("Fetching successful. Please wait a few moment..")
                                                                )
                                                            )

                                                            withContext(Dispatchers.Main) {
                                                                artistThumbnails.addAll(thumbList)
                                                                thumbList.also {
                                                                    bucketUrl?.let { fetchArtists(it) }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                }
                        }

                    } catch (throwable: Exception) {
                        Timber.e(throwable)
                        withContext(Dispatchers.Main) {
                            updateArtistUiState(
                                ArtistsUiState.Error(
                                    message = NotBlankString.create(
                                        throwable.message
                                            ?: NotBlankString.create("Error occurred while getting value")
                                    ),
                                    errorResponse = throwable
                                )
                            )
                        }
                    }
                }
        } ?: run { Timber.e("getFirebaseFiles() | Unable to get activity") }
    }


    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun fetchArtists(urlPath: String) {
        Timber.d("fetchArtists() | url: $urlPath")

        fetchArtistsJob = viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            try {
                val artists: List<ArtistModel> = repository.getArtists(urlPath).run {
                    ArtistsManager.convertArtistsToModel(this, artistThumbnails)
                }

                _artistUiState.value = ArtistsUiState.Success(artists)
            } catch (throwable: Exception) {
                Timber.e(throwable)
                withContext(Dispatchers.Main) {
                    updateArtistUiState(
                        ArtistsUiState.Error(
                            message = NotBlankString.create(
                                throwable.message
                                    ?: NotBlankString.create("Error occurred while getting value")
                            ),
                            errorResponse = throwable
                        )
                    )
                }
            }
        }
    }

    private fun cancelJobs() {
        Timber.e("cancelJobs() | cancelling all jobs...")

        if (null != fetchJsonJob && true == fetchJsonJob?.isActive) {
            fetchJsonJob?.cancel()
        }
        fetchJsonJob = null

        if (null != fetchArtistsJob && true == fetchArtistsJob?.isActive) {
            fetchArtistsJob?.cancel()
        }
        fetchArtistsJob = null

        if (null != fetchArtistsThumbJob && true == fetchArtistsThumbJob?.isActive) {
            fetchArtistsThumbJob?.cancel()
        }
        fetchArtistsThumbJob = null
    }


    //////////////////////////////////////////
    //
    // IMPLEMENTS
    //
    //////////////////////////////////////////
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.d("onCreate()")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
        getFirebaseJSONURL()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Timber.e("onDestroy()")
    }
}