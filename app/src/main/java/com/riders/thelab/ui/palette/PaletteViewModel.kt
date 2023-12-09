package com.riders.thelab.ui.palette

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.data.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PaletteViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    val paletteNameList = listOf(
        "Vibrant",
        "Vibrant Dark",
        "Vibrant Light",
        "Muted",
        "Muted Dark",
        "Light Muted"
    )

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }

    /////////////////////////////
    // Compose
    /////////////////////////////
    private val _imageUrl: MutableStateFlow<String> = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    private val progressVisibility = mutableStateOf(false)

    ////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////
    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    fun getWallpaperImages(context: Activity) {
        Timber.d("getFirebaseFiles()")
        progressVisibility.value = true

        viewModelScope.launch(IO + SupervisorJob() + coroutineExceptionHandler) {
            try {
                val storageReference: StorageReference? = repository.getStorageReference(context)

                storageReference?.let {
                    withContext(Main) {
                        // Create a child reference
                        // imagesRef now points to "images"
                        val imagesRef: StorageReference = it.child("images/dark_theme")
                        imagesRef.list(5)
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
                                        _imageUrl.value = uri.toString()
                                    }
                            }
                            .addOnFailureListener { t: Exception? ->
                                Timber.e(t)
                                // imagesFetchedFailed.value = true
                                progressVisibility.value = false
                            }
                            .addOnCompleteListener { task1: Task<ListResult> ->
                                Timber.d("onComplete() - ${task1.result.items.size}")
                                // imagesFetchedDone.value = true
                                progressVisibility.value = false
                            }
                    }
                }
            } catch (throwable: Exception) {
                Timber.e(throwable)
            }
        }
    }
}