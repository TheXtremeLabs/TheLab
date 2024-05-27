package com.riders.thelab.feature.youtube.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun YoutubeDetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier,
    video: Video,
    index: Int,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(350) }

    var animateDescription by remember { mutableStateOf(false) }
    var animateGoBack by remember { mutableStateOf(false) }

    val painter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = video.imageUrl,
        isSvg = false,
        placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
    )
    val state = painter.state

    /* Create the Palette, pass the bitmap to it */
    var palette: Palette

    /* Get the swatches */
    var lightVibrantSwatch by remember { mutableIntStateOf(0) }
    var darkVibrantSwatch by remember { mutableIntStateOf(0) }
    var lightMutedSwatch by remember { mutableIntStateOf(0) }
    var darkMutedSwatch by remember { mutableIntStateOf(0) }

    LaunchedEffect(state) {
        delay(250L)

        when (state) {
            is AsyncImagePainter.State.Success -> {
                Timber.d("state is AsyncImagePainter.State.Success")

                scope.launch {
                    val image = painter.loadImage()

                    palette = Palette.from(
                        image.toBitmap(
                            image.intrinsicWidth,
                            image.intrinsicHeight
                        )
                    ).generate()

                    ////////////////

                    lightVibrantSwatch = palette.lightVibrantSwatch?.rgb ?: 0
                    darkVibrantSwatch = palette.darkVibrantSwatch?.rgb ?: 0
                    lightMutedSwatch = palette.lightMutedSwatch?.rgb ?: 0
                    darkMutedSwatch = palette.darkMutedSwatch?.rgb ?: 0
                }
            }

            else -> {
                Timber.e("state | else branch")
            }
        }
    }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
            ) {
                with(sharedTransitionScope) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .sharedElement(
                                state = rememberSharedContentState(key = "image-$index"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = boundsTransform
                            ),
                        painter = painter,
                        contentDescription = "youtube_detail_item_image",
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .sharedElement(
                                state = rememberSharedContentState(key = "text-$index"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = boundsTransform
                            )
                            .skipToLookaheadSize(),
                        text = video.name,
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 18.sp,
                            color = if (LocalInspectionMode.current) Color.Black else {
                                Color(if (0 == darkMutedSwatch) darkVibrantSwatch else darkMutedSwatch)
                            }
                        ),
                    )

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = if (LocalInspectionMode.current) true else animateDescription,
                        label = "animated_description_block"
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = video.description,
                            color = if (LocalInspectionMode.current) Color.Black else {
                                if (!isSystemInDarkTheme()) Color.Black else Color.White
                            }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onBackClick
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (LocalInspectionMode.current) {
                        if (!isSystemInDarkTheme()) Color.Black else Color.White
                    } else {
                        Color(if (0 == lightMutedSwatch) lightVibrantSwatch else lightMutedSwatch)
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(vertical = 8.dp),
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        tint = if (LocalInspectionMode.current) Color.White else {
                            Color(if (0 == darkMutedSwatch) darkVibrantSwatch else darkMutedSwatch)
                        }
                    )

                    AnimatedVisibility(
                        visible = if (LocalInspectionMode.current) true else animateGoBack,
                        label = "go_back_animation"
                    ) {
                        Text(
                            text = "Go back",
                            color = if (LocalInspectionMode.current) Color.White else {
                                Color(if (0 == darkMutedSwatch) darkVibrantSwatch else darkMutedSwatch)
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(750L)
        animateDescription = true

        delay(500L)
        animateGoBack = true
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
private fun PreviewYoutubeDetailScreen(@PreviewParameter(PreviewProviderVideo::class) video: Video) {
    val navController = rememberNavController()
    TheLabTheme {
        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = YoutubeScreen.Detail.route.toString()
            ) {
                composable(route = YoutubeScreen.Detail.route.toString()) {
                    YoutubeDetailScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        modifier = Modifier.fillMaxSize(),
                        video = video,
                        index = 0,
                        onBackClick = {}
                    )
                }
            }
        }
    }
}