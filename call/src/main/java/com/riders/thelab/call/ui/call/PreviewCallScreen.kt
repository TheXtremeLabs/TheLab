package com.riders.thelab.call.ui.call

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.call.data.local.compose.CallState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun CallScreen(
    contactName: String,
    contactAvatar: Painter?, // pass null for initials
    callState: String,
    elapsedSeconds: Long,
    uiEvent: (UiEvent) -> Unit
) {
    var muted by remember { mutableStateOf(false) }
    var isDialPad by remember { mutableStateOf(false) }
    var onHold by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }

    val timerText = remember(elapsedSeconds) { formatSeconds(elapsedSeconds) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E11))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section: Avatar + Name + Timer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar
                if (contactAvatar != null) {
                    Image(
                        painter = contactAvatar,
                        contentDescription = "Caller Avatar",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3D7EFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contactName.first().uppercaseChar().toString(),
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Name
                Text(
                    text = contactName,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // Timer / call state
                Text(
                    text = if (callState == "active") timerText else callState,
                    color = Color(0xFFB3B3B3),
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Middle section: Control buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    CallButton(
                        icon = Icons.Default.MicOff,
                        backgroundColor = Color(0xFF1C1C22),
                        onClick = {
                            muted = !muted
                            uiEvent.invoke(UiEvent.OnMute(muted))
                        },
                        isEnable = muted
                    )
                    CallButton(
                        icon = Icons.Default.Dialpad,
                        backgroundColor = Color(0xFF1C1C22),
                        onClick = {
                            isDialPad = !isDialPad
                            uiEvent.invoke(UiEvent.OnKeypad(isDialPad))
                        },
                        isEnable = isDialPad
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    CallButton(
                        icon = Icons.Default.Pause,
                        backgroundColor = Color(0xFF1C1C22),
                        onClick = {
                            onHold = !onHold
                            uiEvent.invoke(UiEvent.OnHold(onHold))
                        },
                        isEnable = onHold
                    )
                    CallButton(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        backgroundColor = Color(0xFF1C1C22),
                        onClick = {
                            isSpeakerOn = !isSpeakerOn
                            uiEvent.invoke(UiEvent.OnSpeaker(isSpeakerOn))
                        },
                        isEnable = isSpeakerOn
                    )
                }
            }

            // Bottom section: End Call Button
            CallButton(
                icon = Icons.Default.CallEnd,
                backgroundColor = Color(0xFFFF3B30),
                size = 80.dp,
                onClick = { uiEvent.invoke(UiEvent.OnHangUp(contactName)) }
            )
        }
    }
}

// Reusable circular button
@Composable
fun CallButton(
    icon: ImageVector,
    backgroundColor: Color,
    size: Dp = 64.dp,
    isEnable: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (isEnable) Color.White else backgroundColor)
            .clickable(enabled = true, onClick = { onClick() }),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isEnable) Color.Black else Color.White,
            modifier = Modifier.size(size / 2)
        )
    }
}

// Timer formatting
fun formatSeconds(seconds: Long): String {
    val minutes = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(minutes, sec)
}

@Composable
fun CallScreen(callState: CallState, onHangup: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (callState) {
            is CallState.Idle -> {
                // Nothing to show
            }

            is CallState.Incoming -> {
                Text("Incoming Call from ${callState.caller}")
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = { /* TODO: Accept call */ }) {
                        Text("Accept")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { /* TODO: Reject call */ }) {
                        Text("Reject")
                    }
                }
            }

            is CallState.Outgoing -> {
                Text("Calling ${callState.number}...")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onHangup.invoke() }) {
                    Text("Hang Up")
                }
            }

            is CallState.Active -> {
                Text("Call with ${callState.number}")
                Text("Duration: ${callState.duration}s")
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = { /* TODO: Mute */ }) {
                        Text("Mute")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { /* TODO: Speaker */ }) {
                        Text("Speaker")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = { /* TODO: Hold */ }) {
                        Text("Hold")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { onHangup.invoke() }) {
                        Text("Hang Up")
                    }
                }
            }

            else -> {
                // Nothing to show
            }
        }
    }
}


@DevicePreviewsPhoneOnly
@Composable
private fun PreviewCallButtonEnabled(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
            CallButton(
                icon = Icons.Default.MicOff,
//                backgroundColor = Color(0xFF1C1C22),
                backgroundColor = appTheme.primaryColor,
                isEnable = true
            ) {}
        }
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewCallScreen(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = AppTheme.Default) {
        CallScreen(
            contactName = "John Doe",
            contactAvatar = null,
            callState = "Active",
            elapsedSeconds = 120
        ) {}
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun IncomingCallPreview(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        CallScreen(CallState.Incoming("1234567890")) {}
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun OutgoingCallPreview(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        CallScreen(CallState.Outgoing("1234567890")) {}
    }
}

@DevicePreviewsPhoneOnly
@Composable
fun ActiveCallPreview(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        CallScreen(CallState.Active("1234567890", 120)) {}
    }
}
