package com.riders.thelab.feature.artists

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.utils.loadImage
import kotlinx.coroutines.launch


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ArtistItem(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    artist: ArtistModel,
    index: Int,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(350) }

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val painter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = artist.urlThumb,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        })
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
                        if (isLoading) {
                            // Display a progress bar while loading
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(80.dp),
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        } else {
                            LaunchedEffect(state) {
                                if (state is AsyncImagePainter.State.Success) {
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
                                    }
                                }
                            }
                        }

                        Image(
                            modifier = Modifier.size(
                                width = this.maxWidth,
                                height = this.maxHeight
                            ),
                            contentScale = ContentScale.Crop,
                            painter = if (isError.not() && !LocalInspectionMode.current) {
                                painter
                            } else {
                                painterResource(id = com.riders.thelab.core.ui.R.drawable.logo_colors)
                            },
                            // TODO b/226661685: Investigate using alt text of  image to populate content description
                            // decorative image,
                            contentDescription = null,
                        )
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
                            text = artist.sceneName,
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

@OptIn(ExperimentalSharedTransitionApi::class)
@DevicePreviews
@Composable
private fun PreviewArtistItem(@PreviewParameter(PreviewProviderArtist::class) artist: ArtistModel) {
    val navController = rememberNavController()
    TheLabTheme {
        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = ArtistScreen.List.route.toString()
            ) {
                composable<ArtistScreen.List> {
                    ArtistItem(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        artist = artist,
                        index = 0
                    ) {}
                }
            }
        }
    }
}