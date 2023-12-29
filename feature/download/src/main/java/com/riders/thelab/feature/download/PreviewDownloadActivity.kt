package com.riders.thelab.feature.download

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography

////////////////////////////////////////
//
// COMPOSE
//
////////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloaderContent(viewModel: DownloadViewModel) {

    val lazyListState = rememberLazyListState()
    val downloadList by viewModel.downloadList.collectAsStateWithLifecycle()

    TheLabTheme {
        Scaffold(
            topBar = {
                TheLabTopAppBar(title = stringResource(id = com.riders.thelab.core.ui.R.string.activity_title_download))
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Welcome to ChronoDownloader",
                    style = Typography.bodyLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(2f),
                        text = "Click on start button to start downloading files"
                    )

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (!viewModel.isDownloadStarted) {
                                viewModel.updateIsDownloadStarted(true)
                            } else {
                                viewModel.updateIsDownloadStarted(false)
                            }
                        },
                        enabled = viewModel.canDownload
                    ) {
                        Text(
                            text = if (!viewModel.isDownloadStarted) "Launch Download" else "Stop download",
                            fontSize = 12.sp
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = downloadList,
                        key = { _, item -> item.id }
                    ) { index, item ->

                        val isSamDB = item.filename.contains("bases_sam", true)
                        val isAdrDb = item.filename.contains("base_adr", true)

                        if (isSamDB || isAdrDb) {
                            DownloadItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement(
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = LinearOutSlowInEasing,
                                        )
                                    ),
                                item = item,
                                isDbDownload = isSamDB || isAdrDb
                            )
                        } else {
                            DownloadItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .animateItemPlacement(
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = LinearOutSlowInEasing,
                                        )
                                    ),
                                item = item
                            )
                        }
                    }
                }
            }
        }
    }

    /*LaunchedEffect(true) {
        Timber.d("LaunchedEffect | key = true | context: ${this.coroutineContext}")
        scope.launch {
            while (mainProgress.floatValue <= 1f) {
                mainProgress.floatValue += 0.01f
                delay(25L)
            }
        }
    }*/
}


////////////////////////////////////////
//
// PREVIEWS
//
////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewDownloaderContent() {
    val viewModel: DownloadViewModel = hiltViewModel<DownloadViewModel>()

    TheLabTheme {
        DownloaderContent(viewModel = viewModel)
    }
}