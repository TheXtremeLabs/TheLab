package com.riders.thelab.ui.theaters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_background
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_primaryContainer
import com.riders.thelab.core.compose.ui.theme.samsungSangFamily
import com.riders.thelab.data.local.bean.MovieCategoryEnum
import com.riders.thelab.data.local.bean.MovieEnum
import com.riders.thelab.data.local.model.Movie
import kotlinx.coroutines.delay


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TheatersSplash() {
    val scale = remember { Animatable(initialValue = 2f) }
    val theaterAdditionalTextVisibility = remember { mutableStateOf(false) }
    val visible = remember { mutableStateOf(false) }

    TheLabTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.scale(scale.value),
                    text = "T",
                    style = TextStyle(
                        fontFamily = samsungSangFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        color = md_theme_dark_primaryContainer
                    ),
                    maxLines = 1
                )
                AnimatedVisibility(visible = if (LocalInspectionMode.current) true else theaterAdditionalTextVisibility.value) {
                    Text(
                        modifier = Modifier.scale(scale.value),
                        text = "heaters",
                        style = TextStyle(
                            fontFamily = samsungSangFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            color = md_theme_dark_primaryContainer
                        ),
                        maxLines = 1
                    )
                }
            }


            AnimatedVisibility(visible = if (LocalInspectionMode.current) true else visible.value) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Powered By",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Image(
                        modifier = Modifier.height(12.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_the),
                        contentDescription = "the_icon",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Image(
                        modifier = Modifier.height(12.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_lab),
                        contentDescription = "lab_icon",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }
        }
    }

    LaunchedEffect(scale) {
        delay(750L)
        scale.animateTo(targetValue = 1f, initialVelocity = 0.3f)
    }

    LaunchedEffect(theaterAdditionalTextVisibility) {
        delay(1000L)
        theaterAdditionalTextVisibility.value = true
    }

    LaunchedEffect(visible) {
        delay(1300L)
        visible.value = true
    }
}

@Composable
fun TheaterCategoryList(
    viewModel: TheatersViewModel,
    rowListState: LazyListState,
    categoryTitle: String,
    movieList: List<Movie>
) {
    val list = remember { movieList }

    TheLabTheme {
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
            items(items = movieList.toList()) {
                MovieItem(viewModel = viewModel, movie = it)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheatersContent(viewModel: TheatersViewModel) {
    val lazyListState = rememberLazyListState()
    val lazyRowTrendingListState = rememberLazyListState()
    val lazyRowUpcomingListState = rememberLazyListState()
    val lazyRowPopularListState = rememberLazyListState()

    TheLabTheme(darkTheme = true) {
        Scaffold(
            topBar = {
                TheLabTopAppBar {}
            }
        ) {
            // Screen content
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    state = lazyListState
                ) {
                    item {
                        TrendingMovie(viewModel = viewModel, movie = MovieEnum.getMovies().random())
                    }

                    // TRENDING
                    item {
                        TheaterCategoryList(
                            viewModel = viewModel,
                            rowListState = lazyRowTrendingListState,
                            categoryTitle = MovieCategoryEnum.TRENDING.value,
                            movieList = viewModel.mTrendingMovies
                        )
                    }

                    // UPCOMING
                    item {
                        TheaterCategoryList(
                            viewModel = viewModel,
                            rowListState = lazyRowUpcomingListState,
                            categoryTitle = MovieCategoryEnum.UPCOMING.value,
                            movieList = viewModel.mUpcomingMovies
                        )
                    }

                    // POPULAR
                    item {
                        TheaterCategoryList(
                            viewModel = viewModel,
                            rowListState = lazyRowPopularListState,
                            categoryTitle = MovieCategoryEnum.POPULAR.value,
                            movieList = viewModel.mPopularMovies
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TheatersContainer(viewModel: TheatersViewModel) {

    val switch = remember { mutableStateOf(false) }

    TheLabTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_dark_background)
        ) {
            if (!viewModel.once) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.Center),
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    },
                    targetState = if (LocalInspectionMode.current) true else switch.value
                ) { targetState ->
                    if (!targetState) {
                        TheatersSplash()
                    } else {
                        TheatersContent(viewModel = viewModel)
                    }
                }
            } else {
                TheatersContent(viewModel = viewModel)
            }
        }
    }

    LaunchedEffect(switch) {
        delay(3000L)
        switch.value = true
        viewModel.updateOnce()
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewTheatersSplash() {
    TheLabTheme(darkTheme = true) {
        TheatersSplash()
    }
}

@DevicePreviews
@Composable
private fun PreviewTheatersContent() {
    val viewModel: TheatersViewModel = hiltViewModel()
    TheLabTheme(darkTheme = true) {
        TheatersContent(viewModel)
    }
}

@DevicePreviews
@Composable
private fun PreviewTheatersContainer() {
    val viewModel: TheatersViewModel = hiltViewModel()
    TheLabTheme(darkTheme = true) {
        TheatersContainer(viewModel)
    }
}