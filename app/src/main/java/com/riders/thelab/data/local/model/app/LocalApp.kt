package com.riders.thelab.data.local.model.app

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.res.ResourcesCompat
import com.riders.thelab.TheLabApplication
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import timber.log.Timber
import java.io.Serializable
import kotlin.reflect.KClass

@Parcelize
data class LocalApp(
    val localId: Long,
    val localTitle: String,
    val localDescription: String,
    var localDrawableIcon: @RawValue Drawable?,
    val localActivity: Class<out Activity?>?,
    val localDate: String,
) : App(
    appName = null,
    appVersion = null,
    appPackageName = null,
    id = localId,
    appTitle = localTitle,
    appDescription = localDescription,
    appDrawableIcon = localDrawableIcon,
    appActivity = localActivity,
    appDate = localDate
), Parcelable, Serializable {

    companion object : Parceler<LocalApp> {

        override fun create(parcel: Parcel): LocalApp {
            /* Source : https://stackoverflow.com/questions/9033764/how-to-add-a-drawable-object-to-a-parcel-object-in-android*/
            val drawable: BitmapDrawable?

            val bitmap = parcel.readValue(Bitmap::class.java.classLoader)!! as Bitmap
            drawable = if (null != bitmap) {
                BitmapDrawable(TheLabApplication.getInstance().getContext().resources, bitmap)
            } else {
                null
            }

            // Custom read implementation
            return LocalApp(
                parcel.readLong(),
                parcel.readString()!!,
                parcel.readString()!!,
                drawable as Drawable,
                parcel.readValue(Activity::class.java.classLoader)!! as Class<Activity>,
                parcel.readString()!!
            )
        }

        override fun LocalApp.write(parcel: Parcel, flags: Int) {
            // Custom write implementation
            parcel.writeLong(id)
            var bitmap: Bitmap? = null

            if (localDrawableIcon is BitmapDrawable) {
                bitmap = (localDrawableIcon as BitmapDrawable).bitmap as Bitmap
            } else if (localDrawableIcon is VectorDrawable) {
                bitmap = getBitmap(localDrawableIcon as VectorDrawable)!!
            }
            parcel.writeParcelable(bitmap, flags)

            title.let { parcel.writeString(it) }
        }


        @JvmStatic
        fun getActivityClassObjectByName(activityName: String): Activity? {
            return try {

                // Get All activity list in the app and find by name
                val activityFound = TheLabApplication.getActivityPackageName(activityName)

                if (null == activityFound) {
                    Timber.e("No activity found for name : $activityName")
                    null
                } else {
//                    val myClass = Class.forName(activityFound)

                    val mObject: Class<out Activity> =
                        Class.forName(activityFound).newInstance() as Class<out Activity>

                    /*val types = arrayOf<Class<*>>(java.lang.Double.TYPE, this.javaClass)
                    val constructor: Constructor<Class<*>> =
                        myClass.getConstructor(*types) as Constructor<Class<*>>

                    val parameters = arrayOf<Any>(0, this)
                    val instanceOfMyClass: Any = constructor.newInstance(parameters)

                    instanceOfMyClass as Class<out Activity>*/
                    mObject.getClass() as Activity
                }

            } catch (exception: Exception) {
                exception.printStackTrace()
                null
            }
        }


        @JvmStatic
        fun getDrawableByName(imageResName: String): Drawable {
            val mAppContext = TheLabApplication.getInstance().applicationContext
            val mResources = mAppContext.resources

            return ResourcesCompat.getDrawable(
                mResources,
                mResources.getIdentifier(imageResName, "drawable", mAppContext.packageName),
                mAppContext.theme
            )!!
        }
    }

    // From activities
//    @IgnoredOnParcel
//    var id: Long = 0L

    @IgnoredOnParcel
    var title: String? = null

    @IgnoredOnParcel
    var description: String? = null

    @IgnoredOnParcel
    var activity: Class<out Activity?>? = null

    @IgnoredOnParcel
    var icon: String? = null

    @IgnoredOnParcel
    var date: String? = null

}

fun <T : Any> T.getClass(): KClass<T> {
    return javaClass.kotlin
}
