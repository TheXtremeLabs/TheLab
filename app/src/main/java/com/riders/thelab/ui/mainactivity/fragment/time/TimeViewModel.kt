package com.riders.thelab.ui.mainactivity.fragment.time

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.data.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimeViewModel @Inject constructor(
        val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private val imagesFetchedDone: MutableLiveData<Boolean> = MutableLiveData()
    private val imagesFetchedFailed: MutableLiveData<Boolean> = MutableLiveData()
    private val imageUrl: MutableLiveData<String> = MutableLiveData()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mRepositoryImpl: RepositoryImpl = repositoryImpl


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

        val disposable: Disposable =
                mRepositoryImpl.getStorageReference(context)
                        .subscribe({ storageReference ->
                            // Create a child reference
                            // imagesRef now points to "images"
                            val imagesRef: StorageReference = storageReference.child("images/dark_theme")
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
                                    }
                                    .addOnCompleteListener { task1: Task<ListResult> ->
                                        Timber.d(
                                                "onComplete() - %d ",
                                                task1.result.items.size)
                                        imagesFetchedDone.value = true
                                    }
                        }, Timber::e)

        compositeDisposable.add(disposable)
    }


}