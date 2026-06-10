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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.local.model.compose.Screen
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.videocall.data.CallState
import com.riders.thelab.feature.videocall.data.VideoCallState
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.actions.DefaultOnCallActionHandler
import io.getstream.video.android.compose.ui.components.call.renderer.FloatingParticipantVideo
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.mock.StreamPreviewDataUtils
import io.getstream.video.android.mock.previewCall
import kotlinx.coroutines.launch
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
    val scope = rememberCoroutineScope()

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

            /*val callPermissionState = rememberCallPermissionsState(
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
            )*/

            LaunchCallPermissions(
                call = callUiState.call,
                onPermissionsResult = { permissions ->
                    if (!permissions.all { it.value }) {
                        UIManager.showToast(
                            context = context,
                            message = "Permissions not granted"
                        )
                    } else {
                        Timber.tag("PreviewVideoCallScreen").d("Permissions granted")
                        // uiEvent.invoke(VideoCallUiEvent.OnJoinClick)
                        scope.launch { callUiState.call.join(create = true) }
                    }
                })

            VideoTheme {
                // Define required properties.
                val participants by callUiState.call.state.participants.collectAsStateWithLifecycle()
                val remoteParticipants by callUiState.call.state.remoteParticipants.collectAsStateWithLifecycle()
                val remoteParticipant = remoteParticipants.firstOrNull()
                val me by callUiState.call.state.me.collectAsStateWithLifecycle()
                val connection by callUiState.call.state.connection.collectAsStateWithLifecycle()

                var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

                val isCameraEnabled by callUiState.call.camera.isEnabled.collectAsStateWithLifecycle()
                val isMicrophoneEnabled by callUiState.call.microphone.isEnabled.collectAsStateWithLifecycle()

                callUiState.call.camera.setEnabled(true)
                callUiState.call.microphone.setEnabled(isMicrophoneEnabled)

                LaunchedEffect(connection) {
                    Timber.tag("PreviewVideoCallScreen").i("connection: $connection")
                }

                if (connection == RealtimeConnection.Connected) {

                    CallContent(
                        modifier = Modifier.fillMaxSize(),
                        call = callUiState.call,
                        //permissions = callPermissionState,
                        onCallAction = { callAction -> // Handles call control actions (mute, camera flip, etc.)
                            Timber.tag("PreviewVideoCallScreen").d("onCallAction: $callAction")

                            // Handle call actions
                            when (callAction) {
                                is FlipCamera -> callUiState.call.camera.flip() // Switch between front/back camera
                                is ToggleCamera -> callUiState.call.camera.setEnabled(callAction.isEnabled) // Enable/disable camera
                                is ToggleMicrophone -> callUiState.call.microphone.setEnabled(
                                    callAction.isEnabled
                                ) // Mute/unmute microphone
                                is LeaveCall -> {
                                    //  End the call
                                    uiEvent.invoke(VideoCallUiEvent.OnLeaveClick)
                                }

                                else -> Unit
                            }

                            DefaultOnCallActionHandler.onCallAction(callUiState.call, callAction)
                        },
                        onBackPressed = {
                            uiEvent.invoke(VideoCallUiEvent.OnLeaveClick)
                        },
                        floatingVideoRenderer = { call, intSize ->
                            me?.let { localVideo ->
                                FloatingParticipantVideo(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    call = callUiState.call,
                                    participant = localVideo,
                                    parentBounds = parentSize
                                )
                            }
                        }
                    )

                    /*Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(VideoTheme.colors.baseSenary)
                            .onSizeChanged { parentSize = it }
                    ) {
                        if (remoteParticipant != null) {
                            ParticipantVideo(
                                modifier = Modifier.fillMaxSize(),
                                call = callUiState.call,
                                participant = remoteParticipant
                            )
                        } else {
                            if (connection != RealtimeConnection.Connected) {
                                Text(
                                    text = "waiting for a remote participant...",
                                    fontSize = 30.sp,
                                    color = VideoTheme.colors.basePrimary
                                )
                            } else {
                                Text(
                                    modifier = Modifier.padding(30.dp),
                                    text = "Join call ${callUiState.call.id} in your browser to see the video here",
                                    fontSize = 30.sp,
                                    color = VideoTheme.colors.basePrimary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // floating video UI for the local video participant
                        me?.let { localVideo ->
                            FloatingParticipantVideo(
                                modifier = Modifier.align(Alignment.TopEnd),
                                call = callUiState.call,
                                participant = localVideo,
                                parentBounds = parentSize
                            )
                        }
                    }*/
                }
            }
        }
    }
    }
}


@DevicePreviewsPhoneOnly
@Composable
private fun PreviewVideoCallScreen() {

    StreamPreviewDataUtils.initializeStreamVideo(LocalContext.current)

    TheLabTheme(
        theme = AppTheme.Default,
        darkTheme = isSystemInDarkTheme(),
    ) {
        VideoCallScreen(
            theme = AppTheme.Default,
            callUiState = VideoCallState(previewCall, CallState.CONNECTED)
        ) {}
    }
}
