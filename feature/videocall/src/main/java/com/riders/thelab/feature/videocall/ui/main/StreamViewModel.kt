package com.riders.thelab.feature.videocall.ui.main

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.application
import com.riders.thelab.core.ui.compose.base.BaseAndroidViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.videocall.data.ConnectState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StreamViewModel @Inject constructor(
    application: Application,
    uiRepository: IUiRepository
) : BaseAndroidViewModel(application = application, uiRepository = uiRepository),
    DefaultLifecycleObserver {
    var streamClient: StreamVideo? = null

    private var _connectState: MutableStateFlow<ConnectState> = MutableStateFlow(ConnectState())
    val connectState: StateFlow<ConnectState> = _connectState


    fun updateConnectState(newState: ConnectState) {
        Timber.d("updateConnectState() | new connect state: $newState")
        _connectState.update { newState }
    }

    fun updateConnectState(
        name: String? = null,
        errorMessage: String? = null
    ) {
        Timber.d("updateConnectState() | name: $name, errorMessage: $errorMessage")
        _connectState.update {
            it.copy(name = name ?: it.name, errorMessage = errorMessage)
        }
    }


    fun initStreamVideoClient(newUserName: String) {
        Timber.d("initStreamVideoClient() | name: $newUserName")
        updateConnectState(errorMessage = null)

        if (_connectState.value.name.isBlank()) {
            updateConnectState(errorMessage = "The username can't be blank.")
            return
        }

        if (null == streamClient || _connectState.value.name != newUserName) {
            StreamVideo.removeClient()

            streamClient = StreamVideoBuilder(
                context = application.applicationContext,
                apiKey = "djmy2f7dpjk8",
                user = User(
                    id = newUserName,
                    name = newUserName,
                    type = UserType.Guest
                )
            )
                .build()
        }

        updateConnectState(
            ConnectState(
                name = newUserName,
                isConnected = true,
                errorMessage = null
            )
        )
    }

    fun onEvent(event: UiEvent) {
        Timber.d("onEvent() | event: $event")

        when (event) {
            is UiEvent.OnNameChanged -> updateConnectState(name = event.name)

            is UiEvent.OnConnectClicked -> {
                initStreamVideoClient(_connectState.value.name)
            }

            is UiEvent.OnDisconnectClicked -> {}
        }
    }
}