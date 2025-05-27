package com.riders.thelab.feature.palette

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.palette.PaletteUiState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

@HiltViewModel
class PaletteViewModel @Inject constructor(
    labNetworkManager: LabNetworkManager,
    private val repository: IRepository,
    val uiRepository: IUiRepository
) : BaseViewModel(), CoroutineScope, DefaultLifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    val paletteNameList = listOf(
        "Vibrant",
        "Vibrant Dark",
        "Vibrant Light",
        "Muted",
        "Muted Dark",
        "Light Muted"
    )


    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private val _paletteUiState: MutableStateFlow<PaletteUiState> =
        MutableStateFlow(PaletteUiState.Loading)
    val paletteUiState: StateFlow<PaletteUiState> = _paletteUiState


    // Network State
    var hasInternetConnection: StateFlow<Boolean> = labNetworkManager.isConnectedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    var isRefreshing: Boolean by mutableStateOf(false)
        private set

    private fun updateUIState(newPaletteUiState: PaletteUiState) {
        this._paletteUiState.value = newPaletteUiState
    }

    fun updateIsRefreshing(refreshing: Boolean) {
        this.isRefreshing = refreshing
    }


    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private var getWallpapersJob: Job? = null

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)

            updateIsRefreshing(false)
            throwable.message?.let { updateUIState(PaletteUiState.Error(it)) }
        }

    ////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////
    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                Timber.d("init | isNightMode() | dark mode value: $it")
                updateDarkMode(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")

        if (true == getWallpapersJob?.isActive) {
            getWallpapersJob?.cancel()
        }
        getWallpapersJob = null
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onStop()")
    }


    ////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////
    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    fun getWallpaperImages(context: Activity) {
        Timber.d("getWallpaperImages()")

        getWallpapersJob =
            viewModelScope.launch(coroutineContext + SupervisorJob() + coroutineExceptionHandler) {

                val storageReference: StorageReference? = repository.getStorageReference(context)

                storageReference?.let {
                    withContext(Dispatchers.Main) {
                        // Create a child reference
                        // imagesRef now points to "images"
                        val imagesRef: StorageReference = it.child("images/dark_theme")
                        imagesRef
                            .list(5)
                            .addOnFailureListener { exception: Exception ->
                                Timber.d("getWallpaperImages() | addOnFailureListener | Error while getting images with message : ${exception.message} (class: ${exception.javaClass?.canonicalName})")
                                // imagesFetchedFailed.value = true
                                updateIsRefreshing(false)
                                updateUIState(PaletteUiState.Error(exception.message.toString()))
                            }
                            .addOnSuccessListener { listResult: ListResult ->
                                Timber.d("getWallpaperImages() | addOnSuccessListener | count : ${listResult.items.size}")
                                val max = listResult.items.size

                                // Get random int
                                val iRandom = Random.nextInt(max)

                                // Get item url using random int
                                val item: StorageReference = listResult.items[iRandom]

                                // Make rest call
                                item
                                    .downloadUrl
                                    .addOnSuccessListener { uri: Uri ->
                                        Timber.d("downloadUrl | addOnSuccessListener | uri: $uri")
                                        updateUIState(PaletteUiState.Success(uri.toString()))
                                    }
                            }
                            .addOnCompleteListener { task1 ->
                                Timber.d("getWallpaperImages() | addOnCompleteListener | ${task1.result.items.size}")
                                updateIsRefreshing(false)
                            }
                    }
                } ?: run {
                    val errorMessage = "Unable to get wallpaper. Authentication error."
                    Timber.e("getWallpaperImages() | $errorMessage")
                    updateIsRefreshing(false)
                    updateUIState(PaletteUiState.Error(errorMessage))
                }
            }
    }
}