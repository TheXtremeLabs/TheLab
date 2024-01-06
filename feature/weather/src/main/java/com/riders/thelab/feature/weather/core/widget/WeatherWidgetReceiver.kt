package com.riders.thelab.feature.weather.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.weather.WeatherWidgetModel
import timber.log.Timber

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    private val mWeatherWidget = WeatherWidget()

    // Let MyAppWidgetReceiver know which GlanceAppWidget to use
    override val glanceAppWidget: GlanceAppWidget = mWeatherWidget


    /**
     * Called when the first instance of the widget is placed. Since all instances share the same
     * state, we don't need to enqueue a new one for subsequent instances.
     *
     * Note: if you would need to load different data for each instance you could enqueue onUpdate
     * method instead. It's safe to call multiple times because of the unique work + KEEP policy
     */
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Timber.d("onEnabled()")
        //WeatherWorker.enqueue(context)
    }

    /**
     * Called when the last instance of this widget is removed.
     * Make sure to cancel all ongoing workers when user remove all widget instances
     */
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Timber.e("onDisabled()")
        //WeatherWorker.cancel(context)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Timber.d("onReceive() | intent: ${intent.toString()}")

        val mWeatherWidgetModel: WeatherWidgetModel? = if (LabCompatibilityManager.isTiramisu()) {
            intent.getSerializableExtra(EXTRA_WEATHER_WIDGET, WeatherWidgetModel::class.java)
        } else {
            intent.extras?.getSerializable(EXTRA_WEATHER_WIDGET) as? WeatherWidgetModel
        }

        //mWeatherWidgetModel?.let { mWeatherWidget.updateAll(context) }
    }

    companion object {
        const val EXTRA_WEATHER_WIDGET = "EXTRA_WEATHER_WIDGET"
    }
}