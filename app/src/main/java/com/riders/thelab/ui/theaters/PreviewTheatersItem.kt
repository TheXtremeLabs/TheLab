package com.riders.thelab.ui.theaters

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.Typography
import com.riders.thelab.core.compose.utils.findActivity
import com.riders.thelab.data.local.bean.MovieEnum
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.navigator.Navigator


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun TrendingMovie(viewModel: TheatersViewModel, movie: Movie) {
    val context = LocalContext.current
    val activity = context.findActivity() as TheatersActivity
    val navigator = Navigator(activity)

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(movie.urlThumbnail)
            .apply {
                crossfade(true)
                allowHardware(false)
                //transformations(RoundedCornersTransformation(32.dp.value))
                size(Size.ORIGINAL)
                scale(Scale.FIT)
            }
            .build(),
        placeholder = painterResource(R.drawable.logo_colors),
    )

    TheLabTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .defaultMinSize(1.dp)
                .height(650.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(650.dp)
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
                        text = "${movie.title}",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.getMovieDetail(activity, navigator, movie) }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(viewModel: TheatersViewModel, movie: Movie) {
    val context = LocalContext.current
    val activity = context.findActivity() as TheatersActivity
    val navigator = Navigator(activity)
    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(movie.urlThumbnail)
            .apply {
                crossfade(true)
                allowHardware(false)
                //transformations(RoundedCornersTransformation(32.dp.value))
                size(Size.ORIGINAL)
                scale(Scale.FIT)
            }
            .build(),
        placeholder = painterResource(R.drawable.logo_colors),
    )

    TheLabTheme(darkTheme = true) {

        Card(
            modifier = Modifier.size(
                width = dimensionResource(id = R.dimen.max_card_image_height),
                height = dimensionResource(id = R.dimen.max_card_image_width)
            ),
            onClick = {
                viewModel.getMovieDetail(activity, navigator, movie)
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2.5f)
                        .clip(RoundedCornerShape(12.dp)),
                    painter = painter,
                    contentDescription = "weather icon wth coil",
                    contentScale = ContentScale.Crop,
                )

                Text(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = "${movie.title}",
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
private fun PreviewTrendingMovie() {
    val viewModel: TheatersViewModel = hiltViewModel()
    val movie = MovieEnum.getMovies().random()

    TheLabTheme {
        TrendingMovie(viewModel, movie)
    }
}

@DevicePreviews
@Composable
private fun PreviewMovieItem() {
    val viewModel: TheatersViewModel = hiltViewModel()
    val movie = MovieEnum.getMovies().random()

    TheLabTheme {
        MovieItem(viewModel, movie)
    }
}