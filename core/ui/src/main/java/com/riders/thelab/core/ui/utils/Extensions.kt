package com.riders.thelab.core.ui.utils

import android.graphics.drawable.Drawable
import coil.compose.AsyncImagePainter


/////////////////////////////////////////////////////
// Glide Image Loader
/////////////////////////////////////////////////////

// painter.loadImage() -> Drawable
suspend fun AsyncImagePainter.loadImage(): Drawable =
    imageLoader
        .execute(request)
        .drawable!!