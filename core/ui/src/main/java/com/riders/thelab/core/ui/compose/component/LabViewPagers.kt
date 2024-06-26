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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

// extension method for current page offset
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabHorizontalViewPager(
    viewModel: BaseViewModel,
    pagerState: PagerState,
    items: List<App>,
    pageCount: Int = items.size,
    autoScroll: Boolean = false,
    content: @Composable (page: Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val screenWidth: Int = LocalConfiguration.current.screenWidthDp
    val horizontalPadding: Dp = 8.dp
    val itemWidth: Dp = dimensionResource(id = R.dimen.max_card_image_width)
    val contentPadding = PaddingValues(
        start = horizontalPadding,
        end = (screenWidth - itemWidth.value.toInt() + horizontalPadding.value.toInt()).dp
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        snapAnimationSpec = spring(
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
                //pageSpacing = 8.dp,
                //contentPadding = contentPadding,
                beyondViewportPageCount = 2,
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
                        },
                    contentAlignment = Alignment.Center
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

        if (autoScroll) {
            scope.launch {
                delay(3_000L)

                if (pagerState.currentPage == pageCount) {
                    pagerState.animateScrollToPage(0)
                } else {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> LabHorizontalViewPagerGeneric(
    viewModel: BaseViewModel,
    pagerState: PagerState,
    items: List<T>,
    pageCount: Int = items.size,
    autoScroll: Boolean = false,
    userScrollEnabled: Boolean = true,
    content: @Composable (page: Int, pageOffset: Float) -> Unit
) {
    val scope = rememberCoroutineScope()
    val screenWidth: Int = LocalConfiguration.current.screenWidthDp
    val horizontalPadding: Dp = 8.dp
    val itemWidth: Dp = dimensionResource(id = R.dimen.max_card_image_width)
    val contentPadding = PaddingValues(
        start = horizontalPadding,
        end = (screenWidth - itemWidth.value.toInt() + horizontalPadding.value.toInt()).dp
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        snapAnimationSpec = spring(
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
                //pageSpacing = 8.dp,
                //contentPadding = contentPadding,
                beyondViewportPageCount = 2,
                flingBehavior = fling,
                userScrollEnabled = userScrollEnabled
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
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // page composable
                    content(page, pageOffset)
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

        if (autoScroll) {
            scope.launch {
                while (true) {
                    delay(3_000L)

                    // Timber.d("onCurrentPageChanged | currentPage: ${pagerState.currentPage}, pageCount:$pageCount")
                    if (pagerState.currentPage == pageCount - 1) {
                        pagerState.animateScrollToPage(0)
                    } else {
                        if (items.size > 1)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> LabHorizontalViewPagerGeneric(
    pagerState: PagerState,
    items: List<T>,
    onCurrentPageChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    pageCount: Int = items.size,
    viewPagerDotVisibility: Boolean = false,
    updateViewPagerExpanded: (Boolean) -> Unit = {},
    viewPagerDotExpanded: Boolean = false,
    updateViewPagerDotVisibility: (Boolean) -> Unit = {},
    autoScroll: Boolean = false,
    userScrollEnabled: Boolean = true,
    content: @Composable (page: Int, pageOffset: Float) -> Unit
) {
    val scope = rememberCoroutineScope()
    val screenWidth: Int = LocalConfiguration.current.screenWidthDp
    val horizontalPadding: Dp = 8.dp
    val itemWidth: Dp = dimensionResource(id = R.dimen.max_card_image_width)
    val contentPadding = PaddingValues(
        start = horizontalPadding,
        end = (screenWidth - itemWidth.value.toInt() + horizontalPadding.value.toInt()).dp
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        snapAnimationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val dotsAnimatedAlpha =
        animateFloatAsState(
            targetValue = if (!viewPagerDotVisibility) 0.0f else 1f,
            label = ""
        )

    TheLabTheme {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            HorizontalPager(
                state = pagerState,
                //pageSpacing = 8.dp,
                //contentPadding = contentPadding,
                beyondViewportPageCount = 2,
                flingBehavior = fling,
                userScrollEnabled = userScrollEnabled
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
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // page composable
                    content(page, pageOffset)
                }
            }

            AnimatedVisibility(visible = viewPagerDotExpanded) {
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
            onCurrentPageChanged(page)
        }
    }

    LaunchedEffect(Unit) {
        delay(750L)
        updateViewPagerExpanded(true)

        delay(500L)
        updateViewPagerDotVisibility(true)

        if (autoScroll) {
            scope.launch {
                while (true) {
                    delay(3_000L)

                    // Timber.d("onCurrentPageChanged | currentPage: ${pagerState.currentPage}, pageCount:$pageCount")
                    if (pagerState.currentPage == pageCount - 1) {
                        pagerState.animateScrollToPage(0)
                    } else {
                        if (items.size > 1)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> LabVerticalViewPagerGeneric(
    pagerState: PagerState,
    items: List<T>,
    pageCount: Int = items.size,
    viewPagerDotVisibility: Boolean = false,
    onUpdateViewPagerExpanded: (Boolean) -> Unit,
    viewPagerDotExpanded: Boolean = false,
    onUpdateViewPagerDotVisibility: (Boolean) -> Unit,
    autoScroll: Boolean = false,
    userScrollEnabled: Boolean = true,
    onCurrentPageChanged: (Int) -> Unit,
    content: @Composable (page: Int, pageOffset: Float) -> Unit
) {
    val scope = rememberCoroutineScope()
    val screenWidth: Int = LocalConfiguration.current.screenWidthDp
    val horizontalPadding: Dp = 8.dp
    val itemWidth: Dp = dimensionResource(id = R.dimen.max_card_image_width)
    val contentPadding = PaddingValues(
        start = horizontalPadding,
        end = (screenWidth - itemWidth.value.toInt() + horizontalPadding.value.toInt()).dp
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        snapAnimationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val dotsAnimatedAlpha =
        animateFloatAsState(
            targetValue = if (!viewPagerDotVisibility) 0.0f else 1f,
            label = ""
        )

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            VerticalPager(
                state = pagerState,
                //pageSpacing = 8.dp,
                //contentPadding = contentPadding,
                beyondViewportPageCount = 2,
                flingBehavior = fling,
                userScrollEnabled = userScrollEnabled
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
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // page composable
                    content(page, pageOffset)
                }
            }

            AnimatedVisibility(visible = viewPagerDotExpanded) {
                VerticalPagerIndicator(
                    modifier = Modifier
                        .alpha(dotsAnimatedAlpha.value)
                        .zIndex(5f),
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
            onCurrentPageChanged(page)
        }
    }

    LaunchedEffect(Unit) {
        delay(750L)
        onUpdateViewPagerExpanded(true)

        delay(500L)
        onUpdateViewPagerDotVisibility(true)

        if (autoScroll) {
            scope.launch {
                while (true) {
                    delay(3_000L)

                    // Timber.d("onCurrentPageChanged | currentPage: ${pagerState.currentPage}, pageCount:$pageCount")
                    if (pagerState.currentPage == pageCount - 1) {
                        pagerState.animateScrollToPage(0)
                    } else {
                        if (items.size > 1)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
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
            .width(selectedIndicatorSize + indicatorPadding * 2)
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
                        horizontal = size / 4,
                        vertical = ((selectedIndicatorSize + indicatorPadding * 2) - size) / 2
                    )
                    .clip(RoundedCornerShape(indicatorCornerRadius))
                    .background(color)
                    .height(size)
                    .width(size / 2)
            )
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@DevicePreviews
@Composable
private fun PreviewLabHorizontalViewPagerGeneric() {
    TheLabTheme {
        LabHorizontalViewPagerGeneric(
            viewModel = hiltViewModel<BaseViewModel>(),
            pagerState = rememberPagerState { 10 },
            items = listOf("1", "2", "3", "4", "5", "6")
        ) { page, _ ->
            Text(text = "Element $page")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@DevicePreviews
@Composable
private fun PreviewHorizontalPagerIndicator() {
    TheLabTheme {
        HorizontalPagerIndicator(
            pageCount = listOf("1", "2", "3", "4", "5", "6").size,
            currentPage = 2,
            targetPage = 3,
            currentPageOffsetFraction = rememberPagerState { 10 }.currentPageOffsetFraction
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@DevicePreviews
@Composable
private fun PreviewLabVerticalViewPagerGeneric() {
    TheLabTheme {
        LabVerticalViewPagerGeneric(
            pagerState = rememberPagerState { 10 },
            items = listOf("1", "2", "3", "4", "5", "6"),
            onUpdateViewPagerExpanded = {},
            onUpdateViewPagerDotVisibility = {},
            onCurrentPageChanged = {}
        ) { page, _ ->
            Text(text = "Element $page")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@DevicePreviews
@Composable
private fun PreviewVerticalPagerIndicator() {
    TheLabTheme {
        VerticalPagerIndicator(
            pageCount = listOf("1", "2", "3", "4", "5", "6").size,
            currentPage = 2,
            targetPage = 3,
            currentPageOffsetFraction = rememberPagerState { 10 }.currentPageOffsetFraction
        )
    }
}
