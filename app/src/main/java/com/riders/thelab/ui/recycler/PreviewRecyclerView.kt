package com.riders.thelab.ui.recycler

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.compose.ArtistsUiState
import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.weather.core.component.TheLabTopAppBar
import timber.log.Timber

@Composable
fun RecyclerViewContent(artistUiState: ArtistsUiState) {
    val lazyState = rememberLazyGridState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TheLabTopAppBar(title = stringResource(id = R.string.activity_title_recycler_view)) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {

            AnimatedContent(
                targetState = artistUiState,
                label = "recycler animation content"
            ) { targetState ->

                when (targetState) {
                    is ArtistsUiState.Loading -> {
                        Timber.i("state is ArtistsUiState.Loading")
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    is ArtistsUiState.Success -> {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            state = lazyState,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            columns = GridCells.Fixed(2)
                        ) {
                            itemsIndexed(
                                items = targetState.artists,
                                key = { _: Int, item: ArtistModel -> item.id }
                            ) { _, item ->
                                Artist(artist = item)
                            }
                        }
                    }

                    else -> {
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun PreviewRecyclerViewContent(@PreviewParameter(PreviewProviderArtistUiState::class) state: ArtistsUiState) {
    TheLabTheme {
        RecyclerViewContent(state)
    }
}