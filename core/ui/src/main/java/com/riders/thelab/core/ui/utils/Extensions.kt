package com.riders.thelab.core.ui.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import coil.compose.AsyncImagePainter

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density

/////////////////////////////////////////////////////
// Glide Image Loader
/////////////////////////////////////////////////////

// painter.loadImage() -> Drawable
suspend fun AsyncImagePainter.loadImage(): Drawable =
    imageLoader
        .execute(request)
        .drawable!!