package com.riders.thelab.core.ui.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.success
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


///////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////
@Composable
fun Toast(message: String, imageVector: ImageVector? = null, containerColor: Color? = null) {
    val scope = rememberCoroutineScope()
    var animated by remember { mutableStateOf(false) }

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF1E1E1E)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedVisibility(
                visible = if (LocalInspectionMode.current) true else animated,
                enter = fadeIn() + slideIn(
                    initialOffset = { IntOffset(0, -300) },
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = FastOutLinearInEasing
                    )
                ),
                exit = slideOut(
                    targetOffset = { IntOffset(0, 300) },
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = LinearOutSlowInEasing
                    )
                ) + fadeOut()
            ) {
                Card(
                    modifier = Modifier.padding(bottom = 64.dp),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = message)

                        if (null != imageVector && null != containerColor) {
                            Card(
                                modifier = Modifier.size(30.dp),
                                colors = CardDefaults.cardColors(containerColor = containerColor)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = imageVector,
                                        contentDescription = "icon_icon",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(animated) {
        scope.launch {
            animated = true
            delay(1_500)
            animated = false
        }
    }
}


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewToast() {
    TheLabTheme {
        Toast(message = "This is a toast from TheLab")
    }
}

@DevicePreviews
@Composable
private fun PreviewToastWithIcon() {
    TheLabTheme {
        Toast(
            message = "This is a toast from TheLab",
            imageVector = Icons.Filled.BatteryFull,
            containerColor = success
        )
    }
}