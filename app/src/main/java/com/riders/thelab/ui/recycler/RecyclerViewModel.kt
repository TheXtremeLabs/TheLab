package com.riders.thelab.ui.recycler

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.remote.dto.artist.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.parceler.Parcels
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecyclerViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private var JSONURLFetched: MutableLiveData<String> = MutableLiveData()
    private var JSONURLError: MutableLiveData<Boolean> = MutableLiveData()
    private var artistsThumbnails: MutableLiveData<List<String>> = MutableLiveData()
    private var artistsThumbnailsError: MutableLiveData<Boolean> = MutableLiveData()
    private var artists: MutableLiveData<List<Artist>> = MutableLiveData()
    private var artistsError: MutableLiveData<Boolean> = MutableLiveData()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getJSONURLFetched(): LiveData<String> {
        return JSONURLFetched
    }

    fun getJSONURLError(): LiveData<Boolean> {
        return JSONURLError
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
        val disposable: Disposable =
            repositoryImpl.getStorageReference(activity)
                .subscribe(
                    {
                        // Create a child reference
                        // imagesRef now points to "images"
                        val artistsRef: StorageReference = it.child("bulk/artists.json")

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
                                JSONURLFetched.value = url
                            }
                    },
                    {
                        Timber.e(it)
                        JSONURLError.value = true
                    }
                )

        compositeDisposable.add(disposable)
    }

    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    fun getFirebaseFiles(activity: Activity) {

        Timber.d("getFirebaseFiles()")

        val disposable: Disposable =
            repositoryImpl
                .getStorageReference(activity)
                .subscribe(
                    { storageReference ->
                        Timber.d("signInAnonymously:success")

                        // Create a child reference
                        // imagesRef now points to "images"
                        val imagesRef: StorageReference = storageReference.child("images/artists")
                        imagesRef
                            .listAll()
                            .addOnSuccessListener { listResult: ListResult? -> Timber.d("onSuccess()") }
                            .addOnFailureListener { t: java.lang.Exception? -> Timber.e(t) }
                            .addOnCompleteListener { taskResult: Task<ListResult> ->
                                if (!taskResult.isSuccessful) {
                                    Timber.e("error occurred. Please check logs.")
                                } else {
                                    Timber.d(
                                        "onComplete() - with size of : %d element(s)",
                                        taskResult.result.items.size
                                    )
                                    buildArtistsThumbnailsList(taskResult.result.items)
                                        .subscribe(
                                            { links: List<String> ->
                                                Timber.d("Links : %s", links.toString())
                                                if (taskResult.result.items.size == links.size) {
                                                    artistsThumbnails.value = links
                                                }
                                            },
                                            { throwable: Throwable? ->
                                                Timber.e(throwable)
//                                                getView().hideLoader()
                                                artistsThumbnailsError.value = true
                                            })
                                }
                            }
                    },
                    Timber::e
                )

        compositeDisposable.add(disposable)
    }

    private fun buildArtistsThumbnailsList(storageReferences: List<StorageReference>): Single<List<String>> {
        return object : Single<List<String>>() {
            @SuppressLint("NewApi")
            override fun subscribeActual(observer: SingleObserver<in List<String>>) {
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
                                observer.onError(throwable!!)
                            }
                            .addOnCompleteListener { taskResult: Task<Uri?>? ->
                                observer.onSuccess(
                                    thumbnailsLinks
                                )
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
                                    observer.onError(throwable!!)
                                }
                                .addOnCompleteListener { task1: Task<Uri?>? ->
                                    observer.onSuccess(
                                        thumbnailsLinks
                                    )
                                }
                        }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchArtists(urlPath: String) {
        val disposable: Disposable =
            repositoryImpl
                .getArtists(urlPath)
                .subscribe(
                    {
                        artists.value = it
                    }, { throwable ->
                        Timber.e(throwable)
                        artistsError.value = true
                    })
        compositeDisposable.add(disposable)
    }


    fun onDetailClick(
        activity: Activity,
        item: Artist,
        sharedImageView: ShapeableImageView,
        position: Int
    ) {
        Timber.d("onDetailClick(item, sharedImageView, position)")

        val intent = Intent(activity, RecyclerViewDetailActivity::class.java)

        intent.putExtra(RecyclerViewDetailActivity.EXTRA_RECYCLER_ITEM, Parcels.wrap(item))

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

    fun onDeleteClick(
        activity: Activity,
        item: Artist,
        adapter: RecyclerViewAdapter,
        position: Int
    ) {
        Timber.d("onDeleteClick() item %s at position : %s", item.artistName, position)

        // get the removed item name to display it in snack bar
        val name: String = item.artistName

        // backup of removed item for undo purpose

        // remove the item from recycler view
        adapter.removeItem(position)

        // showing snack bar with Undo option
        UIManager.showActionInSnackBar(
            activity, activity.findViewById(android.R.id.content),
            "$name removed from cart!", SnackBarType.WARNING,
            "UNDO"
        ) { view: View? ->
            // undo is selected, restore the deleted item
            adapter.restoreItem(item, position)
        }
    }
}