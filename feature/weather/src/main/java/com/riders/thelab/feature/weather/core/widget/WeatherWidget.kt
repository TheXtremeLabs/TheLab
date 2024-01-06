package com.riders.thelab.feature.weather.core.widget

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import com.riders.thelab.core.data.local.model.weather.WeatherWidgetModel
import com.riders.thelab.core.ui.utils.toPx
import timber.log.Timber

class WeatherWidget : GlanceAppWidget() {

    ////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////

    // Override the state definition to use our custom one using Kotlin serialization
    override val stateDefinition: GlanceStateDefinition<*>
        get() = WeatherGlanceStateDefinition

    // Define the supported sizes for this widget.
    // The system will decide which one fits better based on the available space
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(thinMode, smallMode, mediumMode, largeMode)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Timber.i("provideGlance()")

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            Timber.i("provideContent")
            // create your AppWidget here
            WeatherWidgetContent()
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        Timber.e("onDelete()")
    }

    fun updateAll(context: Context) {
        Timber.d("updateAll()")
    }

    companion object {

        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 280.dp)

        val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        val BIG_SQUARE = DpSize(250.dp, 250.dp)


        val sourceKey = stringPreferencesKey("image_source")
        val sourceUrlKey = stringPreferencesKey("image_source_url")

        fun getImageKey(size: DpSize) = getImageKey(size.width.value.toPx, size.height.value.toPx)

        fun getImageKey(width: Float, height: Float) = stringPreferencesKey(
            "uri-$width-$height"
        )
    }
}