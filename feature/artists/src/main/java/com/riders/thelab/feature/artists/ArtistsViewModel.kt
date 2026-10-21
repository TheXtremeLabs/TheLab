package com.riders.thelab.feature.artists

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.domain.model.artist.Artist
import com.riders.thelab.core.domain.usecase.artists.FetchArtistsThumbnailsUseCase
import com.riders.thelab.core.domain.usecase.artists.FetchArtistsUseCase
import com.riders.thelab.core.domain.usecase.artists.GetArtistsUseCase
import com.riders.thelab.core.domain.usecase.artists.SaveArtistsUseCase
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.base.updateState
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.artists.core.utils.ArtistsManager
import com.riders.thelab.feature.artists.data.local.compose.ArtistUiState
import com.riders.thelab.feature.artists.data.local.model.ArtistUi
import com.riders.thelab.feature.artists.data.mapper.toArtistUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalKotoolsTypesApi::class)
@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val fetchArtistsThumbnailsUseCase: FetchArtistsThumbnailsUseCase,
    private val fetchArtistsUseCase: FetchArtistsUseCase,
    private val saveArtistsUseCase: SaveArtistsUseCase,
    private val getArtistsUseCase: GetArtistsUseCase,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository), DefaultLifecycleObserver {


    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var mBucketUrl: String? = null
    private var mArtistThumbnails = mutableListOf<String>()

    private var mStorageReference: StorageReference? = null

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    val artistUiState: StateFlow<ArtistUiState>
        field = MutableStateFlow<ArtistUiState>(
            ArtistUiState.Loading("Loading...".toNotBlankString().getOrThrow())
        )

    private fun updateArtistUiState(newState: ArtistUiState) {
        this.artistUiState.update { newState }
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

        viewModelScope.launch(Dispatchers.IO) {
            when (val hasArtistsResult = getArtistsUseCase.invoke(null)) {
                is Resource.Error -> {
                    artistUiState.updateState {
                        ArtistUiState.Error(
                            message = hasArtistsResult.message.toNotBlankString().getOrThrow(),
                            errorResponse = hasArtistsResult.throwable
                        )
                    }
                }

                is Resource.Success -> {
                    if (hasArtistsResult.data.isNullOrEmpty()) {
                        Timber.w("onCreate() | Artists list is empty")
                        fetchArtistsThumbnails()
                    } else {
                        Timber.d("onCreate() | Artists list is not empty")
                    }
                }
            }
        }

        /*viewModelScope.launch(Dispatchers.IO) {

            when(val hasArtistsRecordsResult: com.riders.thelab.core.common.utils.Resource<Boolean> = fetchArtistsUseCase.invoke(params = null)){
                is Resource.Success<*> -> {
                    if(false == hasArtistsRecordsResult.data){
                        getFirebaseJSONURL()
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            updateArtistUiState(
                                ArtistUiState.Loading(
                                    "Artists records found....".toNotBlankString().getOrThrow()
                                )
                            )
                            delay(2.toDuration(DurationUnit.SECONDS))

                            updateArtistUiState(
                                ArtistUiState.Loading(
                                    "Loading. Please wait....".toNotBlankString().getOrThrow()
                                )
                            )
                            delay(2.toDuration(DurationUnit.SECONDS))

                            repository.getArtists().collect { artistModels ->
                                withContext(Dispatchers.Main) {
                                    updateArtistUiState(ArtistUiState.Success(artistModels))
                                }
                            }
                        }
                    }
                }
                is Resource.Error<*> -> {}
                else -> {}
            }
        }*/
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

    fun fetchArtists() {
        viewModelScope.launch(Dispatchers.IO + fetchJsonExceptionHandler) {
            when (val fetchedResult = fetchArtistsUseCase.invoke(null)) {
                is Resource.Error -> {
                    Timber.e("fetchArtists() | Error caught with message : ${fetchedResult.message} (class : ${fetchedResult::class.java.canonicalName})")
                    artistUiState.updateState {
                        ArtistUiState.Error(
                            message = fetchedResult.message.toNotBlankString().getOrThrow(),
                            errorResponse = fetchedResult.throwable
                        )
                    }
                }

                is Resource.Success -> {
                    Timber.d("fetchArtists() | fetchedResult : ${fetchedResult.data}")

                    fetchedResult.data?.let {
                        val artistsModel: List<Artist> = ArtistsManager.convertArtistsToModel(
                            listOfArtistDto = it,
                            artistThumbnails = mArtistThumbnails
                        )

                        val artistsUi = artistsModel.map { it.toArtistUi() }
                        updateArtistUiState(ArtistUiState.Success(artistsUi))
                    }
                }
            }
        }
    }

    fun fetchArtistsThumbnails() {
        viewModelScope.launch(Dispatchers.IO + fetchJsonExceptionHandler) {
            when (val thumbsResult = fetchArtistsThumbnailsUseCase.invoke("images/artists")) {
                is Resource.Error -> {
                    Timber.e("fetchArtistsThumbnails() | Error caught with message : ${thumbsResult.message} (class : ${thumbsResult::class.java.canonicalName})")
                    artistUiState.updateState {
                        ArtistUiState.Error(
                            message = thumbsResult.message.toNotBlankString().getOrThrow(),
                            errorResponse = thumbsResult.throwable
                        )
                    }
                }

                is Resource.Success -> {
                    Timber.d("fetchArtistsThumbnails() | thumbsResult : ${thumbsResult.data}")
                    mArtistThumbnails.addAll(thumbsResult.data)
                    fetchArtists(/*thumbsResult.data*/)
                }
            }
        }
    }

    fun getArtists(url: String) {
        viewModelScope.launch(Dispatchers.IO + fetchJsonExceptionHandler) {
            when (val fetchedResult = getArtistsUseCase.invoke(null)) {
                is Resource.Error -> {
                    Timber.e("fetchArtists() | Error caught with message : ${fetchedResult.message} (class : ${fetchedResult::class.java.canonicalName})")
                    artistUiState.updateState {
                        ArtistUiState.Error(
                            message = fetchedResult.message.toNotBlankString().getOrThrow(),
                            errorResponse = fetchedResult.throwable
                        )
                    }
                }

                is Resource.Success -> {
                    Timber.d("fetchArtists() | fetchedResult : ${fetchedResult.data}")
                }
            }
        }
    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    private fun getFirebaseJSONURL() {
        if (null == mWeakReference?.get()) {
            Timber.e("getFirebaseJSONURL() | Unable to get activity")
            return
        }

        fetchJsonJob = viewModelScope.launch(fetchJsonExceptionHandler) {
            try {
                if (null == mStorageReference) {
                    updateArtistUiState(
                        ArtistUiState.Loading(
                            message = "Authenticating to the server...".toNotBlankString()
                                .getOrThrow()
                        )
                    )
                }

                //val mStorageReferenceResource = repository.getStorageReferenceAsResource()
                /* when (mStorageReferenceResource) {
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
                                 ArtistUiState.Error(
                                     message = mStorageReferenceResource.message,
                                     errorResponse = mStorageReferenceResource.throwable
                                 )
                             )
                         }
                     }

                     else -> Timber.e("getFirebaseJSONURL() | Unhandled type")
                 }*/

            } catch (throwable: Exception) {
                Timber.e(throwable)
                withContext(Dispatchers.Main) {
                    updateArtistUiState(
                        ArtistUiState.Error(
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
            /*fun getFirebaseFiles() {

                (mWeakReference?.get() as? ArtistsActivity)?.let { activity ->
                    Timber.d("getFirebaseFiles()")

                    fetchArtistsThumbJob =
                        viewModelScope.launch(coroutineExceptionHandler) {

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
                                        ArtistUiState.Loading(
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
                                                                        ArtistUiState.Error(
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
                                                                        ArtistUiState.Loading(
                                                                            message = "Fetching successful. Please wait a few moment.."
                                                                                .toNotBlankString()
                                                                                .getOrThrow()
                                                                        )
                                                                    )

                                                                    withContext(Dispatchers.Main) {
                                                                        //artistThumbnails.addAll(thumbList)
                                                                        thumbList.also {
                                                                            //bucketUrl?.let { fetchArtists(it) }
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
                                        ArtistUiState.Error(
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
            }*/


    fun fetchArtists(urlPath: String) {
        Timber.d("fetchArtists() | url: $urlPath")

        fetchArtistsJob = viewModelScope.launch(coroutineExceptionHandler) {

            /* when (val result = repository.getArtistsResource(urlPath)) {
                 is Resource.Success -> {
                     val artistsModel: List<ArtistModel> = ArtistsManager.convertArtistsToModel(
                         listOfArtistDto = result.data,
                         artistThumbnails = artistThumbnails
                     )

                     updateArtistUiState(ArtistUiState.Success(artistsModel))

                     val hasArtistsRecords: Boolean = repository.getArtistsSync().isNotEmpty()
                     if (!hasArtistsRecords) {
                         repository.insertAllArtists(artistsModel)
                     }
                 }

                 is Resource.Error -> {
                     withContext(Dispatchers.Main) {
                         updateArtistUiState(
                             ArtistUiState.Error(
                                 message = result.message,
                                 errorResponse = result.throwable
                             )
                         )
                     }
                 }

                 else -> return@launch
             }*/
        }
    }

    fun saveArtists(artists: List<Artist>) {
        updateArtistJob = viewModelScope.launch(Dispatchers.IO + updateArtistExceptionHandler) {
            when (val saved = saveArtistsUseCase.invoke(artists)) {
                is Resource.Error -> {
                    artistUiState.updateState {
                        ArtistUiState.Error(
                            message = saved.message.toNotBlankString().getOrThrow(),
                            errorResponse = saved.throwable
                        )
                    }
                }

                is Resource.Success -> {}
            }
        }
    }

    fun updateArtistInDatabase(artistToUpdate: ArtistUi) {
        updateArtistJob = viewModelScope.launch(Dispatchers.IO + updateArtistExceptionHandler) {
            //val result = repository.updateArtist(artistToUpdate)
            //Timber.d("updateArtistInDatabase() | result : $result")
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