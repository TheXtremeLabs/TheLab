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
import com.riders.thelab.core.data.utils.Resource
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalKotoolsTypesApi::class)
@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val repository: IRepository,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository), CoroutineScope, DefaultLifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var bucketUrl: String? = null
    private val artistThumbnails = mutableListOf<String>()

    private var mStorageReference: StorageReference? = null

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _artistUiState: MutableStateFlow<ArtistsUiState> = MutableStateFlow(
        ArtistsUiState.Loading("Loading...".toNotBlankString().getOrThrow())
    )
    val artistUiState: StateFlow<ArtistsUiState> = _artistUiState
        .asStateFlow()
        .stateIn(
            scope = viewModelScope,
            initialValue = ArtistsUiState.Loading("Loading...".toNotBlankString().getOrThrow()),
            started = SharingStarted.WhileSubscribed(5_000),
        )

    private fun updateArtistUiState(newState: ArtistsUiState) {
        this._artistUiState.update { newState }
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private var fetchJsonJob: Job? = null
    private var fetchArtistsJob: Job? = null
    private var fetchArtistsThumbJob: Job? = null
    private var updateArtistJob: Job? = null

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("fetchJsonExceptionHandler | Error caught with message : ${throwable.message} (class : ${throwable::class.java.canonicalName})")
        }

    private val fetchJsonExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Timber.e("fetchJsonExceptionHandler | Error caught with message : ${throwable.message} (class : ${throwable::class.java.canonicalName})")
    }

    private val updateArtistExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        Timber.e("updateArtistExceptionHandler | Error caught with message : ${throwable.message} (class : ${throwable::class.java.canonicalName})")
    }

    //////////////////////////////////////////
    //
    // OVERRIDE
    //
    //////////////////////////////////////////
    init {
        Timber.d("init method")
    }

    override fun onCleared() {
        Timber.e("onCleared()")
        cancelJobs()
        super.onCleared()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.d("onCreate()")

        val hasArtistsRecords: Boolean = repository.getArtistsSync().isNotEmpty()

        if (!hasArtistsRecords) {
            getFirebaseJSONURL()
        } else {
            viewModelScope.launch(coroutineContext) {
                updateArtistUiState(
                    ArtistsUiState.Loading(
                        "Artists records found....".toNotBlankString().getOrThrow()
                    )
                )
                delay(2.toDuration(DurationUnit.SECONDS))

                updateArtistUiState(
                    ArtistsUiState.Loading(
                        "Loading. Please wait....".toNotBlankString().getOrThrow()
                    )
                )
                delay(2.toDuration(DurationUnit.SECONDS))

                repository.getArtists().collect { artistModels ->
                    withContext(Dispatchers.Main) {
                        updateArtistUiState(ArtistsUiState.Success(artistModels))
                    }
                }
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Timber.e("onDestroy()")
    }

    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnUpdateArtistWithImage -> updateArtistInDatabase(event.artist)
            else -> return
        }
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    private fun getFirebaseJSONURL() {
        if (null == mWeakReference?.get()) {
            Timber.e("getFirebaseJSONURL() | Unable to get activity")
            return
        }

        fetchJsonJob = viewModelScope.launch(coroutineContext + fetchJsonExceptionHandler) {
            try {
                if (null == mStorageReference) {
                    updateArtistUiState(
                        ArtistsUiState.Loading(
                            message = "Authenticating to the server...".toNotBlankString()
                                .getOrThrow()
                        )
                    )
                }

                val mStorageReferenceResource = repository.getStorageReferenceAsResource()
                when (mStorageReferenceResource) {
                    is Resource.Success -> {
                        // Create a child reference
                        // imagesRef now points to "images"
                        val artistsRef: StorageReference = mStorageReferenceResource
                            .data
                            .also { Timber.d("getFirebaseJSONURL() | storage reference : ${it.name}") }
                            .child("bulk/artists.json")

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

                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            updateArtistUiState(
                                ArtistsUiState.Error(
                                    message = mStorageReferenceResource.message,
                                    errorResponse = mStorageReferenceResource.throwable
                                )
                            )
                        }
                    }

                    else -> Timber.e("getFirebaseJSONURL() | Unhandled type")
                }

            } catch (throwable: Exception) {
                Timber.e(throwable)
                withContext(Dispatchers.Main) {
                    updateArtistUiState(
                        ArtistsUiState.Error(
                            message = throwable.message
                                ?.toNotBlankString()
                                ?.getOrThrow()
                                ?: "Error occurred while getting value".toNotBlankString()
                                    .getOrThrow(),
                            errorResponse = throwable
                        )
                    )
                }
            }
        }
    }


    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun getFirebaseFiles() {

        (mWeakReference?.get() as? ArtistsActivity)?.let { activity ->
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

                            updateArtistUiState(
                                ArtistsUiState.Loading(
                                    message = "Fetching Artists data...".toNotBlankString()
                                        .getOrThrow()
                                )
                            )

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
                                                .buildArtistsThumbnailsListWithFlow(taskResult.result.items)
                                                .collect { thumbList ->
                                                    if (thumbList.isEmpty()) {
                                                        withContext(Dispatchers.Main) {
                                                            updateArtistUiState(
                                                                ArtistsUiState.Error(
                                                                    message = "Thumbnail list is Empty".toNotBlankString()
                                                                        .getOrThrow(),
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
                                                                    message = "Fetching successful. Please wait a few moment.."
                                                                        .toNotBlankString()
                                                                        .getOrThrow()
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
                                    message =
                                        throwable.message
                                            ?.toNotBlankString()
                                            ?.getOrThrow()
                                            ?: "Error occurred while getting value"
                                                .toNotBlankString()
                                                .getOrThrow(),
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

            when (val result = repository.getArtistsResource(urlPath)) {
                is Resource.Success -> {
                    val artistsModel: List<ArtistModel> = ArtistsManager.convertArtistsToModel(
                        listOfArtistDto = result.data,
                        artistThumbnails = artistThumbnails
                    )

                    updateArtistUiState(ArtistsUiState.Success(artistsModel))

                    val hasArtistsRecords: Boolean = repository.getArtistsSync().isNotEmpty()
                    if (!hasArtistsRecords) {
                        repository.insertAllArtists(artistsModel)
                    }
                }

                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        updateArtistUiState(
                            ArtistsUiState.Error(
                                message = result.message,
                                errorResponse = result.throwable
                            )
                        )
                    }
                }

                else -> return@launch
            }
        }
    }

    fun updateArtistInDatabase(artistToUpdate: ArtistModel) {
        updateArtistJob = viewModelScope.launch(Dispatchers.IO + updateArtistExceptionHandler) {
            val result = repository.updateArtist(artistToUpdate)
            Timber.d("updateArtistInDatabase() | result : $result")
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
}