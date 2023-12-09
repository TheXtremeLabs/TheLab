package com.riders.thelab.core.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.transition.Transition
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.feature.weather.core.widget.TheLabAppWidgetProvider
import com.riders.thelab.ui.mainactivity.MainActivity
import kotlinx.coroutines.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TheLabAppWidgetProvider : AppWidgetProvider() {

    /*
        AppWidgetManager
            Updates AppWidget state; gets information about installed AppWidget
            providers and other AppWidget related state.

        ComponentName
            Identifier for a specific application component (Activity, Service, BroadcastReceiver,
            or ContentProvider) that is available. Two pieces of information, encapsulated here,
            are required to identify a component: the package (a String) it exists in, and the
            class (a String) name inside of that package.

        RemoteViews
            A class that describes a view hierarchy that can be displayed in another process.
            The hierarchy is inflated from a layout resource file, and this class provides some
            basic operations for modifying the content of the inflated hierarchy.
    */

    // Time
    private var isTimeUpdatedStarted: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Timber.d("onReceive()")

        if (null != intent) {
            when (intent.action) {
                AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                    if (null != intent.extras) {
                        Timber.d("null != intent.extras()")

                        // Log purpose
                        val city = intent.extras!!.getString(EXTRA_WEATHER_CITY)
                        val country = intent.extras!!.getString(EXTRA_WEATHER_COUNTRY)
                        val description = intent.extras!!.getString(EXTRA_WEATHER_DESCRIPTION)
                        val temperature = intent.extras!!.getString(EXTRA_WEATHER_TEMPERATURE)
                        val realFeels = intent.extras!!.getString(EXTRA_WEATHER_REAL_FEELS)
                        val icon = intent.extras!!.getString(EXTRA_WEATHER_ICON)

                        Timber.d("received : $description, $temperature, $realFeels, $icon")

                        val appWidgetId = intent.extras?.getInt(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID
                        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

                        Timber.d("appWidgetId : $appWidgetId")

                        val remoteViews =
                            RemoteViews(
                                context?.packageName,
                                R.layout.widget_the_lab_preview
                            ).also {

                                Timber.d("setup widget's views")

                                // Classic Way
                                /*try {
                                    val bitmap: Bitmap = Glide.with(context!!)
                                        .asBitmap()
                                        .load(icon)
                                        .submit(64, 64)
                                        .get()
                                    it.setImageViewBitmap(R.id.iv_weather_icon, bitmap)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }*/

                                // Glide's Widget constructor way
                                val awt: AppWidgetTarget = object : AppWidgetTarget(
                                    context?.applicationContext,
                                    R.id.iv_weather_icon,
                                    it,
                                    appWidgetId
                                ) {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        super.onResourceReady(resource, transition)
                                    }
                                }

                                val options =
                                    RequestOptions().override(300, 300)
                                        .placeholder(R.drawable.ic_lab_6_lab)
                                        .error(R.drawable.ic_error)

                                Glide.with(context!!)
                                    .asBitmap()
                                    .load(icon)
                                    .apply(options)
                                    .into(awt)

                                it.setTextViewText(
                                    R.id.tv_weather_main_description,
                                    description
                                )
                                it.setTextViewText(R.id.tv_city, "$city, $country")
                                it.setTextViewText(R.id.tv_weather, temperature)
                                it.setTextViewText(
                                    R.id.tv_weather_real_feels,
                                    "Real Feels : $realFeels"
                                )
                            }

                        val componentName =
                            ComponentName(context!!, TheLabAppWidgetProvider::class.java)
                        Timber.e("appWidgetManager.updateAppWidget")
                        AppWidgetManager.getInstance(context)
                            .updateAppWidget(componentName, remoteViews)
                    }
                }

                else -> {
                    Timber.e("else branch")
                }
            }
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Timber.d("onEnabled()")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Timber.e("onDisabled()")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Timber.e("onUpdate()")

        // Perform this loop procedure for each widget that belongs to this
        // provider.
        appWidgetIds?.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity.
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(
                    /* context = */ context,
                    /* requestCode = */
                    0,
                    /* intent = */
                    Intent(context, MainActivity::class.java),
//                    Intent(context, TheLabAppWidgetConfigurationActivity::class.java),
                    /* flags = */
                    if (LabCompatibilityManager.isMarshmallow()) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
                )


            // Get the layout for the widget and attach an on-click listener
            // to the button.
            val views: RemoteViews = RemoteViews(
                context?.packageName,
                R.layout.widget_the_lab_preview
            ).apply {
                CoroutineScope(Dispatchers.Main).launch {
                    Timber.d("updateTime()")

                    isTimeUpdatedStarted = true

                    while (isTimeUpdatedStarted) {
                        val date = Date(System.currentTimeMillis())
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)

                        setTextViewText(
                            R.id.tv_time,
                            time
                        )
                        delay(1000)
                    }
                }
                setTextViewText(
                    R.id.tv_time,
                    SimpleDateFormat("HH:mm", Locale.FRENCH)
                        .format(System.currentTimeMillis())
                        .toString()
                )
                setOnClickPendingIntent(R.id.button, pendingIntent)
            }

            // Tell the AppWidgetManager to perform an update on the current
            // widget.
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Timber.e("onDeleted()")
    }

    companion object {
        const val EXTRA_WEATHER_CITY = "EXTRA_WEATHER_CITY"
        const val EXTRA_WEATHER_COUNTRY = "EXTRA_WEATHER_COUNTRY"
        const val EXTRA_WEATHER_DESCRIPTION = "EXTRA_WEATHER_DESCRIPTION"
        const val EXTRA_WEATHER_TEMPERATURE = "EXTRA_WEATHER_TEMPERATURE"
        const val EXTRA_WEATHER_REAL_FEELS = "EXTRA_WEATHER_REAL_FEELS"
        const val EXTRA_WEATHER_ICON = "EXTRA_WEATHER_ICON"
    }
}