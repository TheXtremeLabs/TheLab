package com.riders.thelab.feature.theaters

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TheatersDetailViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    ////////////////////////////////////////
    // Variables
    ////////////////////////////////////////
    private var tmdbItemModel: TMDBItemModel? = null

    ////////////////////////////////////////
    // Compose states
    ////////////////////////////////////////
    var title by mutableStateOf("")
        private set
    var isTrailerVisible by mutableStateOf(false)
        private set

    private val _tmdbItemUiState: MutableStateFlow<TMDBItemModel> =
        MutableStateFlow(TMDBItemModel())
    val tmdbItemUiState: StateFlow<TMDBItemModel> = _tmdbItemUiState

    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    private fun updateTMDBItemUiState(newItem: TMDBItemModel) {
        this._tmdbItemUiState.value = newItem
        Timber.e("updateTMDBItemUiState() | _tmdbItemUiState: ${_tmdbItemUiState.value.toString()}")
    }

    fun updateTitle(newTitle: String) {
        this.title = newTitle
    }

    fun updateIsTrailerVisible(isTrailerVisible: Boolean) {
        this.isTrailerVisible = isTrailerVisible
    }


    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    ////////////////////////////////////////
    //
    // CLASS METHODS
    //
    ////////////////////////////////////////
    fun getBundle(activity: Activity, intent: Intent) {
        val mBundle = intent.extras
        if (null == mBundle) {
            Timber.e("Bundle is null exit activity.")
            activity.finish()
        }

        mBundle?.let { bundle ->
            bundle.getString(TheatersDetailActivity.EXTRA_TMDB_ITEM)?.let { extraMovieJsonString ->
                Json
                    .decodeFromString<TMDBItemModel>(extraMovieJsonString)
                    .runCatching {
                        tmdbItemModel = this
                        //updateTMDBItemUiState(this)
                        getVideos()
                    }
                    .onFailure {
                        Timber.e("runCatching - onFailure() | error caught: ${it.message}")
                    }
                    .onSuccess {
                        Timber.d("runCatching - onSuccess() |  Bundle is not null. movie: ${_tmdbItemUiState.value}")
                    }
            }
        }
    }

    fun getVideos() {
        Timber.d("getVideos() | tmdbItemModel: ${tmdbItemModel.toString()}")
        tmdbItemModel?.let { tmdbItem ->
            viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                val movieVideoResponse =
                    repository.getMovieVideos(tmdbItem.id)

                movieVideoResponse?.let { movieVideo ->

                    if (movieVideo.results.isEmpty()) {

                        val tvShowVideoResponse = repository.getTvShowVideos(tmdbItem.id)

                        tvShowVideoResponse?.let { tvShowVideo ->
                            if (tvShowVideo.results.isEmpty()) {
                                Timber.e("getVideos() | No videos found.")
                            } else {

                                tmdbItemModel =
                                    tmdbItem.copy(videos = tvShowVideo.results.map { it.toModel() })
                                updateTMDBItemUiState(tmdbItemModel!!)
                            }
                        } ?: run { Timber.e("tvShowVideoResponse is null") }
                    } else {

                        tmdbItemModel =
                            tmdbItem.copy(videos = movieVideo.results.map { it.toModel() })
                        updateTMDBItemUiState(tmdbItemModel!!)
                    }

                } ?: run { Timber.e("videoResponse is null") }
            }
        } ?: run { Timber.e("tmdbItemModel is null") }
    }
}