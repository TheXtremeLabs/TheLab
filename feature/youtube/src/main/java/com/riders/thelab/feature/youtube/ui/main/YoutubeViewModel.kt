package com.riders.thelab.feature.youtube.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.common.network.NetworkState
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.youtube.YoutubeUiState
import com.riders.thelab.core.data.local.model.youtube.toModel
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.kotools.types.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    labNetworkManager: LabNetworkManager,
    private val repository: IRepository,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository), CoroutineScope, DefaultLifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    private var fetchContentJob: Job? = null

    /////////////////////////////////////////////////
    // Composable states
    /////////////////////////////////////////////////
    private var _youtubeUiState: MutableStateFlow<YoutubeUiState> =
        MutableStateFlow(YoutubeUiState.Loading)
    val youtubeUiState: StateFlow<YoutubeUiState> = _youtubeUiState.asStateFlow()

    // Network State
    val hasInternetConnection: StateFlow<Boolean> = labNetworkManager.isConnectedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    private fun updateYoutubeUiState(newState: YoutubeUiState) {
        this._youtubeUiState.value = newState
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    @OptIn(ExperimentalKotoolsTypesApi::class)
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | Exception caught with message : ${throwable.message}")
            val errorMessage = "Error occurred while getting value".toNotBlankString().getOrThrow()

            updateYoutubeUiState(
                YoutubeUiState.Error(
                    message = throwable.message?.toNotBlankString()?.getOrThrow()?: errorMessage,
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
    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun fetchVideos() {
        Timber.d("fetchVideos() | Fetch Content")

        if (true == fetchContentJob?.isActive) {
            fetchContentJob?.cancel()
        }

        val errorMessage = "Error occurred while getting value".toNotBlankString().getOrThrow()

        fetchContentJob =
            viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                try {
                    supervisorScope {
                        val videos = repository.getVideos()

                        if (videos.isEmpty()) {
                            withContext(Dispatchers.Main) {
                                updateYoutubeUiState(YoutubeUiState.Error(errorMessage))
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
                        updateYoutubeUiState(YoutubeUiState.Error(errorMessage))
                    }
                }
            }
    }
}