package com.riders.thelab.feature.videocall.ui.video

import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.videocall.data.CallState
import com.riders.thelab.feature.videocall.data.VideoCallState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.result.extractCause
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject constructor(
    uiRepository: IUiRepository,
    private val streamVideo: StreamVideo
) : BaseViewModel(uiRepository) {

    private var _videoCallState: MutableStateFlow<VideoCallState> =
        MutableStateFlow(VideoCallState(call = streamVideo.call("default", "main-room")))
    val videoCallState: StateFlow<VideoCallState> = _videoCallState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("coroutineExceptionHandler | ${throwable.toString()}")
        updateVideoCallState(error = throwable)
    }

    fun updateVideoCallState(
        callState: CallState? = null,
        error: Throwable? = null
    ) {
        Timber.d("updateVideoCallState() | callState: $callState, error: $error")
        _videoCallState.update {
            it.copy(state = callState, error = error)
        }
    }

    fun onEvent(event: VideoCallUiEvent) {
        Timber.d("onEvent() | event: $event")
        when (event) {
            is VideoCallUiEvent.OnJoinClick -> {
                joinCall()
            }

            is VideoCallUiEvent.OnLeaveClick -> {
                _videoCallState.value.call.leave()
                streamVideo.logOut()
                Timber.e("onEvent() | change state to disconnected...")
                updateVideoCallState(callState = CallState.DISCONNECTED)
            }

            is VideoCallUiEvent.OnSwitchCameraClick -> {

            }
        }
    }

    fun joinCall() {
        if (CallState.CONNECTED == _videoCallState.value.state) {
            Timber.w("joinCall() | Already connected")
            return
        }

        if (CallState.CONNECTING == _videoCallState.value.state) {
            Timber.w("joinCall() | Already connecting")
            return
        }

        Timber.d("joinCall()")

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            updateVideoCallState(callState = CallState.CONNECTING)

            val shouldCreate = streamVideo
                .queryCalls(filters = emptyMap())
                .getOrNull()
                ?.also { Timber.d("joinCall() | queryCalls: $it") }
                ?.calls
                ?.also { Timber.d("joinCall() | calls: $it") }
                ?.isEmpty() == true

            Timber.d("joinCall() | shouldCreate: $shouldCreate")

            _videoCallState.value.call
                .join(create = shouldCreate)
                .onSuccess { session ->
                    Timber.d("joinCall() | session: $session")
                    updateVideoCallState(callState = CallState.CONNECTED, error = null)
                }
                .onError { error ->
                    Timber.e("joinCall() | ${error.extractCause()}")
                    updateVideoCallState(callState = null, error = error.extractCause())
                }
                .getOrNull()
                ?.connect()
        }
    }

}