package com.riders.thelab.core.ui.compose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabHorizontalViewPager(
    viewModel: BaseViewModel,
    items: List<App>,
    pageCount: Int = items.size,
    pagerState: PagerState = rememberPagerState { items.size },
    content: @Composable () -> Unit
) {
    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp
                /*contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )*/
            ) { page ->
                // page composable
                content()
            }

            HorizontalPagerIndicator(
                pageCount = pageCount,
                currentPage = pagerState.currentPage,
                targetPage = pagerState.targetPage,
                currentPageOffsetFraction = pagerState.currentPageOffsetFraction
            )
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onCurrentPageChanged(page)
        }
    }
}


@Composable
fun HorizontalPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    targetPage: Int,
    currentPageOffsetFraction: Float,
    modifier: Modifier = Modifier,
    indicatorColor: Color = Color.DarkGray,
    unselectedIndicatorSize: Dp = 8.dp,
    selectedIndicatorSize: Dp = 10.dp,
    indicatorCornerRadius: Dp = 2.dp,
    indicatorPadding: Dp = 2.dp
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentSize()
            .height(selectedIndicatorSize + indicatorPadding * 2)
    ) {

        // draw an indicator for each page
        repeat(pageCount) { page ->
            // calculate color and size of the indicator
            val (color, size) =
                if (currentPage == page || targetPage == page) {
                    // calculate page offset
                    val pageOffset =
                        ((currentPage - page) + currentPageOffsetFraction).absoluteValue
                    // calculate offset percentage between 0.0 and 1.0
                    val offsetPercentage = 1f - pageOffset.coerceIn(0f, 1f)

                    val size =
                        unselectedIndicatorSize + ((selectedIndicatorSize - unselectedIndicatorSize) * offsetPercentage)

                    indicatorColor.copy(
                        alpha = offsetPercentage
                    ) to size
                } else {
                    indicatorColor.copy(alpha = 0.1f) to unselectedIndicatorSize
                }

            // draw indicator
            Box(
                modifier = Modifier
                    .padding(
                        // apply horizontal padding, so that each indicator is same width
                        horizontal = ((selectedIndicatorSize + indicatorPadding * 2) - size) / 2,
                        vertical = size / 4
                    )
                    .clip(RoundedCornerShape(indicatorCornerRadius))
                    .background(color)
                    .width(size)
                    .height(size / 2)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabVerticalViewPager(
    viewModel: BaseViewModel,
    items: List<App>,
    pageCount: Int = items.size,
    pagerState: PagerState = rememberPagerState { items.size },
) {
    TheLabTheme {

        Row(modifier = Modifier.fillMaxWidth()) {
            VerticalPager(
                state = pagerState,
                contentPadding = PaddingValues(
                    horizontal = 32.dp,
                    vertical = 8.dp
                )
            ) { page ->
                // page composable
            }

            VerticalPagerIndicator(
                pageCount = pageCount,
                currentPage = pagerState.currentPage,
                targetPage = pagerState.targetPage,
                currentPageOffsetFraction = pagerState.currentPageOffsetFraction
            )
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onCurrentPageChanged(page)
        }
    }
}


@Composable
fun VerticalPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    targetPage: Int,
    currentPageOffsetFraction: Float,
    modifier: Modifier = Modifier,
    indicatorColor: Color = Color.DarkGray,
    unselectedIndicatorSize: Dp = 8.dp,
    selectedIndicatorSize: Dp = 10.dp,
    indicatorCornerRadius: Dp = 2.dp,
    indicatorPadding: Dp = 2.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .wrapContentSize()
            .height(selectedIndicatorSize + indicatorPadding * 2)
    ) {

        // draw an indicator for each page
        repeat(pageCount) { page ->
            // calculate color and size of the indicator
            val (color, size) =
                if (currentPage == page || targetPage == page) {
                    // calculate page offset
                    val pageOffset =
                        ((currentPage - page) + currentPageOffsetFraction).absoluteValue
                    // calculate offset percentage between 0.0 and 1.0
                    val offsetPercentage = 1f - pageOffset.coerceIn(0f, 1f)

                    val size =
                        unselectedIndicatorSize + ((selectedIndicatorSize - unselectedIndicatorSize) * offsetPercentage)

                    indicatorColor.copy(
                        alpha = offsetPercentage
                    ) to size
                } else {
                    indicatorColor.copy(alpha = 0.1f) to unselectedIndicatorSize
                }

            // draw indicator
            Box(
                modifier = Modifier
                    .padding(
                        // apply horizontal padding, so that each indicator is same width
                        horizontal = ((selectedIndicatorSize + indicatorPadding * 2) - size) / 2,
                        vertical = size / 4
                    )
                    .clip(RoundedCornerShape(indicatorCornerRadius))
                    .background(color)
                    .width(size)
                    .height(size / 2)
            )
        }
    }
}
