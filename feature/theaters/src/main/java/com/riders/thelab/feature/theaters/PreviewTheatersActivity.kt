package com.riders.thelab.feature.theaters

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import kotlinx.coroutines.delay


val trendingItemImageHeight: Dp = 550.dp


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TheatersContainer(viewModel: TheatersViewModel, networkManager: LabNetworkManager) {

    val switch = remember { mutableStateOf(false) }
    val networkState by networkManager.networkState.collectAsStateWithLifecycle()

    TheLabTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_dark_background)
        ) {
            if (!viewModel.once) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.Center),
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut())
                    },
                    targetState = if (LocalInspectionMode.current) true else switch.value,
                    label = "splashscreen animation"
                ) { targetState ->
                    if (!targetState) {
                        TheatersSplash()
                    } else {
                        TheatersContent(viewModel, networkState, viewModel.isRefreshing) {
                            viewModel.updateIsRefreshing(it)
                            if (it) {
                                viewModel.fetchTMDBData()
                            }
                        }
                    }
                }
            } else {
                TheatersContent(viewModel, networkState, viewModel.isRefreshing) {
                    viewModel.updateIsRefreshing(it)
                    if (it) {
                        viewModel.fetchTMDBData()
                    }
                }
            }
        }
    }

    LaunchedEffect(switch) {
        delay(3000L)
        switch.value = true
        viewModel.updateOnce()
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewTheatersContainer() {
    val viewModel: TheatersViewModel = hiltViewModel()
    val networkManager =
        LabNetworkManager(LocalContext.current, LocalLifecycleOwner.current.lifecycle)

    TheLabTheme(darkTheme = true) {
        TheatersContainer(viewModel, networkManager)
    }
}