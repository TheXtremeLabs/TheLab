package com.riders.thelab.ui.theaters

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TheatersDetailContent(viewModel: TheatersDetailViewModel) {
    val lazyListState = rememberLazyListState()

    val movie by viewModel.movieUiState.collectAsStateWithLifecycle()

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
        Scaffold(topBar = { TheLabTopAppBar {} }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = lazyListState
            ) {
                // Image
                item {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(1.dp)
                            .height(650.dp),
                        painter = painter,
                        contentDescription = "movie image",
                        contentScale = ContentScale.Crop,
                    )
                }

                // Popularity and Rating
                item {
                    PopularityAndRating(movie)
                }

                // Overview
                item {
                    Overview(movie)
                }

                // Director
                item {
                    Director(movie)
                }

                // Scenarists
                item {
                    Scenarists(movie)
                }

                // Casting
                item {
                    Casting(movie)
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
private fun PreviewTheatersDetailContent() {
    val viewModel: TheatersDetailViewModel = hiltViewModel()
    TheLabTheme(darkTheme = true) {
        TheatersDetailContent(viewModel = viewModel)
    }
}