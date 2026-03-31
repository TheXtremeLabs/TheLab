package com.riders.thelab.call.ui.call

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.call.core.compose.component.CallControl
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun ActiveCallScreen(
    name: String,
    elapsedTime: String,
    isMuted: Boolean = false,
    onMute: () -> Unit,
    onKeypad: () -> Unit,
    isSpeakerOn: Boolean = false,
    onSpeaker: () -> Unit,
    onEnd: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(32.dp))
            Text(name, color = Color.White, fontSize = 26.sp)
            Text(
                elapsedTime,
                color = Color(0xFFB3B3B3),
                fontFamily = FontFamily.Monospace
            )
        }

        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CallControl(Icons.Rounded.MicOff, "Mute", onMute)
                CallControl(Icons.Rounded.Dialpad, "Keypad", onKeypad)
                CallControl(Icons.AutoMirrored.Rounded.VolumeUp, "Audio", onSpeaker)
            }

            Spacer(Modifier.height(32.dp))

            IconButton(
                onClick = onEnd,
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Red, CircleShape)
            ) {
                Icon(Icons.Default.CallEnd, null, tint = Color.White)
            }
        }
    }
}


@DevicePreviewsPhoneOnly
@Composable
private fun PreviewActiveCallScreen(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme, darkTheme = isSystemInDarkTheme()) {
        ActiveCallScreen(
            name = "John Doe",
            elapsedTime = "3:08",
            onMute = {},
            onKeypad = {},
            onSpeaker = {},
            onEnd = {})
    }
}