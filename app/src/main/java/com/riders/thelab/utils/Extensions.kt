package com.riders.thelab.utils

import android.graphics.drawable.Drawable
import coil.compose.AsyncImagePainter
import kotlinx.coroutines.runBlocking

// painter.loadImage() -> Drawable
fun AsyncImagePainter.loadImage(): Drawable =
    runBlocking {
        imageLoader
            .execute(request)
            .drawable!!
    }