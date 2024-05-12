package com.riders.thelab.feature.youtube.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.riders.thelab.core.data.local.model.Video
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun YoutubeItem(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    index: Int,
    video: Video,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val image = getCoilAsyncImagePainter(context = context, dataUrl = video.imageUrl)

    val interactionSource = remember { MutableInteractionSource() }
    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(550) }

    TheLabTheme {
        Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                with(sharedTransitionScope) {
                    Image(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .sharedElement(
                                state = rememberSharedContentState(key = "image-$index"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = boundsTransform
                            ),
                        painter = image,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElement(
                                state = rememberSharedContentState(key = "text-$index"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = boundsTransform
                            )
                            .skipToLookaheadSize(),
                        text = video.description
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@DevicePreviews
@Composable
private fun PreviewYoutubeItem() {
    val navController = rememberNavController()
    TheLabTheme {
        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = YoutubeScreen.List.route
            ) {
                composable(route = YoutubeScreen.List.route.toString()) {
                    YoutubeItem(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        video = Video(),
                        index = 0
                    ) {}
                }
            }
        }
    }
}