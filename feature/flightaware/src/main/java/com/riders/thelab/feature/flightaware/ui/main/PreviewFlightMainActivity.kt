package com.riders.thelab.feature.flightaware.ui.main

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun FlightMainContent() {

    TheLabTheme {

    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewFlightMainContent() {
    TheLabTheme {
        FlightMainContent()
    }
}