package com.riders.thelab.feature.theaters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.bean.MovieCategoryEnum
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTrendingTvShowItemUiState
import com.riders.thelab.core.data.local.model.compose.TMDBUiState.TMDBTvShowsUiState
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.toModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ScreenTvShowsContent(
    trendingTvShowItem: TMDBTrendingTvShowItemUiState,
    trendingTvShows: TMDBTvShowsUiState,
    onTMDBItemDetailClicked: (tmdbItemModel: TMDBItemModel) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val lazyRowTrendingListState = rememberLazyListState()

    TheLabTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = lazyListState
            ) {
                item {
                    AnimatedContent(targetState = trendingTvShowItem, label = "") { targetState ->

                        if (targetState is TMDBTrendingTvShowItemUiState.Success) {
                            val item: TMDBItemModel =
                                targetState.response.results.map { it.toModel() }[0]

                            TrendingTMDBItem(trendingItem = item) {
                                onTMDBItemDetailClicked(it)
                            }
                        }
                    }
                }

                // TRENDING
                item {
                    AnimatedContent(targetState = trendingTvShows, label = "") { targetState ->
                        if (targetState is TMDBTvShowsUiState.Success) {

                            val tmdbList: List<TMDBItemModel> =
                                targetState.response.results.map { it.toModel() }

                            TheaterTMDBList(
                                rowListState = lazyRowTrendingListState,
                                categoryTitle = MovieCategoryEnum.TRENDING.value,
                                tmdbList = tmdbList
                            ) { item: TMDBItemModel ->
                                Timber.d(item.toString())
                                onTMDBItemDetailClicked(item)
                            }
                        }
                    }
                }

                item {
                    ProvidedBy(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        providerIcon = R.drawable.tmdb_logo
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
private fun PreviewScreenTvShowsContent() {
}