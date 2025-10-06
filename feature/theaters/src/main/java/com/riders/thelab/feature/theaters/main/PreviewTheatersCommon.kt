package com.riders.thelab.feature.theaters.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.component.ticket.TicketShape
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV
import com.riders.thelab.feature.theaters.previewprovider.PreviewProviderTMDBItemModel


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TheaterTMDBList(
    theme: AppTheme,
    darkTheme: Boolean,
    rowListState: LazyListState,
    categoryTitle: String,
    tmdbList: List<TMDBItemModel>,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = categoryTitle,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                state = rowListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(items = tmdbList) { tmdbItem ->
                    TMDBItem(
                        theme = theme,
                        darkTheme = darkTheme,
                        tmdbItem = tmdbItem,
                        uiEvent = uiEvent
                    )
                }
            }
        }
    }
}

@Composable
fun TheaterTMDBListTV(
    theme: AppTheme,
    darkTheme: Boolean,
    rowListState: LazyListState,
    categoryTitle: String,
    tmdbList: List<TMDBItemModel>,
    modifier: Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            androidx.tv.material3.Text(
                modifier = Modifier.padding(start = 16.dp),
                text = categoryTitle,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            LazyRow(
                modifier = modifier

                    .padding(horizontal = 16.dp, vertical = 8.dp),
                state = rowListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(items = tmdbList) { index, tmdbItem ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val isFocused by interactionSource.collectIsFocusedAsState()

                    val scale by animateFloatAsState(if (isFocused) 1.5f else 1f)
                    val border by animateDpAsState(if (isFocused) 2.dp else 1.dp)

                    TMDBItemTV(
                        theme = theme,
                        darkTheme = darkTheme,
                        modifier = Modifier
                            .size(
                                width = dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_height),
                                height = dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_default_max_width)
                            )
                            .clip(TicketShape(circleRadius = 8.dp, cornerSize = CornerSize(8.dp)))
                            .focusable(enabled = true, interactionSource = interactionSource)
                            .scale(scale = scale)
                            .border(
                                width = border, brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White, Color.Transparent
                                    )
                                ), shape = RoundedCornerShape(12.dp)
                            ),
                        tmdbItem = tmdbItem,
                        uiEvent = uiEvent
                    )
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
private fun PreviewTheaterTMDBList(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabTheme(theme = AppTheme.Default) {
        TheaterTMDBList(
            theme = AppTheme.Default,
            darkTheme = true ,
            rowListState = rememberLazyListState(),
            categoryTitle = "Trending Movies",
            tmdbList = listOf(item),
        ) {}
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewTheaterTMDBListTV(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabThemeTV(theme = AppTheme.Default) {
        TheaterTMDBListTV(
            theme = AppTheme.Default,
            darkTheme = true ,
            rowListState = rememberLazyListState(),
            categoryTitle = "Trending Movies",
            tmdbList = listOf(item),
        ) {}
    }
}