package com.riders.thelab.ui.youtubelike

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.common.network.LabNetworkManagerNewAPI
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.Video
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class YoutubeLikeViewModel @Inject constructor(
    private val repositoryImpl: IRepository
) : ViewModel() {

    private val progressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val connectionStatus: MutableLiveData<Boolean> = MutableLiveData()
    private val youtubeVideos: MutableLiveData<List<Video>> = MutableLiveData()
    private val youtubeVideosFailed: MutableLiveData<Boolean> = MutableLiveData()


    ///////////////////////////
    //
    // Observers
    //
    ///////////////////////////
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


    ///////////////////////////
    //
    // Class methods
    //
    ///////////////////////////
    fun fetchVideos(activity: YoutubeLikeActivity) {

        progressVisibility.value = true
        //Test the internet's connection
        if (!LabNetworkManagerNewAPI.getInstance(activity).isOnline()) {
            Timber.e("No Internet connection")
            progressVisibility.value = false
            connectionStatus.value = false
            UIManager.showActionInToast(
                activity,
                activity.resources.getString(R.string.network_status_disconnected)
            )
        }

        Timber.e("Fetch Content")

        viewModelScope.launch(IO) {
            try {
                supervisorScope {
                    val videos = repositoryImpl.getVideos()

                    if (videos.isEmpty()) {
                        withContext(Main) {
                            youtubeVideosFailed.value = true
                            progressVisibility.value = false
                            youtubeVideos.value = videos
                        }
                    } else {
                        withContext(Main) {
                            progressVisibility.value = false
                            youtubeVideos.value = videos
                        }
                    }
                }

            } catch (throwable: Exception) {
                Timber.e(throwable)
                withContext(Main) {
                    progressVisibility.value = false
                    youtubeVideosFailed.value = true
                }
            }
        }
    }


    fun onYoutubeItemClicked(
        activity: YoutubeLikeActivity,
        navigator: Navigator,
        thumbShapeableImageView: ShapeableImageView,
        titleTextView: MaterialTextView,
        descriptionTextView: MaterialTextView,
        video: Video
    ) {

        Timber.e("Click on : ${video.name}")

        val intent = Intent(activity, YoutubeLikeDetailActivity::class.java)
        intent.putExtra(YoutubeLikeDetailActivity.VIDEO_OBJECT_ARG, video)

        val sePairThumb: Pair<View, String> =
            Pair.create(
                thumbShapeableImageView,
                activity.getString(R.string.thumb_transition_name)
            )
        val sePairTitle: Pair<View, String> =
            Pair.create(
                titleTextView,
                activity.getString(R.string.title_transition_name)
            )
        val sePairDescription: Pair<View, String> =
            Pair.create(
                descriptionTextView,
                activity.getString(R.string.description_transition_name)
            )
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, sePairThumb, sePairTitle, sePairDescription
        )

        // Call navigator to switch activity with or without transition according
        // to the device's version running the application
        options.toBundle()?.let {
            navigator.callYoutubeDetailActivity(
                intent,
                it
            )
        }
    }

    fun onYoutubeItemClicked(
        activity: YoutubeLikeActivity,
        navigator: Navigator,
        thumbShapeableImageView: ShapeableImageView,
        titleTextView: MaterialTextView,
        descriptionTextView: MaterialTextView,
        video: Video,
        position: Int
    ) {

        Timber.e("Click on : $position + ${video.name}")

        val intent = Intent(activity, YoutubeLikeDetailActivity::class.java)
        intent.putExtra(YoutubeLikeDetailActivity.VIDEO_OBJECT_ARG, video)

        val sePairThumb: Pair<View, String> =
            Pair.create(
                thumbShapeableImageView,
                activity.getString(R.string.thumb_transition_name)
            )
        val sePairTitle: Pair<View, String> =
            Pair.create(
                titleTextView,
                activity.getString(R.string.title_transition_name)
            )
        val sePairDescription: Pair<View, String> =
            Pair.create(
                descriptionTextView,
                activity.getString(R.string.description_transition_name)
            )
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, sePairThumb, sePairTitle, sePairDescription
        )

        // Call navigator to switch activity with or without transition according
        // to the device's version running the application
        options.toBundle()?.let {
            navigator.callYoutubeDetailActivity(
                intent,
                it
            )
        }
    }
}