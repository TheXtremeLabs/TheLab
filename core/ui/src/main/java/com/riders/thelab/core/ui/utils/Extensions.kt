package com.riders.thelab.core.ui.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImagePainter
import com.riders.thelab.core.ui.compose.utils.loadImage
import timber.log.Timber
import java.nio.ByteBuffer
import java.util.concurrent.Executor

val Int.toDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.toPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.toPx get() = this * Resources.getSystem().displayMetrics.density


/////////////////////////////////////////////////////
// Context
/////////////////////////////////////////////////////
val Context.executor: Executor get() = ContextCompat.getMainExecutor(this)
fun Context.getDrawableByName(imageResName: String): Drawable? = ResourcesCompat.getDrawable(
    this.resources,
    this.resources.getIdentifier(imageResName, "drawable", this.packageName),
    this.theme
)

fun Context.getDrawableFromIntResource(resId: Int): Drawable =
    ContextCompat.getDrawable(this, resId)!!


/////////////////////////////////////////////////////
// Vector Drawable
/////////////////////////////////////////////////////
fun VectorDrawable.toBitmap(): Bitmap? {
    val bitmap = createBitmap(this.intrinsicWidth, this.intrinsicHeight)

    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}

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
// Palette
/////////////////////////////////////////////////////
fun generatePalette(painterState: AsyncImagePainter.State.Success) = Palette
    .from(painterState.loadImage())
    .generate()
    .run {
        getPalette(this)
    }


fun generatePalette(bitmap: Bitmap): HashMap<String, Int?> = Palette
    .from(bitmap)
    .generate()
    .run {
        getPalette(this)
    }

private fun getPalette(palette: Palette): HashMap<String, Int?> {
    Timber.d("Palette extensions | Generate Palette")

    val paletteMap: HashMap<String, Int?> = hashMapOf(
        "vibrant" to (palette.vibrantSwatch?.rgb ?: 0),
        "dark vibrant" to (palette.darkVibrantSwatch?.rgb ?: 0),
        "light vibrant" to (palette.lightVibrantSwatch?.rgb ?: 0),
        "muted" to (palette.mutedSwatch?.rgb ?: 0),
        "dark muted" to (palette.darkMutedSwatch?.rgb ?: 0),
        "light muted" to (palette.lightMutedSwatch?.rgb ?: 0)
    ).also { Timber.d("getPalette() | ${it.toString()}") } as HashMap<String, Int?>

    return paletteMap
}
