package com.riders.thelab.feature.weather.core.widget

/* Import Glance Composables
 In the event there is a name clash with the Compose classes of the same name,
 you may rename the imports per https://kotlinlang.org/docs/packages.html#imports
 using the `as` keyword.

import androidx.glance.Button
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.text.Text
*/

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.ImageProvider
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.utils.appWidgetBackgroundCornerRadius
import com.riders.thelab.feature.weather.core.worker.WeatherWidgetWorker
import com.riders.thelab.feature.weather.ui.WeatherActivity
import timber.log.Timber
import kotlin.math.roundToInt

private val destinationKey = ActionParameters.Key<String>(
    WeatherActivity.KEY_DESTINATION
)

/**
 * Create an ImageProvider using an URI if it's a "content://" type, otherwise load
 * the bitmap from the cache file
 *
 * Note: When using bitmaps directly your might reach the memory limit for RemoteViews.
 * If you do reach the memory limit, you'll need to generate a URI granting permissions
 * to the launcher.
 *
 * More info:
 * https://developer.android.com/training/secure-file-sharing/share-file#GrantPermissions
 */
private fun getImageProvider(path: String): ImageProvider {
    if (path.startsWith("content://")) {
        return ImageProvider(path.toUri())
    }
    val bitmap = BitmapFactory.decodeFile(path)
    return ImageProvider(bitmap)
}


@Composable
fun WeatherWidgetContent() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Get the stored stated based on our custom state definition.
    val weatherInfo = currentState<WeatherInfo>()
    // It will be one of the provided ones
    val size = LocalSize.current

    GlanceTheme {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                //.appWidgetBackground()
                .background(md_theme_dark_background.copy(alpha = .87f))
                .appWidgetBackgroundCornerRadius(),
            contentAlignment = Alignment.Center
        ) {
            when (weatherInfo) {
                is WeatherInfo.Loading -> {
                    CircularProgressIndicator()
                }

                is WeatherInfo.Available -> {
                    val weatherWidgetModel = weatherInfo.currentData

                    Box(
                        modifier = GlanceModifier.fillMaxSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Column(
                            modifier = GlanceModifier.fillMaxSize().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Top header info current weather
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Horizontal.Start,
                                verticalAlignment = Alignment.Vertical.CenterVertically
                            ) {

                                // Location and temperature
                                Column(
                                    modifier = GlanceModifier.defaultWeight(),
                                    horizontalAlignment = Alignment.Horizontal.Start,
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {

                                    // Location
                                    Text(
                                        modifier = GlanceModifier.padding(8.dp),
                                        text = weatherInfo.placeName,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = ColorProvider(Color.White)
                                        )
                                    )

                                    // Temperature
                                    Text(
                                        modifier = GlanceModifier.padding(8.dp),
                                        text = "${weatherWidgetModel.temperature.realFeels.roundToInt()} ${
                                            context.getString(
                                                R.string.degree_placeholder
                                            )
                                        }",
                                        style = TextStyle(
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorProvider(Color.White)
                                        )
                                    )
                                }


                                // Icon description and min/max
                                Column(
                                    modifier = GlanceModifier.defaultWeight(),
                                    horizontalAlignment = Alignment.Horizontal.End,
                                    verticalAlignment = Alignment.Vertical.CenterVertically
                                ) {

                                    // Icon
                                    Image(
                                        modifier = GlanceModifier.size(64.dp),
                                        provider = getImageProvider(weatherInfo.currentData.icon),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillBounds
                                    )

                                    // Description
                                    Text(
                                        modifier = GlanceModifier.padding(8.dp),
                                        text = weatherWidgetModel.description,
                                        style = TextStyle(
                                            fontSize = 18.sp, color = ColorProvider(Color.White)
                                        )
                                    )

                                    Row(
                                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                                        verticalAlignment = Alignment.Vertical.CenterVertically
                                    ) {
                                        Text(
                                            modifier = GlanceModifier.padding(8.dp),
                                            text = "${weatherWidgetModel.temperature.min.roundToInt()} ${
                                                context.getString(
                                                    R.string.degree_placeholder
                                                )
                                            }",
                                            style = TextStyle(color = ColorProvider(Color.LightGray))
                                        )
                                        Text(
                                            modifier = GlanceModifier.padding(8.dp),
                                            text = "${weatherWidgetModel.temperature.max.roundToInt()} ${
                                                context.getString(
                                                    R.string.degree_placeholder
                                                )
                                            }",
                                            style = TextStyle(color = ColorProvider(Color.White))
                                        )
                                    }
                                }
                            }


                            // Forecast row weather info
                            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                                repeat(weatherWidgetModel.forecast.take(5).size) { index ->
                                    Column(
                                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                                        verticalAlignment = Alignment.Vertical.CenterVertically
                                    ) {

                                        Text(
                                            modifier = GlanceModifier.padding(12.dp),
                                            text = "${weatherWidgetModel.forecast[index].temperature.min.roundToInt()} / ${weatherWidgetModel.forecast[index].temperature.max.roundToInt()}",
                                            style = TextStyle(color = ColorProvider(Color.White))
                                        )

                                        // Icon
                                        Image(
                                            modifier = GlanceModifier.size(40.dp),
                                            provider = getImageProvider(weatherWidgetModel.forecast[index].icon),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillBounds
                                        )

                                        Text(
                                            modifier = GlanceModifier.padding(12.dp),
                                            text = weatherWidgetModel.forecast[index].day.take(3),
                                            style = TextStyle(color = ColorProvider(Color.White))
                                        )
                                    }
                                }
                            }
                        }

                        Image(
                            modifier = GlanceModifier.padding(16.dp),
                            provider = ImageProvider(R.drawable.ic_open_in_new),
                            contentDescription = null
                        )
                    }
                }

                is WeatherInfo.Unavailable -> {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Data not available")
                        Button("Refresh", actionRunCallback<UpdateWeatherAction>())
                    }
                }
            }
        }
    }
}


/**
 * Force update the weather info after user click
 */
class UpdateWeatherAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Timber.d("onAction() | Force the worker to refresh")
        // Force the worker to refresh
        WeatherWidgetWorker.enqueue(context = context, force = true)
    }
}