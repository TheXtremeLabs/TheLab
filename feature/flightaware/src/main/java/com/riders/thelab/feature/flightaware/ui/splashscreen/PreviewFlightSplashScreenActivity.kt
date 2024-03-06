package com.riders.thelab.feature.flightaware.ui.splashscreen

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.feature.flightaware.R
import com.riders.thelab.feature.flightaware.ui.main.FlightMainActivity
import kotlinx.coroutines.delay


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun FlightSplashScreenContent() {

    val context = LocalContext.current
    var switchProvidedBy by remember { mutableStateOf(false) }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(.85f),
                horizontalAlignment = Alignment.End
            ) {
                AnimatedVisibility(visible = if (LocalInspectionMode.current) true else switchProvidedBy) {
                    ProvidedBy(
                        providerIcon = R.drawable.ic_flightaware_logo,
                        hasPadding = true,
                        hasRoundedCorners = true,
                        backgroundColor = Color(0xFF002f5d)
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(250L)
        switchProvidedBy = true

        delay(3_000L)

        (context.findActivity() as FlightSplashScreenActivity).run {
            startActivity(Intent(this, FlightMainActivity::class.java))
            finish()
        }
    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewFlightSplashScreenContent() {
    TheLabTheme {
        FlightSplashScreenContent()
    }
}