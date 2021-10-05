package com.riders.thelab.data.local.model.app

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
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
data class App(
    var id: Long,
    var appName: String, var appDrawableIcon: @RawValue Drawable,
    var appVersion: String, var appPackageName: String,
    var appTitle: String, var appDescription: String,
    var appActivity: Class<out Activity?>?
) : Parcelable, Serializable {

    private companion object : Parceler<App> {

        override fun create(parcel: Parcel): App {
            var drawable: BitmapDrawable?

            val bitmap = parcel.readValue(Bitmap::class.java.classLoader)!! as Bitmap
            if (null != bitmap) {
                drawable =
                    BitmapDrawable(TheLabApplication.getInstance().getContext().resources, bitmap)
            } else {
                drawable = null
            }

            // Custom read implementation
            return App(
                parcel.readLong(),
                parcel.readString()!!,
                parcel.readString()!!,
                drawable as Drawable,
                parcel.readValue(Activity::class.java.classLoader)!! as Class<out Activity>
            )
        }

        override fun App.write(parcel: Parcel, flags: Int) {
            // Custom write implementation
            parcel.writeLong(id)
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
            parcel.writeString(appTitle)
            parcel.writeString(appDescription)
            parcel.writeValue(appActivity)
        }

    }

    // From Packages
    @IgnoredOnParcel
    private var name: String? = null

    @IgnoredOnParcel
    private var drawableIcon: Drawable? = null

    @IgnoredOnParcel
    private var version: String? = null

    @IgnoredOnParcel
    private var packageName: String? = null

    // From activities
    @IgnoredOnParcel
    private var title: String? = null

    @IgnoredOnParcel
    private var description: String? = null

    @IgnoredOnParcel
    private var activity: Class<out Activity?>? = null

    constructor(
        name: String,
        drawableIcon: Drawable,
        version: String,
        packageName: String
    ) : this(
        -1L,
        name, drawableIcon, version, packageName,
        "", "", null
    ) {
        this.name = name
        this.drawableIcon = drawableIcon
        this.version = version
        this.packageName = packageName
    }

    constructor(
        id: Long,
        title: String,
        description: String,
        icon: Drawable,
        activity: Class<out Activity>?
    ) : this(
        id,
        "", icon, "", "",
        title, description, activity
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.drawableIcon = icon
        this.activity = activity
    }

    override fun toString(): String {
        return "App(id=$id, appName='$appName', appDrawableIcon=$appDrawableIcon, appVersion='$appVersion', appPackageName='$appPackageName', appTitle='$appTitle', appDescription='$appDescription', appActivity=$appActivity, name=$name, drawableIcon=$drawableIcon, version=$version, packageName=$packageName, title=$title, description=$description, activity=$activity)"
    }

    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
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
