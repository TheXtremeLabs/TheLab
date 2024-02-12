package com.riders.thelab.feature.palette

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.PaletteUiState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PaletteViewModel @Inject constructor(private val repository: IRepository) : BaseViewModel() {

    val paletteNameList = listOf(
        "Vibrant",
        "Vibrant Dark",
        "Vibrant Light",
        "Muted",
        "Muted Dark",
        "Light Muted"
    )


    /////////////////////////////
    // Compose
    /////////////////////////////
    private val _paletteUiState: MutableStateFlow<PaletteUiState> =
        MutableStateFlow(PaletteUiState.Loading)
    val paletteUiState: StateFlow<PaletteUiState> = _paletteUiState

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
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)

            updateIsRefreshing(false)
            throwable.message?.let { updateUIState(PaletteUiState.Error(it)) }
        }

    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                Timber.d("init | isNightMode() | dark mode value: $it")
                updateDarkMode(it)
            }
        }
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

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {

            val storageReference: StorageReference? = repository.getStorageReference(context)

            storageReference?.let {
                withContext(Dispatchers.Main) {
                    // Create a child reference
                    // imagesRef now points to "images"
                    val imagesRef: StorageReference = it.child("images/dark_theme")
                    imagesRef.list(5)
                        .addOnFailureListener { t: Exception? ->
                            Timber.e(t)
                            // imagesFetchedFailed.value = true
                            updateIsRefreshing(false)
                            updateUIState(PaletteUiState.Error(t?.message.toString()))
                        }
                        .addOnSuccessListener { listResult: ListResult ->
                            Timber.d("onSuccess()")
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
                                    updateIsRefreshing(false)
                                    updateUIState(PaletteUiState.Success(uri.toString()))
                                }
                        }
                        .addOnCompleteListener { task1 ->
                            Timber.d("onComplete() - ${task1.result.items.size}")
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