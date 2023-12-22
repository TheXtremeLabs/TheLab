package com.riders.thelab.core.ui.compose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

// extension method for current page offset
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabHorizontalViewPager(
    viewModel: BaseViewModel,
    pagerState: PagerState,
    items: List<App>,
    pageCount: Int = items.size,
    content: @Composable (page: Int) -> Unit
) {
    val horizontalPadding: Dp = 16.dp
    val itemWidth: Dp = 340.dp
    val screenWidth: Int = LocalConfiguration.current.screenWidthDp
    val contentPadding: PaddingValues = PaddingValues(
        start = horizontalPadding,
        end = (screenWidth - itemWidth.value.toInt() + horizontalPadding.value.toInt()).dp
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(2),
        lowVelocityAnimationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val dotsAnimatedAlpha =
        animateFloatAsState(
            targetValue = if (!viewModel.viewPagerDotVisibility) 0.0f else 1f,
            label = ""
        )

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                contentPadding = contentPadding,
                beyondBoundsPageCount = 2,
                flingBehavior = fling
            ) { page: Int ->

                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)

                val imageSize = animateFloatAsState(
                    targetValue = if (0.0f != pageOffset) 0.75f else 1f,
                    animationSpec = tween(durationMillis = 500),
                    label = "image size animation"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            // translate the contents by the size of the page, to prevent the pages from sliding in from left or right and stays in the center
//                            translationX = pageOffset * size.width
                            // apply an alpha to fade the current page in and the old page out
                            // alpha = 1 - pageOffset.absoluteValue

                            // get a scale value between 1 and 1.75f, 1.75 will be when its resting,
                            // 1f is the smallest it'll be when not the focused page
                            //val scale = lerp(.75f, 1f, pageOffset)
                            // apply the scale equally to both X and Y, to not distort the image
                            scaleX = imageSize.value
                            scaleY = imageSize.value
                        }
                ) {
                    // page composable
                    content(page)
                }
            }

            AnimatedVisibility(visible = viewModel.viewPagerDotExpanded) {
                HorizontalPagerIndicator(
                    modifier = Modifier.alpha(dotsAnimatedAlpha.value),
                    pageCount = pageCount,
                    currentPage = pagerState.currentPage,
                    targetPage = pagerState.targetPage,
                    currentPageOffsetFraction = pagerState.currentPageOffsetFraction
                )
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.onCurrentPageChanged(page)
        }
    }

    LaunchedEffect(Unit) {
        delay(750L)
        viewModel.updateViewPagerExpanded(true)

        delay(500L)
        viewModel.updateViewPagerDotVisibility(true)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> LabHorizontalViewPagerGeneric(
    viewModel: BaseViewModel,
    pagerState: PagerState,
    items: List<T>,
    pageCount: Int = items.size,
    content: @Composable (page: Int) -> Unit
) {
    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp
            ) { page: Int ->
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                            // translate the contents by the size of the page, to prevent the pages from sliding in from left or right and stays in the center
                            translationX = pageOffset * size.width
                            // apply an alpha to fade the current page in and the old page out
                            // alpha = 1 - pageOffset.absoluteValue
                        }
                ) {
                    // page composable
                    content(page)
                }
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
