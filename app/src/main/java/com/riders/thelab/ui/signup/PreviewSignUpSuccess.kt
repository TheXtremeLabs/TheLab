package com.riders.thelab.ui.signup

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabHtmlText
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_surfaceVariant
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_surfaceVariant
import kotlinx.coroutines.delay

private val lightGradient =
    listOf(
        Color.Transparent,
        md_theme_light_background,
        md_theme_light_surfaceVariant,
        md_theme_light_surfaceVariant
    )
private val darkGradient =
    listOf(
        Color.Transparent,
        md_theme_dark_background,
        md_theme_dark_surfaceVariant,
        md_theme_dark_surfaceVariant
    )

///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@DevicePreviews
@Composable
fun SignUpBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(if (!isSystemInDarkTheme()) lightGradient else darkGradient))
            .zIndex(1f)
    )
}

@Composable
fun SignUpSuccessContent(
    modifier: Modifier,
    username: String,
    onNavigateToSignUpSuccessScreen: () -> Unit
) {
    val animateButtonAlpha = remember { Animatable(0f) }
    val animateButtonEnable = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.85f)
            .padding(horizontal = 16.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Title congrats
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f),
            text = "Welcome $username,",
            style = Typography.titleLarge,
            color = if (!isSystemInDarkTheme()) Color.Black else Color.White
        )

        // Content
        LabHtmlText(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            stringResId = R.string.msg_successfully_signed_up
        )

        // Button next to MainActivity
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.alpha(if (LocalInspectionMode.current) 1f else animateButtonAlpha.value),
                onClick = onNavigateToSignUpSuccessScreen,
                enabled = animateButtonEnable.value
            ) {
                Text(text = stringResource(id = R.string.action_continue))
            }
        }
    }

    LaunchedEffect(animateButtonAlpha) {
        delay(750L)
        animateButtonAlpha.animateTo(1f)

        delay(1_500L)
        animateButtonEnable.value = true
    }
}

@Composable
fun SignUpSuccessScreen(viewModel: SignUpViewModel, onNavigateToSignUpSuccessScreen: () -> Unit) {
    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background lottie animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(0.dp, 300.dp)
                    .align(Alignment.TopCenter)
                    .zIndex(3f),
                contentAlignment = Alignment.Center
            ) {
                Lottie(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    rawResId = R.raw.lottie_congratulations
                )
            }

            // Background
            SignUpBackground()

            // Content
            SignUpSuccessContent(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .zIndex(5f),
                username = viewModel.username,
                onNavigateToSignUpSuccessScreen = onNavigateToSignUpSuccessScreen
            )
        }
    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewSignUpSuccessContent() {
    TheLabTheme {
        SignUpSuccessContent(
            modifier = Modifier
                .background(color = if (!isSystemInDarkTheme()) Color.Black else Color.White),
            username = "JaneDoe2563"
        ) {
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewSignUpSuccessScreen() {
    val viewModel: SignUpViewModel = hiltViewModel()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        SignUpSuccessScreen(viewModel = viewModel) {}
    }
}