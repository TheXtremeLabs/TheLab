package com.riders.thelab.feature.palette

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import com.riders.thelab.core.data.local.model.compose.palette.PaletteUiState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.component.NoInternetConnection
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.snackbar.SnackbarVisualsCustom
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.compose.utils.loadImage
import com.riders.thelab.core.ui.compose.utils.toColor
import com.riders.thelab.core.ui.utils.generatePalette
import kotlinx.coroutines.launch
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@SuppressLint("MutableCollectionMutableState")
@Composable
fun PaletteSuccess(
    fetchedImageUrl: String,
    onPaletteComplete: (HashMap<String, Int?>) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyGridState()

    // Image Container
    val imageShape = remember {
        RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        )
    }

    var generatedPalette: HashMap<String, Int?>? by remember { mutableStateOf(null) }

    val painter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = fetchedImageUrl,
        isSvg = false,
        placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors,
    )
    val painterState: AsyncImagePainter.State by painter.state.collectAsStateWithLifecycle()

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .size(this.maxWidth, this.maxHeight)
                .verticalScroll(rememberScrollState())
        ) {
            // Image Container
            Card(
                modifier = Modifier
                    .semantics {
                        contentDescription = "palette image card container "
                        testTag = "palette_image_card"
                    }
                    .fillMaxWidth()
                    .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_custom_max_height)),
                shape = imageShape
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (painterState) {
                        is AsyncImagePainter.State.Loading -> {
                            Timber.i("state is AsyncImagePainter.State.Loading")
                            LabLoader(modifier = Modifier.size(56.dp))
                        }

                        is AsyncImagePainter.State.Success -> {
                            Timber.d("state is AsyncImagePainter.State.Success")

                            Image(
                                modifier = Modifier
                                    .clip(imageShape)
                                    .fillMaxSize(),
                                painter = painter,
                                contentDescription = "palette image wth coil",
                                contentScale = ContentScale.Crop,
                            )

                            LaunchedEffect(key1 = painterState) {
                                scope.launch {
                                    val bitmap: Bitmap = painterState.loadImage() ?: return@launch
                                    Timber.w("Recomposition | AsyncImagePainter.State.Success | process palette...")
                                    generatedPalette = generatePalette(bitmap)
                                }
                            }
                        }

                        is AsyncImagePainter.State.Error -> {
                            Timber.e("Recomposition | AsyncImagePainter.State.Error | ${(painterState as AsyncImagePainter.State.Error).result.throwable.message}")
                        }

                        else -> {
                            Timber.e("else branch")
                        }
                    }
                }
            }

            AnimatedContent(targetState = null != generatedPalette) { targetState ->
                if (!targetState) {
                    LabLoader(modifier = Modifier.size(56.dp))
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp),
                        state = lazyState,
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        columns = GridCells.Fixed(2)
                    ) {
                        // Image Palette grid content
                        itemsIndexed(
                            generatedPalette!!.toList()
                        ) { index, item ->
                            val paletteKey = remember { generatedPalette?.keys?.toList()[index] }
                                .also { Timber.d("Recomposition | palette key : $it") }

                            paletteKey?.let {
                                PaletteItem(
                                    text = it,
                                    color = generatedPalette?.get(it)?.toColor()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    // LaunchedEffect(painterState) { }

    LaunchedEffect(generatedPalette) {
        if (null == generatedPalette) {
            Timber.e("LaunchedEffect | generatedPalette | value is null")
            return@LaunchedEffect
        }

        onPaletteComplete.invoke(generatedPalette!!)
    }

}


@Composable
fun PaletteError(onRetryClicked: () -> Unit) {
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PaletteContent(
    theme: AppTheme,
    darkTheme: Boolean,
    hasInternetConnection: Boolean,
    paletteUiState: PaletteUiState,
    onRefreshedClicked: () -> Unit,
    isRefreshing: Boolean
) {
    val snackBarHostState = SnackbarHostState()
    val snackBarVisualsCustom = remember { mutableStateOf(SnackbarVisualsCustom()) }

    var navigationColor by remember { mutableStateOf(Color.Black) }
    var gradientStartColor by remember { mutableStateOf(Color.Black) }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    modifier = Modifier.semantics {
                        contentDescription = "palette top app bar"
                        testTag = "palette_top_app_bar"
                    },
                    toolbarSize = ToolbarSize.SMALL,
                    title = stringResource(id = R.string.activity_title_palette),
                    toolbarHeight = 72.dp,
                    withGradientBackground = true,
                    gradientStartColor = gradientStartColor,
                    navigationIconColor = navigationColor,
                    actions = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            AnimatedContent(
                                modifier = Modifier.padding(vertical = 8.dp),
                                targetState = isRefreshing,
                                transitionSpec = { fadeIn() + slideInVertically() togetherWith slideOutVertically() + fadeOut() },
                                label = "loading animation content"
                            ) { targetState ->
                                if (!targetState) {
                                    Box(
                                        modifier = Modifier.size(40.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(
                                            modifier = Modifier
                                                .semantics {
                                                    contentDescription = "refresh button"
                                                    testTag = "refresh_button"
                                                }
                                                .fillMaxSize(),
                                            onClick = onRefreshedClicked,
                                            enabled = hasInternetConnection
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(4.dp),
                                                imageVector = Icons.Filled.Sync,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier.size(40.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .semantics {
                                                    contentDescription =
                                                        "refresh circular progress indicator"
                                                    testTag = "refresh_circular_progress"
                                                }

                                                .fillMaxSize()
                                                .padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    })
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) { it.visuals.message }
            }
        ) { contentPadding ->
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                targetState = hasInternetConnection,
                transitionSpec = { fadeIn() + scaleIn() togetherWith scaleOut() + fadeOut() },
                label = "loading animation content"
            ) { targetState: Boolean ->
                if (!targetState) {
                    NoInternetConnection(
                        theme = theme,
                        darkTheme = darkTheme,
                        modifier = Modifier
                            .semantics {
                                contentDescription = "no internet connection screen content"
                                testTag = "no_internet_connection_screen"
                            }
                            .fillMaxSize()
                            .padding(contentPadding),
                        message = "${stringResource(R.string.network_status_disconnected)}\n${
                            stringResource(
                                R.string.network_check_connection
                            )
                        }",
                        errorImageResId = null,
                        action = onRefreshedClicked
                    )
                } else {
                    when (paletteUiState) {
                        is PaletteUiState.Loading -> LabLoader(
                            modifier = Modifier
                                .semantics {
                                    contentDescription = "lab loading animation content"
                                    testTag = "lab_loader_animation"
                                }
                                .size(56.dp))

                        is PaletteUiState.Success -> PaletteSuccess(fetchedImageUrl = paletteUiState.fetchedImage) { palette ->
                            Timber.d("Recomposition | generated palette $palette")
                            navigationColor = palette["Light Vibrant"]?.toColor() ?: Color.White
                            gradientStartColor = palette["Dark Vibrant"]?.toColor() ?: Color.Black
                        }

                        is PaletteUiState.Error -> PaletteError { }
                    }
                }
            }
        }
    }


    LaunchedEffect(paletteUiState) {
        if (paletteUiState is PaletteUiState.Error) {
            Timber.e("LaunchedEffect | paletteUiState is PaletteUiState.Error | Error: ${paletteUiState.error}. Show snack bar")
            snackBarHostState.currentSnackbarData?.visuals.apply {
                snackBarVisualsCustom.value = SnackbarVisualsCustom(
                    message = paletteUiState.error,
                    duration = SnackbarDuration.Long
                )

                snackBarHostState.showSnackbar(snackBarVisualsCustom.value)
            }
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewPaletteContentWithoutInternet(@PreviewParameter(PreviewProvider::class) palette: PaletteUiState) {
    TheLabTheme(theme = AppTheme.Default) {
        PaletteContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = false,
            palette,
            {},
            true
        )
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewPaletteContent(@PreviewParameter(PreviewProvider::class) palette: PaletteUiState) {
    TheLabTheme(theme = AppTheme.Default) {
        PaletteContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = true,
            palette,
            {},
            true
        )
    }
}
