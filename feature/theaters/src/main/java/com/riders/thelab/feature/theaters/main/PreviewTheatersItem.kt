package com.riders.thelab.feature.theaters.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.size.Scale
import coil3.size.Size
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.utils.Constants
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.component.image.CoilImage
import com.riders.thelab.core.ui.compose.component.ticket.TicketShape
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.feature.theaters.previewprovider.PreviewProviderTMDBItemModel

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TrendingTMDBItem(
    theme: AppTheme,
    darkTheme: Boolean,
    trendingItem: TMDBItemModel,
    uiEvent: (UiEvent) -> Unit
) {
    val painter = getCoilAsyncImagePainter(
        context = LocalContext.current,
        dataUrl = trendingItem.getPosterImageUrl(),
        size = Size.ORIGINAL,
        scale = Scale.FIT,
        isSvg = false,
        placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
    )

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .defaultMinSize(1.dp)
                .height(trendingItemImageHeight)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trendingItemImageHeight)
                    .clip(RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp))
                    .align(Alignment.Center),
                painter = painter,
                contentDescription = "weather icon wth coil",
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black,
                                Color.Black,
                            )
                        )
                    )
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    text = "Trending right now",
                    color = Color.White,
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Start
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(modifier = Modifier.weight(1f), onClick = {}) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Plus icon")
                        }
                    }
                    Text(
                        modifier = Modifier
                            .weight(2f)
                            .padding(24.dp),
                        text = trendingItem.title,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { uiEvent.invoke(UiEvent.OnItemDetailClicked(trendingItem)) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Outline more detail icon"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrendingTMDBItemTV(
    theme: AppTheme,
    darkTheme: Boolean,
    trendingItem: TMDBItemModel,
    modifier: Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    val painter = getCoilAsyncImagePainter(
        context = LocalContext.current,
        dataUrl = trendingItem.getPosterImageUrl(),
        size = Size.ORIGINAL,
        scale = Scale.FIT,
        isSvg = false,
        placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
    )
    val painterState by painter.state.collectAsStateWithLifecycle()

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        Box(modifier = modifier) {
            CoilImage(
                theme = theme,
                darkTheme = darkTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trendingItemImageHeight)
                    .clip(RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp))
                    .align(Alignment.Center),
                painterState = painterState,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Black,
                                Color.Black,
                            )
                        )
                    )
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
            ) {
                androidx.tv.material3.Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    text = "Trending right now",
                    color = Color.White,
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Start
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.tv.material3.IconButton(modifier = Modifier.weight(1f), onClick = {}) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            androidx.tv.material3.Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Plus icon"
                            )
                        }
                    }
                    androidx.tv.material3.Text(
                        modifier = Modifier
                            .weight(2f)
                            .padding(24.dp),
                        text = trendingItem.title,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    androidx.tv.material3.IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { uiEvent.invoke(UiEvent.OnItemDetailClicked(trendingItem)) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            androidx.tv.material3.Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Outline more detail icon"
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TMDBItem(
    theme: AppTheme,
    darkTheme: Boolean,
    tmdbItem: TMDBItemModel,
    uiEvent: (UiEvent) -> Unit
) {
    val painter =
        getCoilAsyncImagePainter(
            context = LocalContext.current,
            dataUrl = "${Constants.BASE_ENDPOINT_TMDB_IMAGE_W_500}${tmdbItem.poster}",
            isSvg = false,
            placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
        )

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(
            modifier = Modifier
                .size(
                    width = dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_height),
                    height = dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_width)
                )
                .clip(TicketShape(circleRadius = 8.dp, cornerSize = CornerSize(8.dp))),
            onClick = { uiEvent.invoke(UiEvent.OnItemDetailClicked(tmdbItem)) }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_width))
                        .weight(2.5f)
                        .clip(RoundedCornerShape(12.dp)),
                    painter = painter,
                    contentDescription = "icon wth coil",
                    contentScale = ContentScale.Crop,
                )

                Text(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = tmdbItem.title,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TMDBItemTV(
    theme: AppTheme,
    darkTheme: Boolean,
    tmdbItem: TMDBItemModel,
    modifier: Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    val painter =
        getCoilAsyncImagePainter(
            context = LocalContext.current,
            dataUrl = "${Constants.BASE_ENDPOINT_TMDB_IMAGE_W_500}${tmdbItem.poster}",
            isSvg = false,
            placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
        )

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        androidx.tv.material3.Card(
            modifier = modifier,
            onClick = { uiEvent.invoke(UiEvent.OnItemDetailClicked(tmdbItem)) }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_width))
                        .weight(2.5f)
                        .clip(RoundedCornerShape(12.dp)),
                    painter = painter,
                    contentDescription = "icon wth coil",
                    contentScale = ContentScale.Crop,
                )

                androidx.tv.material3.Text(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = tmdbItem.title,
                    textAlign = TextAlign.Center
                )
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
private fun PreviewTrendingTMDBItem(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabTheme(theme = AppTheme.Default) {
        TrendingTMDBItem(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(), item) { }
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewTrendingTMDBItemTV(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabThemeTV(theme = AppTheme.Default) {
        TrendingTMDBItemTV(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(), item) { }
    }
}

@DevicePreviews
@Composable
private fun PreviewTMDBItem(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabTheme(theme = AppTheme.Default) {
        TMDBItem(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(), item) { }
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewTMDBItemTV(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabThemeTV(theme = AppTheme.Default) {
        TMDBItemTV(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(), item) { }
    }
}