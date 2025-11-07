package com.riders.thelab.core.ui.compose.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.asPainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.size.Scale
import coil3.size.Size
import coil3.svg.SvgDecoder
import coil3.toBitmap
import com.riders.thelab.core.ui.R
import timber.log.Timber

/*
 * https://www.sinasamaki.com/loading-images-using-coil-in-jetpack-compose/
 */
// @Composable
@Stable
@Composable
private fun getCoilImageRequest(
    context: Context,
    data: Any,
    isSvg: Boolean = false,
    size: Size? = null,
    scale: Scale? = null,
    isCaching: Boolean = true
): ImageRequest = remember(data) {
    ImageRequest
        .Builder(context)
        .apply {
            Timber.d("getCoilImageRequest() | data : $data, size : $size, scale : $scale")

            data(data)

            if (isSvg) {
                decoderFactory(SvgDecoder.Factory())
            }

            crossfade(true)
            allowHardware(false)
            //transformations(RoundedCornersTransformation(32.dp.value))

            size(size ?: Size.ORIGINAL)
            scale(scale ?: Scale.FIT)

            if (isCaching) {
                networkCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
        }
        .build()
}

@Stable
@Composable
private fun getCoilImageRequest(
    context: Context,
    dataUrl: String,
    isSvg: Boolean = false,
    size: Size? = null,
    scale: Scale? = null,
    isCaching: Boolean = true
): ImageRequest = getCoilImageRequest(
    context = context,
    data = dataUrl,
    isSvg = isSvg,
    size = size,
    scale = scale,
    isCaching = isCaching
)

@Composable
fun getCoilAsyncImagePainter(
    context: Context,
    data: Any,
    isSvg: Boolean = false,
    size: Size? = null,
    scale: Scale? = null,
    @DrawableRes placeholderResId: Int = R.drawable.logo_colors
): AsyncImagePainter = rememberAsyncImagePainter(
    model = getCoilImageRequest(
        context = context,
        data = data,
        isSvg = isSvg,
        size = size,
        scale = scale
    ),
    placeholder = painterResource(placeholderResId),
    onLoading = {
        Timber.i("getCoilAsyncImagePainter() | rememberAsyncImagePainter | Loading Image... | data: $data")
    },
    onSuccess = {
        Timber.d("getCoilAsyncImagePainter() | rememberAsyncImagePainter | Image successfully loaded")
    },
    onError = {
        Timber.e("getCoilAsyncImagePainter() | rememberAsyncImagePainter | Error while loading Image")
    }
)

@Composable
fun getCoilAsyncImagePainter(
    context: Context,
    dataUrl: String,
    isSvg: Boolean = false,
    size: Size? = null,
    scale: Scale? = null,
    @DrawableRes placeholderResId: Int = R.drawable.logo_colors
): AsyncImagePainter = getCoilAsyncImagePainter(
    context = context,
    data = dataUrl,
    isSvg = isSvg,
    size = size,
    scale = scale,
    placeholderResId = placeholderResId
)

@Composable
fun getCoilAsyncImagePainter(
    context: Context,
    dataUrl: String,
    isSvg: Boolean = false,
    size: Size? = null,
    scale: Scale? = null,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
): AsyncImagePainter = rememberAsyncImagePainter(
    model = getCoilImageRequest(
        context = context,
        dataUrl = dataUrl,
        isSvg = isSvg,
        size = size,
        scale = scale
    ),
    onState = onState
)

@Composable
fun AsyncImagePainter.getPainterState(): State<AsyncImagePainter.State> =
    this.state.collectAsStateWithLifecycle()

fun AsyncImagePainter.State.get(context: Context): Painter? = when (this) {
    is AsyncImagePainter.State.Success -> this.result.image.asPainter(context = context)
    else -> null
}


fun AsyncImagePainter.State.loadImage(): Bitmap? = when (this) {
    is AsyncImagePainter.State.Success -> this.result.image.toBitmap()
    else -> null
}


fun AsyncImagePainter.State.Success.get(context: Context): Painter =
    this.result.image.asPainter(context = context)


fun AsyncImagePainter.State.Success.loadImage(): Bitmap = this.result.image.toBitmap()