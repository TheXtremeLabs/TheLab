package com.riders.thelab.ui.youtubelike

import android.content.Intent
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.Video
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class YoutubeLikeViewModel @Inject constructor(
    private val repositoryImpl: IRepository
) : BaseViewModel() {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    // Network State
    private lateinit var mNetworkState: StateFlow<NetworkState>
    var hasInternetConnection: Boolean by mutableStateOf(false)
        private set

    fun updateHasInternetConnection(hasInternet: Boolean) {
        this.hasInternetConnection = hasInternet
    }


    //////////////////////////////////////////
    // Live data
    //////////////////////////////////////////
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

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    ///////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }


    ///////////////////////////
    //
    // Class methods
    //
    ///////////////////////////
    fun observeNetworkState(networkManager: LabNetworkManager) {
        Timber.d("observeNetworkState()")
        mNetworkState = networkManager.networkState


        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            networkManager.getNetworkState().collect { networkState ->
                when (networkState) {
                    is NetworkState.Available -> {
                        Timber.d("network state is Available. All set.")
                        updateHasInternetConnection(true)
                    }

                    is NetworkState.Losing -> {
                        Timber.w("network state is Losing. Internet connection about to be lost")
                        updateHasInternetConnection(false)
                    }

                    is NetworkState.Lost -> {
                        Timber.e("network state is Lost. Should not allow network calls initialization")
                        updateHasInternetConnection(false)
                    }

                    is NetworkState.Unavailable -> {
                        Timber.e("network state is Unavailable. Should not allow network calls initialization")
                        updateHasInternetConnection(false)
                    }

                    is NetworkState.Undefined -> {
                        Timber.i("network state is Undefined. Do nothing")
                    }
                }
            }
        }
    }

    fun fetchVideos(activity: YoutubeLikeActivity) {

        progressVisibility.value = true

        //Test the internet's connection
        if (!hasInternetConnection) {
            Timber.e("No Internet connection")
            progressVisibility.value = false
            connectionStatus.value = false
            UIManager.showActionInToast(
                activity,
                activity.resources.getString(R.string.network_status_disconnected)
            )
        } else {
            Timber.e("Fetch Content")

            viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                try {
                    supervisorScope {
                        val videos = repositoryImpl.getVideos()

                        if (videos.isEmpty()) {
                            withContext(Dispatchers.Main) {
                                youtubeVideosFailed.value = true
                                progressVisibility.value = false
                                youtubeVideos.value = videos
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                progressVisibility.value = false
                                youtubeVideos.value = videos
                            }
                        }
                    }

                } catch (throwable: Exception) {
                    Timber.e(throwable)
                    withContext(Dispatchers.Main) {
                        progressVisibility.value = false
                        youtubeVideosFailed.value = true
                    }
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