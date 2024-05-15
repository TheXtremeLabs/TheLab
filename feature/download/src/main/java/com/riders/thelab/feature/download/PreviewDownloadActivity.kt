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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.compose.Download
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography

////////////////////////////////////////
//
// COMPOSE
//
////////////////////////////////////////
@Composable
fun Header(text: String, isButtonEnabled: Boolean, onButtonClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.placeholder_welcome_to_download),
            style = Typography.bodyLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(2f),
                text = stringResource(id = R.string.placeholder_download_message),
            )

            Button(
                modifier = Modifier.weight(1f),
                onClick = onButtonClicked,
                enabled = isButtonEnabled
            ) {
                Text(text = text, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloaderContent(
    downloadListState: List<Download>,
    buttonText: String,
    isButtonEnabled: Boolean,
    onButtonClicked: () -> Unit
) {

    val lazyListState = rememberLazyListState()

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
                Header(
                    text = buttonText,
                    isButtonEnabled = isButtonEnabled,
                    onButtonClicked = onButtonClicked
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = downloadListState,
                        key = { _, item -> item.id }
                    ) { _, item ->
                        DownloadItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .animateItem(
                                    fadeInSpec = tween(300),
                                    fadeOutSpec = tween(300),
                                    placementSpec = tween(
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
private fun PreviewHeader() {
    TheLabTheme {
        Header("Launch Download", true) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewDownloaderContent(@PreviewParameter(PreviewListProvider::class) itemList: List<Download>) {
    TheLabTheme {
        DownloaderContent(itemList, "Launch Download", true) {}
    }
}