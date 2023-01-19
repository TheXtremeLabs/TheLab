package com.riders.thelab.ui.recycler

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.local.model.compose.ArtistsUiState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecyclerViewContent(viewModel: RecyclerViewModel) {

    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyGridState()

    val artistUiState by viewModel.artistUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TheLabTopAppBar(title = stringResource(id = R.string.activity_title_recycler_view)) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            when (artistUiState) {
                is ArtistsUiState.Loading -> {
                    Timber.i("state is ArtistsUiState.Loading")
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .scale(0.5f)
                            .align(Alignment.Center)
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
                        itemsIndexed((artistUiState as ArtistsUiState.Success).artists) { index, item ->

                            item.id = index

                            if (!LabCompatibilityManager.isNougat()) {
                                for (element in viewModel.artistThumbnails) {
                                    if (item.urlThumb.let { element.contains(it) }) {
                                        item.urlThumb = element
                                    }
                                }
                            } else {
                                // Java Stream
                                /*item.urlThumb =
                                    viewModel.artistThumbnails
                                        .stream()
                                        .filter { element: String ->
                                            element.contains(item.urlThumb)
                                        }
                                        ?.findFirst()
                                        ?.orElse("").toString()*/

                                // Kotlin Way
                                item.urlThumb =
                                    viewModel.artistThumbnails.find { element: String ->
                                        element.contains(
                                            item.urlThumb
                                        )
                                    }!!
                            }

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

@DevicePreviews
@Composable
fun PreviewRecyclerViewContent() {
    val viewModel: RecyclerViewModel = hiltViewModel()
    TheLabTheme {
        RecyclerViewContent(viewModel = viewModel)
    }
}