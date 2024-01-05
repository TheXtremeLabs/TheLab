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

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.utils.appWidgetBackgroundCornerRadius
import com.riders.thelab.feature.weather.ui.WeatherActivity

private val destinationKey = ActionParameters.Key<String>(
    WeatherActivity.KEY_DESTINATION
)

@Composable
fun WeatherWidgetContent(/*weatherWidgetModel: WeatherWidgetModel*/) {

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(md_theme_dark_background.copy(alpha = .7f))
            .appWidgetBackground()
            .background(GlanceTheme.colors.background)
            .appWidgetBackgroundCornerRadius(),
        contentAlignment = Alignment.TopEnd
    ) {

        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top header info current weather
            Text(modifier = GlanceModifier.padding(12.dp), text = "Where to?")


            // Forecast row weather info
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                repeat(5) { index ->
                    Column(
                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        Button(
                            text = "Home",
                            onClick = actionStartActivity<WeatherActivity>(
                                actionParametersOf(
                                    destinationKey to "home"
                                )
                            )
                        )

                        Text(modifier = GlanceModifier.padding(12.dp), text = "Weather value")

                        Button(
                            text = "Work",
                            onClick = actionStartActivity<WeatherActivity>(
                                actionParametersOf(
                                    destinationKey to "work"
                                )
                            )
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

@DevicePreviews
@Composable
private fun PreviewWeatherWidgetContent() {
    TheLabTheme {
        WeatherWidgetContent(/*weatherWidgetModel = WeatherWidgetModel()*/)
    }
}