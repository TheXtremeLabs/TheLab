package com.riders.thelab.feature.weather.core.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.weather.ui.WeatherActivity
import timber.log.Timber

class TheLabAppWidgetConfigurationActivity : ComponentActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")
        setContent {
            TheLabTheme {
                Greeting()
            }
        }

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val myProvider = ComponentName(this, TheLabAppWidgetProvider::class.java)

        if (LabCompatibilityManager.isOreo()) {
            Timber.d("isOreo()")
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                Timber.d("appWidgetManager.isRequestPinAppWidgetSupported true")
                // Create the PendingIntent object only if your app needs to be notified
                // that the user allowed the widget to be pinned. Note that, if the pinning
                // operation fails, your app isn't notified. This callback receives the ID
                // of the newly-pinned widget (EXTRA_APPWIDGET_ID).
                val successCallback = PendingIntent.getBroadcast(
                    /* context = */ this,
                    /* requestCode = */
                    0,
                    /* intent = */
                    Intent(this, WeatherActivity::class.java),
                    /* flags = */
                    if (LabCompatibilityManager.isMarshmallow()) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
                )

                appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
            }
        }
    }
}

@Composable
fun Greeting() {
    Text(text = "Greeting")
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TheLabTheme {
        Greeting()
    }
}