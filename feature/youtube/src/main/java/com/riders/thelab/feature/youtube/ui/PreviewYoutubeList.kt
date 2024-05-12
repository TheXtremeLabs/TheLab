package com.riders.thelab.feature.youtube.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.riders.thelab.core.data.local.model.Video
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun YoutubeListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier,
    videos: List<Video>,
    onVideoClicked: (Int) -> Unit
) {
    TheLabTheme {
        LazyColumn(
            modifier = Modifier.then(modifier),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = videos) { index, item ->
                YoutubeItem(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    index = index,
                    video = item
                ) {
                    onVideoClicked(index)
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@DevicePreviews
@Composable
private fun PreviewYoutubeListScreen(
) {
    val navController = rememberNavController()
    TheLabTheme {
        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = YoutubeScreen.List.route
            ) {
                composable(route = YoutubeScreen.List.route.toString()) {
                    YoutubeListScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        modifier = Modifier.fillMaxSize(),
                        videos = listOf(),
                        onVideoClicked = {}
                    )
                }
            }
        }
    }
}