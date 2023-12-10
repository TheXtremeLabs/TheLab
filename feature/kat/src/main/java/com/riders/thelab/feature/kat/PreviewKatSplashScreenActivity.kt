package com.riders.thelab.feature.kat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.delay


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun KatSplashScreenContent() {
    val context = LocalContext.current

    val scaleAnimatedVisibility = remember { Animatable(1.5f) }
    val textAnimatedVisibility = remember { mutableStateOf(false) }
    val iconAnimatedVisibility = remember { mutableStateOf(false) }

    val textSize: TextUnit = 36.sp
    val textAndIconColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.White

    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.scale(scaleAnimatedVisibility.value),
                    text = "K",
                    style = TextStyle(fontSize = textSize, color = textAndIconColor)
                )

                AnimatedVisibility(
                    visible = if (LocalInspectionMode.current) true else textAnimatedVisibility.value,
                    enter = slideInHorizontally(tween(500)) + fadeIn(tween(500))
                ) {
                    Text(
                        text = "at",
                        style = TextStyle(fontSize = textSize, color = textAndIconColor)
                    )
                }

                AnimatedVisibility(
                    modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
                    visible = if (LocalInspectionMode.current) true else iconAnimatedVisibility.value,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500))
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Filled.Message,
                        contentDescription = "message_icon",
                        tint = textAndIconColor
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(150L)
        scaleAnimatedVisibility.animateTo(2f)
        delay(50L)
        scaleAnimatedVisibility.animateTo(1f)
        delay(300L)
        textAnimatedVisibility.value = true

        delay(850L)
        iconAnimatedVisibility.value = true

        delay(1_500L)
        (context.findActivity() as KatSplashscreenActivity).launchKatActivity()
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewKatSplashScreenContent() {
    TheLabTheme {
        KatSplashScreenContent()
    }
}