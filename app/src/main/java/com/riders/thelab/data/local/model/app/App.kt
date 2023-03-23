package com.riders.thelab.data.local.model.app

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
open class App(
    // Packages
    var appName: String?, var appDrawableIcon: @RawValue Drawable?,
    var appVersion: String?, var appPackageName: String?,

    // Activities
    var id: Long = -1L,
    var appTitle: String?, var appDescription: String?,
    var appActivity: Class<out Activity?>?,
    var appDate: String?
) : Parcelable {
    constructor() : this(
        "", null,
        "", "",
        -1L, "", "", null, ""
    )

    fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }
}
