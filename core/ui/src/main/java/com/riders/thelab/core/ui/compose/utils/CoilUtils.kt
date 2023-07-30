package com.riders.thelab.core.compose.utils

import android.content.Context
import androidx.compose.runtime.Composable
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size

@Composable
fun getAsyncImagePainter(context: Context, dataUrl: String): AsyncImagePainter =
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
            }
            .build()
    )