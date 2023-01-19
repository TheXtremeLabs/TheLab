package com.riders.thelab.ui.mainactivity

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.compose.annotation.DevicePreviews

@Composable
fun Header(viewModel: MainActivityViewModel) {

    val config = LocalConfiguration.current
    val toolbarHeight = 112.dp

    Box(modifier = Modifier.defaultMinSize(minHeight = config.screenWidthDp.dp / 2 - toolbarHeight)) {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
            visible = !viewModel.keyboardVisible.value,
            enter = expandVertically() +  fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = shrinkVertically(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    visibilityThreshold = null
                )
            ) + fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    "What's new",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Thin
                )

                Spacer(modifier = Modifier.size(16.dp))

                WhatsNewList(viewModel = viewModel)
            }
        }
    }
}


@DevicePreviews
@Composable
fun PreviewHeader() {
    val viewModel: MainActivityViewModel = hiltViewModel()
    Header(viewModel = viewModel)
}