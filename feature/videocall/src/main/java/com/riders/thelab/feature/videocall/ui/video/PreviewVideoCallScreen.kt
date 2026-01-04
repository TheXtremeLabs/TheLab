package com.riders.thelab.feature.videocall.ui.video

import android.Manifest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.data.local.model.compose.Screen
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.videocall.data.CallState
import com.riders.thelab.feature.videocall.data.VideoCallState
import com.riders.thelab.feature.videocall.ui.main.StreamActivity
import io.getstream.video.android.compose.permission.rememberCallPermissionsState
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
data object VideoCallRoute : Screen(route = "video_call")

@Composable
fun VideoCallScreen(
    theme: AppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    callUiState: VideoCallState,
    uiEvent: (VideoCallUiEvent) -> Unit
) {
    val context = LocalContext.current

    TheLabTheme(
        theme = theme,
        darkTheme = darkTheme
    ) {
        when {
            callUiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = callUiState.error.message ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            callUiState.state == CallState.CONNECTING -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LabLoader(modifier = Modifier.size(40.dp))
                    Text(text = "Joining...")
                }
            }

            else -> {
                val permissions = listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                )

                val bluetoothConnectPermissions =
                    if (LabCompatibilityManager.isS()) listOf(Manifest.permission.BLUETOOTH_CONNECT) else emptyList()

                val notificationsPermissions =
                    if (LabCompatibilityManager.isTiramisu()) listOf(Manifest.permission.POST_NOTIFICATIONS) else emptyList()

                val callPermissionState = rememberCallPermissionsState(
                    call = callUiState.call,
                    permissions = permissions + bluetoothConnectPermissions + notificationsPermissions,
                    onPermissionsResult = { permissions ->
                        if (!permissions.all { it.value }) {
                            UIManager.showToast(
                                context = context,
                                message = "Permissions not granted"
                            )
                        } else {
                            Timber.tag("PreviewVideoCallScreen").d("Permissions granted")
                            uiEvent.invoke(VideoCallUiEvent.OnJoinClick)
                        }
                    }
                )

                VideoTheme {
                    CallContent(
                        modifier = Modifier.fillMaxSize(),
                        call = callUiState.call,
                        permissions = callPermissionState,
                        onCallAction = { action ->
                            Timber.tag("PreviewVideoCallScreen").d("onCallAction: $action")
                            if (LeaveCall == action) {
                                uiEvent.invoke(VideoCallUiEvent.OnLeaveClick)
                            }

                            DefaultOnCallActionHandler.onCallAction(callUiState.call, action)
                        },
                        onBackPressed = {
                            uiEvent.invoke(VideoCallUiEvent.OnLeaveClick)
                        }
                    )
                }
            }
        }
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewVideoCallScreen() {
    val context = LocalContext.current
    val streamClient = remember {
        StreamVideoBuilder(
            context = context,
            apiKey = "djmy2f7dpjk8",
            user = User(
                id = "123",
                name = "123",
                type = UserType.Guest
            )
        )
            .build()
    }

    TheLabTheme(
        theme = AppTheme.Default,
        darkTheme = isSystemInDarkTheme(),
    ) {
        VideoCallScreen(
            theme = AppTheme.Default,
            callUiState = VideoCallState(streamClient.call("default", "123")),
            uiEvent = {}
        )

    }
}