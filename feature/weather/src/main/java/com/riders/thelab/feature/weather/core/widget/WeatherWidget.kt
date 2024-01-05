package com.riders.thelab.feature.weather.core.widget

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.riders.thelab.core.data.local.model.weather.WeatherWidgetModel
import timber.log.Timber

class WeatherWidget : GlanceAppWidget() {

    ////////////////////////////////////////
    // Composable states
    ////////////////////////////////////////
    private var mTitle by mutableStateOf("Hello World")
        private set
    private var mWeatherWidgetModel: WeatherWidgetModel? by mutableStateOf(null)
        private set


    ////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////
    override val sizeMode = SizeMode.Responsive(
        setOf(
            SMALL_SQUARE,
            HORIZONTAL_RECTANGLE,
            BIG_SQUARE
        )
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            WeatherWidgetContent()
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        Timber.e("onDelete()")
    }

    fun updateAll(context: Context, weatherWidgetModel: WeatherWidgetModel) {
        Timber.d("updateAll() | weather widget object: $weatherWidgetModel")

        this.mTitle = "Value has been updated from Worker manager"
        this.mWeatherWidgetModel = weatherWidgetModel
    }

    companion object {
        val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }
}