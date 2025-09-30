package com.riders.thelab.feature.palette

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.riders.thelab.core.data.local.model.compose.palette.PaletteUiState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.NoInternetConnection
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.snackbar.SnackbarVisualsCustom
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.loadImage
import com.riders.thelab.core.ui.compose.utils.toColor
import kotlinx.coroutines.launch
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@DevicePreviews
@Composable
fun PaletteLoader() {
    Box(
        modifier = Modifier
            .size(72.dp)
            .zIndex(5f),
        contentAlignment = Alignment.Center
    ) {
        Lottie(
            modifier = Modifier.fillMaxSize(),
            url = "https://assets2.lottiefiles.com/packages/lf20_kk62um5v.json"
        )
    }
}

@Composable
fun PaletteWithInternetConnection(
    theme: AppTheme,
    darkTheme: Boolean,
) {

}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PaletteContent(
    theme: AppTheme,
    darkTheme: Boolean,
    hasInternetConnection: Boolean,
    paletteUiState: PaletteUiState,
    paletteNameList: List<String>,
    onRefreshedClicked: () -> Unit,
    isRefreshing: Boolean
) {
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyGridState()

    val paletteColorList = remember { mutableStateOf(listOf<Int?>()) }

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
                                modifier = Modifier.padding(bottom = 8.dp),
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
                                            modifier = Modifier.fillMaxSize(),
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
                SnackbarHost(hostState = snackBarHostState) {
                    it.visuals.message
                }
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
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        when (paletteUiState) {
                            is PaletteUiState.Loading -> {
                                LabLoader(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(72.dp)
                                )
                            }

                            else -> {
                                val painter = rememberAsyncImagePainter(
                                    model = ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(if (paletteUiState is PaletteUiState.Success) paletteUiState.fetchedImage else com.riders.thelab.core.ui.R.drawable.logo_colors)
                                        .apply {
                                            crossfade(true)
                                            allowHardware(false)
                                            //transformations(RoundedCornersTransformation(32.dp.value))
                                        }
                                        .build(),
                                    placeholder = painterResource(com.riders.thelab.core.ui.R.drawable.logo_colors),
                                )
                                val state: AsyncImagePainter.State by painter.state.collectAsStateWithLifecycle()

                                LazyVerticalGrid(
                                    modifier = Modifier.size(
                                        width = this.maxWidth,
                                        height = this.maxHeight
                                    ),
                                    state = lazyState,
                                    horizontalArrangement = Arrangement.Center,
                                    verticalArrangement = Arrangement.spacedBy(24.dp),
                                    columns = GridCells.Fixed(2)
                                ) {
                                    // Image Container
                                    item(span = {
                                        // Replace "maxCurrentLineSpan" with the number of spans this item should take.
                                        // Use "maxCurrentLineSpan" if you want to take full width.
                                        GridItemSpan(maxCurrentLineSpan)
                                    }) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            val imageShape = RoundedCornerShape(
                                                topStart = 0.dp,
                                                topEnd = 0.dp,
                                                bottomStart = 12.dp,
                                                bottomEnd = 12.dp
                                            )

                                            // Image Container
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = imageShape
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(0.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Image(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(dimensionResource(id = com.riders.thelab.core.ui.R.dimen.card_image_custom_max_height))
                                                            .clip(imageShape),
                                                        painter = painter,
                                                        contentDescription = "palette image wth coil",
                                                        contentScale = ContentScale.Crop,
                                                    )
                                                    when (state) {
                                                        is AsyncImagePainter.State.Loading -> {
                                                            Timber.i("state is AsyncImagePainter.State.Loading")
                                                            LabLoader(modifier = Modifier.size(56.dp))
                                                        }

                                                        is AsyncImagePainter.State.Success -> {
                                                            Timber.d("state is AsyncImagePainter.State.Success")


                                                            LaunchedEffect(key1 = painter) {
                                                                scope.launch {

                                                                    val palette = Palette
                                                                        .from(painter.loadImage())
                                                                        .generate()

                                                                    ////////////////

                                                                    paletteColorList.value =
                                                                        generatePalette(palette)
                                                                }
                                                            }
                                                        }

                                                        is AsyncImagePainter.State.Error -> {
                                                            Timber.e("Recomposition | AsyncImagePainter.State.Error | ${(state as AsyncImagePainter.State.Error).result.throwable.message}")
                                                        }

                                                        else -> {
                                                            Timber.e("else branch")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Image Palette grid content
                                    itemsIndexed(paletteColorList.value) { index, item ->
                                        PaletteItem(
                                            text = paletteNameList[index],
                                            color = item?.let { Color(it) })
                                    }
                                }
                            }
                        }
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

    LaunchedEffect(paletteColorList) {
        if (paletteColorList.value.isEmpty()) {
            return@LaunchedEffect
        }

        navigationColor = paletteColorList.value.last()?.toColor()
            ?: paletteColorList.value[2]?.toColor()
                    ?: Color.White
        gradientStartColor = paletteColorList.value[1]?.toColor() ?: Color.Black
    }
}


fun generatePalette(palette: Palette): List<Int?> {
    Timber.d("Generate Palette")

    return listOf(
        palette.vibrantSwatch?.rgb,
        palette.darkVibrantSwatch?.rgb,
        palette.lightVibrantSwatch?.rgb,
        palette.mutedSwatch?.rgb,
        palette.darkMutedSwatch?.rgb,
        palette.lightMutedSwatch?.rgb,
    )
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewPaletteContentWithoutInternet(@PreviewParameter(PreviewProvider::class) palette: PaletteUiState) {

    val paletteNameList = listOf(
        "Vibrant",
        "Vibrant Dark",
        "Vibrant Light",
        "Muted",
        "Muted Dark",
        "Light Muted"
    )

    TheLabTheme(theme = AppTheme.Default) {
        PaletteContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = false,
            palette,
            paletteNameList,
            {},
            true
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewPaletteContent(@PreviewParameter(PreviewProvider::class) palette: PaletteUiState) {

    val paletteNameList = listOf(
        "Vibrant",
        "Vibrant Dark",
        "Vibrant Light",
        "Muted",
        "Muted Dark",
        "Light Muted"
    )

    TheLabTheme(theme = AppTheme.Default) {
        PaletteContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = true,
            palette,
            paletteNameList,
            {},
            true
        )
    }
}
