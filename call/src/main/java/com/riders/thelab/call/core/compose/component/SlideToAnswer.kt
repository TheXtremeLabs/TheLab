package com.riders.thelab.call.core.compose.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SlideToAnswer(
    onAnswered: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val width = 300.dp
    val thumbSize = 56.dp

    val offsetX = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .width(width)
            .height(64.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF1C1C22)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Slide to answer",
            modifier = Modifier.align(Alignment.Center),
            color = Color(0xFFB3B3B3)
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color(0xFF3D7EFF))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            if (offsetX.value > size.width * 0.6f) {
                                onAnswered()
                            } else {
                                scope.launch {
                                    offsetX.animateTo(0f)
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        val newX = (offsetX.value + dragAmount.x)
                            .coerceIn(0f, size.width - thumbSize.toPx())
                        scope.launch {
                            offsetX.snapTo(newX)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Call,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewSlideToAnswer(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme, darkTheme = isSystemInDarkTheme()) {
        SlideToAnswer {}
    }
}