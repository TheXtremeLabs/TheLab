package com.riders.thelab.utils

import android.graphics.drawable.Drawable
import coil.compose.AsyncImagePainter
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

// painter.loadImage() -> Drawable
fun AsyncImagePainter.loadImage(): Drawable =
    runBlocking {
        imageLoader
            .execute(request)
            .drawable!!
    }

fun AsyncImagePainter.loadImage(coroutineContext: CoroutineContext): Drawable =
    runBlocking(coroutineContext) {
        imageLoader
            .execute(request)
            .drawable!!
    }