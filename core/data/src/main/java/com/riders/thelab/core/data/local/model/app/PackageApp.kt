package com.riders.thelab.core.data.local.model.app

import android.content.Context
import android.graphics.drawable.Drawable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual

//@Parcelize
@kotlinx.serialization.Serializable
data class PackageApp(
    @Contextual val context: Context,
    var name: String,
    @Contextual var drawableIcon: @RawValue Drawable?,
    var version: String,
    var packageName: String
) : App(
    appName = name,
    appDrawableIcon = drawableIcon,
    appVersion = version,
    appPackageName = packageName,
    id = -1L,
    appTitle = null,
    appDescription = null,
    appActivity = null,
    appDate = null
), /*Parcelable,*/ java.io.Serializable {

    /*@Contextual
    private var _context: Context ?= null

    init {
        this._context = context
    }
    private companion object : Parceler<PackageApp> {

        override fun create(parcel: Parcel): PackageApp {
            *//* Source : https://stackoverflow.com/questions/9033764/how-to-add-a-drawable-object-to-a-parcel-object-in-android*//*
            var drawable: BitmapDrawable?

            val bitmap = parcel.readValue(Bitmap::class.java.classLoader)!! as Bitmap
            if (null != bitmap) {
                drawable =
                    BitmapDrawable(context.resources, bitmap)
            } else {
                drawable = null
            }

            // Custom read implementation
            return PackageApp(
                parcel.readString()!!,
                drawable as Drawable,
                parcel.readString()!!,
                parcel.readString()!!
            )
        }

        override fun PackageApp.write(parcel: Parcel, flags: Int) {
            // Custom write implementation
            var bitmap: Bitmap? = null

            if (appDrawableIcon is BitmapDrawable) {
                bitmap = (appDrawableIcon as BitmapDrawable).bitmap as Bitmap
            } else if (appDrawableIcon is VectorDrawable) {
                bitmap = getBitmap(appDrawableIcon as VectorDrawable)!!
            }
            parcel.writeParcelable(bitmap, flags)

            appName.let { parcel.writeString(it) }
            parcel.writeString(appVersion)
            parcel.writeString(appPackageName)
        }
    }*/

    // From Packages
    @IgnoredOnParcel
    var paName: String? = null

    @Contextual
    @IgnoredOnParcel
    var paDrawableIcon: Drawable? = null

    @IgnoredOnParcel
    var paVersion: String? = null

    @IgnoredOnParcel
    var paPackageName: String? = null
}
