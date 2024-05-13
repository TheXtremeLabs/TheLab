package com.riders.thelab.feature.youtube.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.YoutubeUiState
import com.riders.thelab.core.data.local.model.youtube.toModel
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    var fetchContentJob: Job? = null

    /////////////////////////////////////////////////
    // Composable states
    /////////////////////////////////////////////////
    private var _youtubeUiState: MutableStateFlow<YoutubeUiState> =
        MutableStateFlow(YoutubeUiState.Loading)
    val youtubeUiState: StateFlow<YoutubeUiState> = _youtubeUiState.asStateFlow()

    var hasInternetConnection: Boolean by mutableStateOf(false)


    private fun updateYoutubeUiState(newState: YoutubeUiState) {
        this._youtubeUiState.value = newState
    }

    private fun updateHasInternetConnection(hasInternet: Boolean) {
        this.hasInternetConnection = hasInternet
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    @OptIn(ExperimentalKotoolsTypesApi::class)
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)

            updateYoutubeUiState(
                YoutubeUiState.Error(
                    message = NotBlankString.create(throwable.message),
                    throwable = throwable
                )
            )
        }

    ///////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////
    override fun onCleared() {
        Timber.e("onCleared()")

        if (true == fetchContentJob?.isActive) {
            fetchContentJob?.cancel()
        }

        fetchContentJob = null
        super.onCleared()
    }

    /////////////////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////////////////
    fun observeNetworkState(networkManager: LabNetworkManager) {
        Timber.d("observeNetworkState()")

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            networkManager.getNetworkState().collect { networkState ->
                when (networkState) {
                    is NetworkState.Available -> {
                        Timber.d("network state is Available. All set.")
                        updateHasInternetConnection(true)

                        fetchVideos()
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

    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun fetchVideos() {
        Timber.d("fetchVideos() | Fetch Content")

        if (true == fetchContentJob?.isActive) {
            fetchContentJob?.cancel()
        }

        fetchContentJob =
            viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                try {
                    supervisorScope {
                        val videos = repository.getVideos()

                        if (videos.isEmpty()) {
                            withContext(Dispatchers.Main) {
                                updateYoutubeUiState(YoutubeUiState.Error(NotBlankString.create("Error occurred while getting value")))
                            }
                        } else {
                            val videosModel = videos.map { it.toModel() }
                            withContext(Dispatchers.Main) {
                                updateYoutubeUiState(YoutubeUiState.Success(videosModel))
                            }
                        }
                    }

                } catch (throwable: Exception) {
                    Timber.e(throwable)
                    withContext(Dispatchers.Main) {
                        updateYoutubeUiState(YoutubeUiState.Error(NotBlankString.create("Error occurred while getting value")))
                    }
                }
            }

    }
}