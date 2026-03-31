package com.riders.thelab.call.core.compose.component


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun CallControl(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isActive: Boolean = false,
    enabled: Boolean = true
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color(0xFF2A2A2E)
            isActive -> Color(0xFF3D7EFF)
            else -> Color(0xFF1C1C22)
        },
        label = "bgColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (enabled) Color.White else Color.Gray,
        label = "contentColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(
                    enabled = enabled,
                    indication = LocalIndication.current,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = label,
            color = contentColor,
            fontSize = 13.sp
        )
    }
}


@DevicePreviewsPhoneOnly
@Composable
private fun PreviewCallControlDisabled() {
    TheLabTheme(theme = AppTheme.Default) {
        CallControl(Icons.Default.MicOff, "Mute", {})
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewCallControlEnabled() {
    TheLabTheme(theme = AppTheme.Default) {
        CallControl(Icons.Default.MicOff, "Mute", {}, isActive = true)
    }
}