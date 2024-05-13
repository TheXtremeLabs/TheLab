package com.riders.thelab.feature.youtube.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import com.riders.thelab.core.data.local.model.youtube.Video
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.utils.loadImage
import kotlinx.coroutines.launch
import timber.log.Timber

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun YoutubeItem(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    video: Video,
    index: Int,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(350) }

    val painter = getCoilAsyncImagePainter(context = context, dataUrl = video.imageUrl)
    val state = painter.state

    /* Create the Palette, pass the bitmap to it */
    var palette: Palette

    /* Get the swatches */
    var lightVibrantSwatch by remember { mutableIntStateOf(0) }
    var lightMutedSwatch by remember { mutableIntStateOf(0) }
    var darkMutedSwatch by remember { mutableIntStateOf(0) }

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.max_card_image_height))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                with(sharedTransitionScope) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            modifier = Modifier.size(
                                width = this.maxWidth,
                                height = this.maxHeight
                            ),
                            targetState = state,
                            transitionSpec = { fadeIn() + expandIn() togetherWith fadeOut() },
                            contentAlignment = Alignment.Center,
                            label = "animated content state"
                        ) { targetState: AsyncImagePainter.State ->

                            when (targetState) {
                                is AsyncImagePainter.State.Loading -> {
                                    Timber.i("state is AsyncImagePainter.State.Loading")

                                    Box(
                                        modifier = Modifier.size(56.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(40.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                is AsyncImagePainter.State.Success -> {
                                    Timber.d("state is AsyncImagePainter.State.Success")

                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .defaultMinSize(1.dp)
                                            .aspectRatio(16f / 9f)
                                            .sharedElement(
                                                state = rememberSharedContentState(key = "image-$index"),
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                boundsTransform = boundsTransform
                                            )
                                            .clip(RoundedCornerShape(16.dp)),
                                        painter = painter,
                                        contentDescription = "youtube_item_image",
                                        contentScale = ContentScale.Crop,
                                    )

                                    LaunchedEffect(key1 = painter) {
                                        scope.launch {
                                            val image = painter.loadImage()

                                            palette = Palette.from(
                                                image.toBitmap(
                                                    image.intrinsicWidth,
                                                    image.intrinsicHeight
                                                )
                                            ).generate()

                                            ////////////////

                                            // Timber.d("Recomposition | palette swatches : ${palette.swatches.joinToString(" | ")}")

                                            lightVibrantSwatch =
                                                palette.lightVibrantSwatch?.rgb ?: 0
                                            lightMutedSwatch = palette.lightMutedSwatch?.rgb ?: 0
                                            darkMutedSwatch = palette.darkMutedSwatch?.rgb ?: 0

                                            /*Timber.d(
                                                "Recomposition | palette swatches : ${
                                                    listOf(
                                                        lightVibrantSwatch,
                                                        darkVibrantSwatch,
                                                        vibrantSwatch,
                                                        dominantSwatch,
                                                        lightMutedSwatch,
                                                        darkMutedSwatch,
                                                        mutedSwatch
                                                    ).joinToString(" | ")
                                                }"
                                            )*/
                                        }
                                    }
                                }

                                is AsyncImagePainter.State.Error -> {
                                    Timber.e("state is AsyncImagePainter.State.Error | ${targetState.result}")
                                }

                                else -> {
                                    Timber.e("state | else branch")
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(color = Color(darkMutedSwatch))
                            .zIndex(5f)
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .sharedElement(
                                    state = rememberSharedContentState(key = "text-$index"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = boundsTransform
                                )
                                .skipToLookaheadSize(),
                            text = video.name,
                            color = if (LocalInspectionMode.current) {
                                if (!isSystemInDarkTheme()) Color.Black else Color.White
                            } else {
                                Color(if (0 == lightMutedSwatch) lightVibrantSwatch else lightMutedSwatch)
                            }
                        )
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
@OptIn(ExperimentalSharedTransitionApi::class)
@DevicePreviews
@Composable
private fun PreviewYoutubeItem(@PreviewParameter(PreviewProviderVideo::class) video: Video) {
    val navController = rememberNavController()
    TheLabTheme {
        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = YoutubeScreen.List.route.toString()
            ) {
                composable(route = YoutubeScreen.List.route.toString()) {
                    YoutubeItem(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        video = video,
                        index = 0
                    ) {}
                }
            }
        }
    }
}