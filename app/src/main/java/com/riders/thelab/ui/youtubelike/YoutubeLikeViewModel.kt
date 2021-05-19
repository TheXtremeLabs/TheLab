package com.riders.thelab.ui.youtubelike

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class YoutubeLikeViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl,
    var navigator: Navigator
) : ViewModel() {

    private var progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val youtubeVideos: MutableLiveData<List<Video>> = MutableLiveData()
    private val youtubeVideosFailed: MutableLiveData<Boolean> = MutableLiveData()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getProgressBarVisibility(): LiveData<Boolean> {
        return progressVisibility
    }

    fun getConnectionStatus(): LiveData<Boolean> {
        return connectionStatus
    }

    fun getYoutubeVideos(): LiveData<List<Video>> {
        return youtubeVideos
    }

    fun getYoutubeVideosFailed(): LiveData<Boolean> {
        return youtubeVideosFailed
    }

    fun fetchVideos(activity: YoutubeLikeActivity) {

        progressVisibility.value = true
        //Test the internet's connection
        if (!LabNetworkManagerNewAPI.isConnected) {
            Timber.e("No Internet connection")
            progressVisibility.value = false
            connectionStatus.value = false
            UIManager.showActionInToast(
                activity,
                activity.resources.getString(R.string.network_status_disconnected)
            )
        }


        Timber.e("Fetch Content")
        val disposable: Disposable =
            repositoryImpl.getVideos()
                .subscribe(
                    { videos ->
                        if (videos.isEmpty()) {
                            youtubeVideosFailed.value = true
                            progressVisibility.value = false
                            youtubeVideos.value = videos
                        } else {
                            progressVisibility.value = false
                            youtubeVideos.value = videos
                        }
                    },
                    { throwable ->
                        Timber.e(throwable)
                        progressVisibility.value = false
                        youtubeVideosFailed.value = true
                    })

        compositeDisposable.add(disposable)
    }


    fun onYoutubeItemClicked(
        activity: YoutubeLikeActivity,
        thumbShapeableImageView: ShapeableImageView,
        titleTextView: MaterialTextView,
        descriptionTextView: MaterialTextView,
        video: Video,
        position: Int
    ) {

        Timber.e("Click on : $position + ${video.videoName}")


        val intent = Intent(activity, YoutubeLikeDetailActivity::class.java)
        intent.putExtra(YoutubeLikeDetailActivity.VIDEO_OBJECT_ARG, video)

        val sePairThumb: androidx.core.util.Pair<View, String> =
            androidx.core.util.Pair.create(
                thumbShapeableImageView,
                activity.getString(R.string.thumb_transition_name)
            )
        val sePairTitle: androidx.core.util.Pair<View, String> =
            androidx.core.util.Pair.create(
                titleTextView,
                activity.getString(R.string.title_transition_name)
            )
        val sePairDescription: androidx.core.util.Pair<View, String> =
            androidx.core.util.Pair.create(
                descriptionTextView,
                activity.getString(R.string.description_transition_name)
            )
        var options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, sePairThumb, sePairTitle, sePairDescription
        )

        // Call navigator to switch activity with or without transition according
        // to the device's version running the application
        navigator.callYoutubeDetailActivity(
            intent,
            options.toBundle()
        )
    }
}