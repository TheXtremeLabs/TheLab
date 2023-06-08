package com.riders.thelab.ui.multipane

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
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.samsungSangFamily
import com.riders.thelab.data.local.bean.MovieEnum
import kotlinx.coroutines.delay


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun MultiPaneSplash() {
    val scale = remember { Animatable(initialValue = 2f) }
    val visible = remember { mutableStateOf(false) }

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                modifier = Modifier.scale(scale.value),
                text = "MultiPane",
                style = TextStyle(
                    fontFamily = samsungSangFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                )
            )

            AnimatedVisibility(visible = if (LocalInspectionMode.current) true else visible.value) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Powered By",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Image(
                        modifier = Modifier.height(12.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_the),
                        contentDescription = "the_icon",
                        colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.Black else Color.White)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Image(
                        modifier = Modifier.height(12.dp),
                        painter = painterResource(id = R.drawable.ic_lab_6_lab),
                        contentDescription = "lab_icon",
                        colorFilter = ColorFilter.tint(if (!isSystemInDarkTheme()) Color.Black else Color.White)
                    )
                }
            }
        }
    }

    LaunchedEffect(scale) {
        delay(750L)
        scale.animateTo(targetValue = 1f, initialVelocity = 0.3f)
    }

    LaunchedEffect(visible) {
        delay(1000L)
        visible.value = true
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiPaneContent() {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val lazyRowListState = rememberLazyListState()

    TheLabTheme {
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
                    state = lazyListState
                ) {
                    item {
                        TrendingMovie(movie = MovieEnum.getMovies().random())
                    }

                    item {
                        LazyRow(
                            modifier = Modifier.padding(16.dp),
                            state = lazyRowListState,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(items = MovieEnum.getMovies()) {
                                MovieItem(movie = it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MultiPaneContainer() {

    val switch = remember { mutableStateOf(false) }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                modifier = Modifier.align(Alignment.Center),
                transitionSpec = {
                    slideInVertically { height -> height } + fadeIn() with
                            slideOutVertically { height -> -height } + fadeOut()
                },
                targetState = if (LocalInspectionMode.current) true else switch.value
            ) { targetState ->
                if (!targetState) {
                    MultiPaneSplash()
                } else {
                    MultiPaneContent()
                }
            }
        }
    }

    LaunchedEffect(switch) {
        delay(3000L)
        switch.value = true
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewMultiPaneSplash() {
    TheLabTheme {
        MultiPaneSplash()
    }
}

@DevicePreviews
@Composable
private fun PreviewMultiPaneContent() {
    TheLabTheme {
        MultiPaneContent()
    }
}

@DevicePreviews
@Composable
private fun PreviewMultiPaneContainer() {
    TheLabTheme {
        MultiPaneContainer()
    }
}