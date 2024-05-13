package com.riders.thelab.feature.flightaware.ui.splashscreen

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.ProvidedBy
import com.riders.thelab.core.ui.compose.component.tickerborad.TickerBoard
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
    var newsFlightTitle by remember { mutableStateOf("") }
    var switchProvidedBy by remember { mutableStateOf(false) }

    TheLabTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.img_plane_wing_sunset),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(bottom = 72.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TickerBoard(
                        modifier = Modifier.layoutId("tickerBoard"),
                        text = newsFlightTitle,
                        numColumns = 6,
                        numRows = 2,
                        textColor = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                        fontSize = 24.sp
                    )
                }

                AnimatedVisibility(visible = if (LocalInspectionMode.current) true else switchProvidedBy) {
                    ProvidedBy(
                        providerIcon = R.drawable.ic_flightaware_logo,
                        hasPadding = true,
                        hasRoundedCorners = true,
                        textColor = Color.White,
                        backgroundColor = Color(0xFF002f5d)
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        newsFlightTitle = "News\nFlight"
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