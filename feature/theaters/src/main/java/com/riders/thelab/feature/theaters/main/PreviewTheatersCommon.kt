package com.riders.thelab.feature.theaters.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
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
    uiEvent: (UiEvent) -> Unit
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            androidx.tv.material3.Text(
                modifier = Modifier.padding(start = 16.dp),
                text = categoryTitle,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            LazyRow(
                modifier = Modifier
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                state = rowListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(items = tmdbList) { tmdbItem ->
                    TMDBItemTV(
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
            darkTheme = isSystemInDarkTheme(),
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
            darkTheme = isSystemInDarkTheme(),
            rowListState = rememberLazyListState(),
            categoryTitle = "Trending Movies",
            tmdbList = listOf(item),
        ) {}
    }
}