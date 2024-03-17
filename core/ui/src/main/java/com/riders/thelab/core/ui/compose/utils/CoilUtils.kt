package com.riders.thelab.core.ui.compose.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import coil.util.DebugLogger
import com.riders.thelab.core.ui.R
import timber.log.Timber

/*
 * https://www.sinasamaki.com/loading-images-using-coil-in-jetpack-compose/
 */
@Composable
fun getCoilImageRequest(
    context: Context,
    dataUrl: String,
    size: Size? = null,
    scale: Scale? = null,
    isCaching: Boolean = true
): ImageRequest = ImageRequest
    .Builder(context)
    .data(dataUrl)
    .apply {
        Timber.d("getCoilImageRequest() | dataUrl : $dataUrl, size : $size, scale : $scale")

        crossfade(true)
        allowHardware(false)
        //transformations(RoundedCornersTransformation(32.dp.value))

        size(size ?: Size.ORIGINAL)
        scale(scale ?: Scale.FIT)

        if (isCaching) {
            networkCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }
    .build()

@Composable
fun getCoilAsyncImagePainter(
    context: Context,
    dataUrl: String,
    size: Size? = null,
    scale: Scale? = null,
    @DrawableRes placeholderResId: Int = R.drawable.logo_colors
): AsyncImagePainter =
    rememberAsyncImagePainter(
        model = getCoilImageRequest(
            context = context,
            dataUrl = dataUrl,
            size = size,
            scale = scale
        ),
        placeholder = painterResource(placeholderResId),
        onLoading = {
            Timber.i("getCoilAsyncImagePainter() | rememberAsyncImagePainter | Loading Image... | url: $dataUrl")
        },
        onSuccess = {
            Timber.d("getCoilAsyncImagePainter() | rememberAsyncImagePainter | Image successfully loaded")
        },
        onError = {
            Timber.e("getCoilAsyncImagePainter() | rememberAsyncImagePainter | Error while loading Image")
        },
        imageLoader = ImageLoader.Builder(context).logger(DebugLogger()).build()
    )