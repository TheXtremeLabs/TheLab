package com.riders.thelab.feature.youtube.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.riders.thelab.core.data.local.model.compose.YoutubeUiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.NoItemFound
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun YoutubeContentSuccess(uiState: YoutubeUiState.Success) {
    val navController = rememberNavController()
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backstackEntry?.destination?.route

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = YoutubeScreen.List.route.toString()
        ) {
            composable(route = YoutubeScreen.List.route.toString()) {
                YoutubeListScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    videos = uiState.videos,
                    onVideoClicked = { item: Int ->
                        navController.navigate(route = "details/$item")
                    }
                )
            }

            composable(
                route = YoutubeScreen.Detail.route.toString(),
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backstackEntry ->
                val id =
                    backstackEntry.arguments?.getInt("id") ?: error("No URL")

                YoutubeDetailScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    modifier = Modifier.fillMaxSize(),
                    video = uiState.videos[id],
                    index = id,
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }

    LaunchedEffect(currentDestination) {
        currentDestination?.let {
            Timber.d("LaunchedEffect | currentDestination | current destination: $currentDestination")

        }
    }
}

@Composable
fun YoutubeContent(uiState: YoutubeUiState) {
    TheLabTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                targetState = uiState,
                contentAlignment = Alignment.Center,
                label = "youtube_like_animated_content"
            ) { targetState ->
                when (targetState) {
                    is YoutubeUiState.Loading -> {
                        LabLoader(modifier = Modifier.size(56.dp))
                    }

                    is YoutubeUiState.Error -> {
                        NoItemFound("An error occurred while loading\n${targetState.message.toString()}")
                    }

                    is YoutubeUiState.Success -> {
                        YoutubeContentSuccess(uiState = targetState)
                    }
                }
            }
        }
    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewYoutubeContent(@PreviewParameter(PreviewProviderYoutube::class) uiState: YoutubeUiState) {
    TheLabTheme {
        YoutubeContent(uiState)
    }
}