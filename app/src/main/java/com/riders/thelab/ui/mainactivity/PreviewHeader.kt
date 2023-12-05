package com.riders.thelab.ui.mainactivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.utils.findActivity


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun Header(viewModel: MainActivityViewModel) {

    val context = LocalContext.current
    val config = LocalConfiguration.current
    val toolbarHeight = 112.dp

    Box(modifier = Modifier.defaultMinSize(minHeight = config.screenWidthDp.dp / 2 - toolbarHeight)) {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
            visible = !viewModel.keyboardVisible.value,
            enter = expandVertically() + fadeIn(
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "What's new",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Search
                        Image(
                            modifier = Modifier.clickable {
                                viewModel.updateKeyboardVisible(true)
                                viewModel.displayDynamicIsland(true)
                            },
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.Black else Color.White)
                        )

                        // Settings
                        Image(
                            modifier = Modifier.clickable {
                                (context.findActivity() as MainActivity).launchSettings()
                            },
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.Black else Color.White)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                WhatsNewList(viewModel = viewModel)
            }
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
fun PreviewHeader() {
    val viewModel: MainActivityViewModel = hiltViewModel()
    Header(viewModel = viewModel)
}