package com.riders.thelab.feature.videocall.ui.main

import android.app.Application
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.application
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.core.ui.compose.base.BaseAndroidViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.videocall.BuildConfig
import com.riders.thelab.feature.videocall.data.ConnectState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StreamViewModel @Inject constructor(
    application: Application,
    uiRepository: IUiRepository
) : BaseAndroidViewModel(application = application, uiRepository = uiRepository),
    DefaultLifecycleObserver {

    private val currentName: NotBlankString = "Mike".toNotBlankString().getOrThrow()

    var streamClient: StreamVideo? = null

    private var _connectState: MutableStateFlow<ConnectState> =
        MutableStateFlow(ConnectState(currentName))
    val connectState: StateFlow<ConnectState> = _connectState

    var username: String by mutableStateOf(
        when {
            BuildConfig.DEBUG && Build.MODEL.contains(
                LabDeviceManager.MODEL_NAME_GALAXY_NOTE_8,
                ignoreCase = true
            ) -> "note8"

            BuildConfig.DEBUG && Build.MODEL.contains(
                LabDeviceManager.MODEL_NAME_GALAXY_NOTE_20_ULTRA,
                ignoreCase = true
            ) -> "note20"

            else -> ""
        }
    )
        private set

    fun updateConnectState(connectState: ConnectState) {
        Timber.d("updateConnectState() | connectState: $connectState")
        _connectState.update { connectState }
    }

    fun updateUserName(userName: String) {
        Timber.d("updateUserName() | userName: $userName")
        this.username = userName
    }


    fun initStreamVideoClient(newUserName: NotBlankString) {
        Timber.d("initStreamVideoClient() | name: $newUserName")
        _connectState.update { _connectState.value.copy(errorMessage = null) }
        if (_connectState.value.name.toString().isBlank()) {
            _connectState.update {
                _connectState.value.copy(errorMessage = "The username can't be blank.")
            }
            return
        }

        if (null == streamClient || newUserName != currentName) {
            StreamVideo.removeClient()

            streamClient = StreamVideoBuilder(
                context = application.applicationContext,
                apiKey = "djmy2f7dpjk8",
                user = User(
                    id = newUserName.toString(),
                    name = newUserName.toString(),
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
            is UiEvent.OnNameChanged -> {
                updateUserName(event.name)

                runCatching {
                    event.name.toNotBlankString().getOrThrow()
                }
                    .onFailure { exception ->
                        exception.printStackTrace()
                    }
                    .onSuccess {
                        updateConnectState(ConnectState(name = it))
                    }
            }

            is UiEvent.OnConnectClicked -> {
                initStreamVideoClient(username.toNotBlankString().getOrThrow())
            }

            is UiEvent.OnDisconnectClicked -> {}
        }
    }
}