package com.riders.thelab.ui.mainactivity.fragment.time


import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.data.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimeViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private var progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val imagesFetchedDone: MutableLiveData<Boolean> = MutableLiveData()
    private val imagesFetchedFailed: MutableLiveData<Boolean> = MutableLiveData()
    private val imageUrl: MutableLiveData<String> = MutableLiveData()


    fun getProgressVisibility(): LiveData<Boolean> {
        return progressVisibility
    }

    fun getImagesFetchedDone(): LiveData<Boolean> {
        return imagesFetchedDone
    }

    fun getImagesFetchedFailed(): LiveData<Boolean> {
        return imagesFetchedFailed
    }

    fun getImageUrl(): LiveData<String> {
        return imageUrl
    }


    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    fun getWallpaperImages(context: FragmentActivity) {
        Timber.d("getFirebaseFiles()")
        progressVisibility.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val storageReference: StorageReference? = repository.getStorageReference(context)

                storageReference?.let {

                    // Create a child reference
                    // imagesRef now points to "images"
                    val imagesRef: StorageReference = it.child("images/dark_theme")
                    imagesRef.list(5)
                        .addOnSuccessListener { listResult: ListResult ->
                            Timber.d("onSuccess()")
                            val max = listResult.items.size

                            // Get random int
                            val iRandom = Random().nextInt(max)

                            // Get item url using random int
                            val item = listResult.items[iRandom]

                            // Make rest call
                            item
                                .downloadUrl
                                .addOnSuccessListener { uri: Uri ->
                                    imageUrl.value = uri.toString()
                                }
                        }
                        .addOnFailureListener { t: Exception? ->
                            Timber.e(t)
                            imagesFetchedFailed.value = true
                            progressVisibility.value = false
                        }
                        .addOnCompleteListener { task1: Task<ListResult> ->
                            Timber.d("onComplete() - ${task1.result.items.size}")
                            imagesFetchedDone.value = true
                            progressVisibility.value = false
                        }
                }

            } catch (throwable: Exception) {
                Timber.e(throwable)
            }
        }
    }

}