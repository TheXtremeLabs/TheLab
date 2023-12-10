package com.riders.thelab.ui.palette

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.previewprovider.TextContentPreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.loadImage
import com.riders.thelab.feature.weather.core.component.TheLabTopAppBar
import kotlinx.coroutines.launch
import timber.log.Timber


@DevicePreviews
@Composable
fun PaletteItem(
    index: Int = 0,
    @PreviewParameter(TextContentPreviewProvider::class) text: String,
    color: Color? = MaterialTheme.colorScheme.background
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.weight(2f), text = text)
            Card(
                modifier = Modifier
                    .size(36.dp),
                colors = CardDefaults.cardColors(
                    containerColor = color ?: MaterialTheme.colorScheme.background
                ),
                shape = CircleShape
            ) {

            }
        }
    }
}

@Composable
fun PaletteContent(viewModel: PaletteViewModel) {
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyGridState()

    val imageUrl by viewModel.imageUrl.collectAsState()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .apply {
                crossfade(true)
                allowHardware(false)
                //transformations(RoundedCornersTransformation(32.dp.value))
            }
            .build(),
        placeholder = painterResource(R.drawable.logo_colors),
    )
    val state = painter.state

    val paletteColorList = remember { mutableStateOf(listOf<Int?>()) }

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TheLabTopAppBar(stringResource(id = R.string.activity_title_palette)) }
        ) { contentPadding ->

            LazyVerticalGrid(
                state = lazyState,
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                columns = GridCells.Fixed(2)
            ) {
                item(span = {
                    // Replace "maxCurrentLineSpan" with the number of spans this item should take.
                    // Use "maxCurrentLineSpan" if you want to take full width.
                    GridItemSpan(maxCurrentLineSpan)
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(contentPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
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
                                        .clip(RoundedCornerShape(12.dp)),
                                    painter = painter,
                                    contentDescription = "palette image wth coil",
                                    contentScale = ContentScale.Crop,
                                )
                                when (state) {
                                    is AsyncImagePainter.State.Loading -> {
                                        Timber.i("state is AsyncImagePainter.State.Loading")
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .scale(0.5f)
                                                .align(Alignment.CenterHorizontally)
                                        )
                                    }

                                    is AsyncImagePainter.State.Success -> {
                                        Timber.d("state is AsyncImagePainter.State.Success")
                                        LaunchedEffect(key1 = painter) {
                                            scope.launch {
                                                val image = painter.loadImage()

                                                val palette = Palette.from(
                                                    image.toBitmap(
                                                        image.intrinsicWidth,
                                                        image.intrinsicHeight
                                                    )
                                                ).generate()

                                                ////////////////

                                                paletteColorList.value = generatePalette(palette)
                                            }
                                        }
                                    }

                                    is AsyncImagePainter.State.Error -> {
                                        Timber.e("state is AsyncImagePainter.State.Error | ${state.result}")
                                    }

                                    else -> {
                                        Timber.e("else branch")
                                    }
                                }
                            }
                        }
                    }
                }

                itemsIndexed(paletteColorList.value) { index, item ->
                    PaletteItem(
                        index,
                        text = viewModel.paletteNameList[index],
                        color = item?.let { Color(it) })
                }
            }
        }
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