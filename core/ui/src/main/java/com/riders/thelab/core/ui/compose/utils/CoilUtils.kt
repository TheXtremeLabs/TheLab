package com.riders.thelab.core.ui.compose.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.riders.thelab.core.ui.R
import timber.log.Timber

/*
 * https://www.sinasamaki.com/loading-images-using-coil-in-jetpack-compose/
 */
@Composable
fun getCoilImageRequest(context: Context, dataUrl: String): ImageRequest = ImageRequest
    .Builder(context)
    .data(dataUrl)
    .apply {
        crossfade(true)
        allowHardware(false)
        //transformations(RoundedCornersTransformation(32.dp.value))
        size(Size.ORIGINAL)
        scale(Scale.FIT)
    }
    .build()

@Composable
fun getCoilAsyncImagePainter(context: Context, dataUrl: String): AsyncImagePainter =
    rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(context)
            .data(dataUrl)
            .apply {
                crossfade(true)
                allowHardware(false)
                //transformations(RoundedCornersTransformation(32.dp.value))
                size(Size.ORIGINAL)
                scale(Scale.FIT)
                networkCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
            .build(),
        placeholder = painterResource(R.drawable.logo_colors),
        onLoading = {
            Timber.i("rememberAsyncImagePainter | Loading Image... | url: $dataUrl")
        },
        onSuccess = {
            Timber.d("rememberAsyncImagePainter | Image successfully loaded")
        },
        onError = {
            Timber.e("rememberAsyncImagePainter | Error while loading Image")
        }
    )