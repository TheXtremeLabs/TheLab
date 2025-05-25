package com.riders.thelab.core.ui.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.content.res.ResourcesCompat
import coil.compose.AsyncImagePainter
import java.nio.ByteBuffer

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density


/////////////////////////////////////////////////////
// Context
/////////////////////////////////////////////////////
fun Context.getDrawableByName(imageResName: String): Drawable? = ResourcesCompat.getDrawable(
    this.resources,
    this.resources.getIdentifier(imageResName, "drawable", this.packageName),
    this.theme
)


/////////////////////////////////////////////////////
// Bitmap
/////////////////////////////////////////////////////
/**
 * Convert bitmap to byte array using ByteBuffer.
 */
fun Bitmap.convertToByteArray(): ByteArray {
    // minimum number of bytes that can be used to store this bitmap's pixels
    val size = this.byteCount

    // allocate new instances which will hold bitmap
    val buffer = ByteBuffer.allocate(size)
    val bytes = ByteArray(size)

    //copy the bitmap's pixels into the specified buffer
    this.copyPixelsToBuffer(buffer)

    // rewinds buffer (buffer position is set to zero and the mark is discarded)
    buffer.rewind()

    // transfer bytes from buffer into the given destination array
    buffer.get(bytes)

    // return bitmap pixels
    return bytes
}

fun Bitmap.encodeToBase64(): String = Base64.encodeToString(
    this.convertToByteArray(),
    Base64.DEFAULT
)

fun String.decodeBase64(): Bitmap = Base64
    .decode(this, 0)
    .run { BitmapFactory.decodeByteArray(this, 0, this.size) }

/////////////////////////////////////////////////////
// Glide Image Loader
/////////////////////////////////////////////////////
// painter.loadImage() -> Drawable
suspend fun AsyncImagePainter.loadImage(): Drawable =
    imageLoader
        .execute(request)
        .drawable!!