package com.riders.thelab.ui.theaters

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(painter = painter, contentDescription = "movie image")
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