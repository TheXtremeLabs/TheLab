package com.riders.thelab.feature.artists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.res.painterResource
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
import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.utils.loadImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ArtistThumb(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: (Rect, Rect) -> TweenSpec<Rect>,
    urlThumb: String,
    index: Int,
    onPalette: (Palette) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val painter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = urlThumb,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        })
    val state = painter.state

    /* Create the Palette, pass the bitmap to it */
    var palette: Palette

    TheLabTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            with(sharedTransitionScope) {
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

                                onPalette(palette)
                            }
                        }
                    }
                }

                Image(
                    modifier = Modifier
                        .size(
                            width = this@BoxWithConstraints.maxWidth,
                            height = this@BoxWithConstraints.maxHeight
                        )
                        .aspectRatio(16f / 9f)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image-$index"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = boundsTransform
                        ),
                    painter = if (isError.not() && !LocalInspectionMode.current) {
                        painter
                    } else {
                        painterResource(id = R.drawable.logo_colors)
                    },
                    // TODO b/226661685: Investigate using alt text of  image to populate content description
                    // decorative image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ArtistDetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier,
    artist: ArtistModel,
    index: Int,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }


    val interactionSource = remember { MutableInteractionSource() }
    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(350) }

    var animateDescription by remember { mutableStateOf(false) }
    var animateGoBack by remember { mutableStateOf(false) }

    /* Create the Palette, pass the bitmap to it */
    var palette: Palette

    /* Get the swatches */
    var lightVibrantSwatch by remember { mutableIntStateOf(0) }
    var darkVibrantSwatch by remember { mutableIntStateOf(0) }
    var lightMutedSwatch by remember { mutableIntStateOf(0) }
    var darkMutedSwatch by remember { mutableIntStateOf(0) }
    val artistFullName by remember {
        mutableStateOf(StringBuilder().apply {
            append(artist.firstName)
            if (true == artist.secondName?.isNotBlank()) {
                append(", ")
                append(artist.secondName)
            }
            append(" ").append(artist.lastName)
        }.run {
            this.toString()
        })
    }

    TheLabTheme {
        Box(modifier = modifier) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                state = lazyListState,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            ) {
                item {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ArtistThumb(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            urlThumb = artist.urlThumb,
                            index = index,
                            boundsTransform = boundsTransform
                        ) { fetchedPalette ->
                            palette = fetchedPalette

                            lightVibrantSwatch =
                                palette.lightVibrantSwatch?.rgb ?: 0
                            lightMutedSwatch = palette.lightMutedSwatch?.rgb ?: 0
                            darkMutedSwatch = palette.darkMutedSwatch?.rgb ?: 0
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

                item {
                    with(sharedTransitionScope) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .sharedElement(
                                    state = rememberSharedContentState(key = "text-$index"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = boundsTransform
                                )
                                .skipToLookaheadSize(),
                            text = artist.sceneName,
                            style = TextStyle(
                                fontWeight = FontWeight.W600,
                                fontSize = 18.sp,
                                color = if (LocalInspectionMode.current) Color.Black else {
                                    Color(if (0 == darkMutedSwatch) darkVibrantSwatch else darkMutedSwatch)
                                }
                            ),
                        )
                    }
                }

                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = artistFullName,
                        color = if (LocalInspectionMode.current) Color.Black else {
                            if (!isSystemInDarkTheme()) Color.Black else Color.White
                        }
                    )
                }

                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Activities: ${artist.activities}",
                        color = if (LocalInspectionMode.current) Color.Black else {
                            if (!isSystemInDarkTheme()) Color.Black else Color.White
                        }
                    )
                }
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Since: ${artist.debutes}",
                        color = if (LocalInspectionMode.current) Color.Black else {
                            if (!isSystemInDarkTheme()) Color.Black else Color.White
                        }
                    )
                }

                item {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = if (LocalInspectionMode.current) true else animateDescription,
                        label = "animated_description_block"
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp),
                            text = artist.description,
                            color = if (LocalInspectionMode.current) Color.Black else {
                                if (!isSystemInDarkTheme()) Color.Black else Color.White
                            }
                        )
                    }
                }
            }


            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp), contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = isScrolled,
                    enter = fadeIn() + slideInVertically(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Button(onClick = { scope.launch { lazyListState.animateScrollToItem(0) } }) {
                        Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = null)
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
private fun PreviewYoutubeDetailScreen(@PreviewParameter(PreviewProviderArtist::class) artist: ArtistModel) {
    val navController = rememberNavController()
    TheLabTheme {
        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = ArtistScreen.Detail
            ) {
                composable<ArtistScreen.Detail> {
                    ArtistDetailScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        modifier = Modifier.fillMaxSize(),
                        artist = artist,
                        index = 0,
                        onBackClick = {}
                    )
                }
            }
        }
    }
}