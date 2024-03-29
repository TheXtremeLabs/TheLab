package com.riders.thelab.feature.weather.core.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.riders.thelab.feature.weather.core.worker.WeatherWidgetWorker
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
        Timber.i("onEnabled() | enqueue worker")
        WeatherWidgetWorker.enqueue(context)
    }

    /**
     * Called when the last instance of this widget is removed.
     * Make sure to cancel all ongoing workers when user remove all widget instances
     */
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Timber.e("onDisabled() | cancel worker")
        WeatherWidgetWorker.cancel(context)
    }
}