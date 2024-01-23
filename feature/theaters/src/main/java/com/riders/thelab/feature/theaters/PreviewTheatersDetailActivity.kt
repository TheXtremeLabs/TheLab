package com.riders.thelab.feature.theaters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheatersDetailContent(tmdbItem: TMDBItemModel) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val painter =
        getCoilAsyncImagePainter(
            context = LocalContext.current,
            dataUrl = if (null != tmdbItem.poster) {
                tmdbItem.getPosterImageUrl()
            } else {
                tmdbItem.getBackdropImageUrl()
            }
        )
    val state = painter.state

    /* Create the Palette, pass the bitmap to it */
    var palette: Palette

    /* Get the dark vibrant swatch */
    val darkVibrantSwatch = remember { mutableIntStateOf(0) }

    TheLabTheme(darkTheme = true) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TheLabTopAppBar(navigationIcon = {}) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = lazyListState
            ) {
                // Image
                item {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(1.dp)
                            .height(trendingItemImageHeight),
                        contentAlignment = Alignment.Center
                    ) {

                        AnimatedContent(
                            targetState = state,
                            label = "animated content state"
                        ) { targetState: AsyncImagePainter.State ->

                            when (targetState) {
                                is AsyncImagePainter.State.Loading -> {
                                    Timber.i("state is AsyncImagePainter.State.Loading")
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .scale(0.5f)
                                    )
                                }

                                is AsyncImagePainter.State.Success -> {
                                    Timber.d("state is AsyncImagePainter.State.Success")

                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .defaultMinSize(1.dp)
                                            .height(trendingItemImageHeight),
                                        painter = painter,
                                        contentDescription = "movie image",
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

                                            darkVibrantSwatch.intValue =
                                                palette.darkVibrantSwatch?.rgb ?: 0
                                            //paletteColorList.value = generatePalette(palette)
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
                }

                // Popularity and Rating
                item {
                    PopularityAndRating(tmdbItem)
                }

                // Overview
                item {
                    Overview(tmdbItem)
                }

                // Director
                /*item {
                    Director(movie)
                }*/

                // Scenarists
                /*item {
                    Scenarists(movie)
                }*/

                // Casting
                /*item {
                    Casting(movie)
                }*/
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
private fun PreviewTheatersDetailContent() {
    TheLabTheme(darkTheme = true) {
        TheatersDetailContent(tmdbItem = TMDBItemModel.mockItem)
    }
}