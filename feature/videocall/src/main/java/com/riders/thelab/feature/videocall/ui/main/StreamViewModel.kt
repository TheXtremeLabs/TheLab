package com.riders.thelab.feature.videocall.ui.main

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.application
import com.riders.thelab.core.ui.compose.base.BaseAndroidViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.videocall.data.ConnectState
import com.riders.thelab.feature.videocall.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.log.Priority
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.logging.LoggingLevel
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

    val connectState: StateFlow<ConnectState>
        // Since Kotlin 2.3.20 : Introducing backing properties
        field = MutableStateFlow<ConnectState>(ConnectState())


    override fun onCleared() {
        super.onCleared()

        StreamVideo.removeClient()
    }

    fun updateConnectState(newState: ConnectState) {
        Timber.d("updateConnectState() | new connect state: $newState")
        connectState.update { newState }
    }

    fun updateConnectState(
        name: String? = null,
        isConnected: Boolean? = null,
        errorMessage: String? = null
    ) {
        Timber.d("updateConnectState() | name: $name, errorMessage: $errorMessage")
        connectState.update {
            it.copy(name = name ?: it.name, errorMessage = errorMessage)
        }
    }


    fun initStreamVideoClient(newUserName: String) {
        Timber.d("initStreamVideoClient() | name: $newUserName")
        updateConnectState(errorMessage = null)

        if (connectState.value.name.isBlank()) {
            updateConnectState(errorMessage = "The username can't be blank.")
            return
        }

        if (null == streamClient || connectState.value.name != newUserName) {
            StreamVideo.instance().logOut()
            StreamVideo.removeClient()

            streamClient = StreamVideoBuilder(
                context = application.applicationContext,
                apiKey = Constants.STREAM_SDK_API_KEY,
                user = User(
                    id = newUserName,
                    name = newUserName,
                    image = "https://bit.ly/2TIt8NR",
                    type = UserType.Guest
                ),
                geo = GEO.GlobalEdgeNetwork, // Choose appropriate geo region
                token = Constants.STREAM_SDK_TOKEN_KEY,
                // set the logging level
                loggingLevel = LoggingLevel(priority = Priority.DEBUG),
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
                initStreamVideoClient(connectState.value.name)
            }

            is UiEvent.OnDisconnectClicked -> {
                streamClient?.logOut()

                updateConnectState(
                    ConnectState(
                        name = connectState.value.name,
                        isConnected = false,
                        errorMessage = null
                    )
                )
            }
        }
    }
}