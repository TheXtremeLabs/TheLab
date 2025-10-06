package com.riders.thelab.core.ui.compose.component.image

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.size.Scale
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.get
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.utils.generatePalette
import kotlinx.coroutines.launch
import kotools.types.text.NotBlankString
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun CoilImage(
    imageUrl: NotBlankString,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorResId: Int = R.drawable.logo_colors
) {
    val context = LocalContext.current

    val painter = getCoilAsyncImagePainter(
            context = context,
            dataUrl = imageUrl.toString(),
            isSvg = false,
            scale = Scale.FILL,
            placeholderResId = com.riders.thelab.core.ui.R.drawable.logo_colors
        )

    val painterState: AsyncImagePainter.State by painter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.then(Modifier.semantics { "coil_image_${imageUrl.toString()}" }),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = painterState,
            label = "coil image animated content for ${imageUrl.toString()}"
        ) { targetState: AsyncImagePainter.State ->
            when (targetState) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Success -> {
                    val painter = remember { targetState.get(context) }
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painter,
                        contentDescription = contentDescription ?: "coil_image_composable",
                        contentScale = contentScale
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(errorResId),
                        contentDescription = "coil_error_image",
                        contentScale = contentScale
                    )
                }

                else -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun CoilImage(
    painterState: AsyncImagePainter.State,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorResId: Int = R.drawable.logo_colors
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (painterState) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator()
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterState.painter,
                    contentDescription = contentDescription ?: "coil_image_composable",
                    contentScale = contentScale
                )
            }

            is AsyncImagePainter.State.Error -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(errorResId),
                    contentDescription = "coil_error_image",
                    contentScale = contentScale
                )
            }

            else -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun CoilImageWithPalette(
    painterState: AsyncImagePainter.State,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorResId: Int = R.drawable.logo_colors,
    onPaletteComplete: (HashMap<String, Int?>) -> Unit
) {

    val scope = rememberCoroutineScope()
    var generatedPalette: HashMap<String, Int?>? = null

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (painterState) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator()
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterState.painter,
                    contentDescription = contentDescription ?: "coil_image_composable",
                    contentScale = contentScale
                )

                LaunchedEffect(key1 = painterState.painter) {
                    scope.launch {
                        generatedPalette = generatePalette(painterState).also { palette ->
                            Timber.d("Recomposition | painter | generatedPalette | ${palette.map { it.value }}")
                        }

                        onPaletteComplete(generatedPalette)
                    }
                }
            }

            is AsyncImagePainter.State.Error -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(errorResId),
                    contentDescription = "coil_error_image",
                    contentScale = contentScale
                )
            }

            else -> {
                CircularProgressIndicator()
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
private fun PreviewCoilImage() {
    TheLabTheme(theme = AppTheme.Default) {
        CoilImage(
            painterState = AsyncImagePainter.State.Loading(null),
            errorResId = R.drawable.logo_colors
        )
    }
}