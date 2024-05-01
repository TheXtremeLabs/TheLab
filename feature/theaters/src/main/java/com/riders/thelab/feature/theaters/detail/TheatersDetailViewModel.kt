package com.riders.thelab.feature.theaters.detail

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBDetailUiState
import com.riders.thelab.core.data.local.model.tmdb.TDMBCastModel
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.toCastModel
import com.riders.thelab.core.data.local.model.tmdb.toVideoModel
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

@Suppress("EmptyMethod")
@HiltViewModel
class TheatersDetailViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    ////////////////////////////////////////
    // Variables
    ////////////////////////////////////////
    private var mTMDBItemModel: TMDBItemModel? by mutableStateOf(null)
        private set

    ////////////////////////////////////////
    // Compose states
    ////////////////////////////////////////
    var title by mutableStateOf("")
        private set
    var isTrailerVisible by mutableStateOf(false)
        private set

    private val _tmdbItemUiState: MutableStateFlow<TMDBDetailUiState> =
        MutableStateFlow(TMDBDetailUiState.Loading)
    val tmdbItemUiState: StateFlow<TMDBDetailUiState> = _tmdbItemUiState

    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    private fun updateTMDBItemUiState(newState: TMDBDetailUiState) {
        this._tmdbItemUiState.value = newState
        Timber.e("updateTMDBItemUiState() | TMDBModel Item value: ${_tmdbItemUiState.value}")
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
                        mTMDBItemModel = this
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

        if (null == mTMDBItemModel) {
            Timber.e("getVideos() | tmdbItemModel is null")
            return
        }

        Timber.d("getVideos() | tmdbItemModel: ${mTMDBItemModel.toString()}")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val movieVideoResponse = repository.getMovieVideos(mTMDBItemModel!!.id)

            movieVideoResponse?.let { movieVideo ->

                if (movieVideo.results.isEmpty()) {
                    val tvShowVideoResponse = repository.getTvShowVideos(mTMDBItemModel!!.id)

                    tvShowVideoResponse?.let { tvShowVideo ->
                        if (tvShowVideo.results.isEmpty()) {
                            Timber.e("getVideos() | No videos found.")
                        } else {

                            mTMDBItemModel!!.videos = tvShowVideo.results.map { it.toVideoModel() }
                            // updateTMDBItemUiState(TMDBDetailUiState.Success(mTMDBItemModel!!))
                        }
                    } ?: run { Timber.e("tvShowVideoResponse is null") }
                } else {

                    mTMDBItemModel!!.videos = movieVideo.results.map { it.toVideoModel() }
                    // updateTMDBItemUiState(TMDBDetailUiState.Success(mTMDBItemModel!!))
                }

                getCast()

            } ?: run { Timber.e("videoResponse is null") }
        }
    }

    fun getCast() {
        if (null == mTMDBItemModel) {
            Timber.e("getCast() | tmdbItemModel is null")
            return
        }

        Timber.d("getCast() | tmdbItemModel: ${mTMDBItemModel.toString()}")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val castResponse = repository.getMovieCredits(mTMDBItemModel!!.id)

            castResponse?.let { response ->
                val castList: MutableList<TDMBCastModel> = mutableListOf()
                response.cast.forEach {
                    if (it.knownForDepartment.contains("acting", true)) {
                        castList.add(it.toCastModel())
                    }
                }
                mTMDBItemModel!!.cast = castList

                val crewList: MutableList<TDMBCastModel> = mutableListOf()
                response.crew.forEach {
                    if (it.department.contains("production", true)) {
                        crewList.add(it.toCastModel())
                    }
                }

                mTMDBItemModel!!.directors = crewList
                updateTMDBItemUiState(TMDBDetailUiState.Success(mTMDBItemModel!!))
            } ?: run { Timber.e("castResponse is null") }
        }
    }
}