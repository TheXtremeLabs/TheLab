package com.riders.thelab.feature.musicrecognition.ui.acrcloud

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.music.MusicRecognitionModel
import com.riders.thelab.core.data.local.model.music.toModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.animatePlacement
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.utils.decodeBase64
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@DevicePreviews
@Composable
fun ACRCloudLibraryShimmerItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(36.dp)
                .shimmerLoading()
        )

        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerLoading()
            )
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerLoading()
            )
        }
    }
}

@Composable
fun ACRCloudLibraryItem(
    theme: AppTheme,
    darkTheme: Boolean,
    hasInternetConnection: Boolean,
    loaded: Boolean,
    item: MusicRecognitionModel,
    modifier: Modifier = Modifier,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val painter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = item.albumThumbUrl!!,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        })
    val state = painter.state

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        AnimatedContent(targetState = loaded) { targetState: Boolean ->
            if (!targetState) {
                ACRCloudLibraryShimmerItem()
            } else {
                Card(
                    modifier = modifier,
                    onClick = { uiEvent.invoke(UiEvent.OpenModelInSpotify(model = item)) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image
                        if (!hasInternetConnection) {
                            if (null != item.albumThumbBase64) {
                                Image(
                                    bitmap = item.albumThumbBase64!!.decodeBase64().asImageBitmap(),
                                    contentDescription = null
                                )
                            }
                        } else {
                            AnimatedContent(targetState = state) { targetState: AsyncImagePainter.State ->
                                when (targetState) {
                                    is AsyncImagePainter.State.Loading -> {
                                        LabLoader(modifier = Modifier.size(36.dp))
                                    }

                                    is AsyncImagePainter.State.Error -> {
                                        Icon(
                                            imageVector = Icons.Rounded.CloudOff,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    is AsyncImagePainter.State.Success -> {
                                        Image(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .size(64.dp),
                                            contentScale = ContentScale.Crop,
                                            painter = if (isError.not() && !LocalInspectionMode.current) {
                                                painter
                                            } else {
                                                painterResource(id = com.riders.thelab.core.ui.R.drawable.logo_colors)
                                            },
                                            // TODO b/226661685: Investigate using alt text of  image to populate content description
                                            // decorative image,
                                            contentDescription = null,
                                        )
                                    }

                                    else -> {
                                        Icon(
                                            imageVector = Icons.Rounded.CloudOff,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(2f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Text(text = item.title, fontWeight = FontWeight.W700, fontSize = 14.sp)
                            Text(text = item.artists, fontSize = 12.sp)
                        }

                        SpotifyIcon(modifier = Modifier.size(30.dp), darkTheme = darkTheme)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACRCloudLibraryContent(
    theme: AppTheme,
    darkTheme: Boolean,
    hasInternetConnection: Boolean,
    songs: List<MusicRecognitionModel>,
    uiEvent: (UiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    var loaded by remember { mutableStateOf(false) }
    val scrollToTopButton: Boolean by remember { derivedStateOf { 0 != lazyListState.firstVisibleItemIndex } }

    LaunchedEffect(loaded) {
        delay(500L)
        loaded = true
    }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .size(width = this.maxWidth, height = this.maxHeight)
                    .padding(top = 12.dp),
                state = lazyListState
            ) {
                itemsIndexed(
                    items = songs,
                    key = { _: Int, item: MusicRecognitionModel -> item._id }) { _, item ->
                    ACRCloudLibraryItem(
                        theme = theme,
                        darkTheme = darkTheme,
                        hasInternetConnection = hasInternetConnection,
                        modifier = Modifier.animatePlacement(),
                        loaded = loaded,
                        item = item,
                        uiEvent = uiEvent
                    )
                }

                item { Box(modifier = Modifier.height(TopAppBarDefaults.MediumAppBarExpandedHeight)) }
            }

            AnimatedVisibility(
                modifier = Modifier.padding(bottom = TopAppBarDefaults.MediumAppBarExpandedHeight + 24.dp),
                visible = scrollToTopButton
            ) {
                Button(
                    modifier = Modifier.clip(CircleShape),
                    onClick = { scope.launch { lazyListState.animateScrollToItem(0) } }
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = null)
                }
            }
        }
    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewACRCloudLibraryItemNotLoaded(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme, darkTheme = isSystemInDarkTheme()) {
        ACRCloudLibraryItem(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = true,
            loaded = false,
            item = Song.mock.toModel()
        ) { }
    }
}

@DevicePreviews
@Composable
private fun PreviewACRCloudLibraryItemLoaded(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme, darkTheme = isSystemInDarkTheme()) {
        ACRCloudLibraryItem(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = true,
            loaded = true,
            item = Song.mock.toModel()
        ) { }
    }
}

@DevicePreviews
@Composable
private fun PreviewACRCloudLibraryContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme, darkTheme = isSystemInDarkTheme()) {
        ACRCloudLibraryContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = true,
            songs = listOf(Song.mock.toModel())
        ) { }
    }
}