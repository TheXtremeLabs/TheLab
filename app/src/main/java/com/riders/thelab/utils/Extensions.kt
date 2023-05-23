package com.riders.thelab.utils

import android.graphics.drawable.Drawable
import coil.compose.AsyncImagePainter

// painter.loadImage() -> Drawable
suspend fun AsyncImagePainter.loadImage(): Drawable =
    imageLoader
        .execute(request)
        .drawable!!