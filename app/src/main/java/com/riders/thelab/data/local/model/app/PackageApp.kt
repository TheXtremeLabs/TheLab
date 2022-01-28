package com.riders.thelab.data.local.model.app

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Parcel
import android.os.Parcelable
import com.riders.thelab.TheLabApplication
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

@Parcelize
data class PackageApp(
    var name: String,
    var drawableIcon: @RawValue Drawable?,
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
), Parcelable, Serializable {

    private companion object : Parceler<PackageApp> {

        override fun create(parcel: Parcel): PackageApp {
            /* Source : https://stackoverflow.com/questions/9033764/how-to-add-a-drawable-object-to-a-parcel-object-in-android*/
            var drawable: BitmapDrawable?

            val bitmap = parcel.readValue(Bitmap::class.java.classLoader)!! as Bitmap
            if (null != bitmap) {
                drawable =
                    BitmapDrawable(TheLabApplication.getInstance().getContext().resources, bitmap)
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
    }

    // From Packages
    @IgnoredOnParcel
    var paName: String? = null

    @IgnoredOnParcel
    var paDrawableIcon: Drawable? = null

    @IgnoredOnParcel
    var paVersion: String? = null

    @IgnoredOnParcel
    var paPackageName: String? = null
}
