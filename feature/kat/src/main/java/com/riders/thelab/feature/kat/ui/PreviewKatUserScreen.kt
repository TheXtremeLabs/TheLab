package com.riders.thelab.feature.kat.ui

import androidx.compose.runtime.Composable
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun KatUserScreen() {
    TheLabTheme {

    }
}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewKaUserScreen() {
    TheLabTheme {
        KatUserScreen()
    }
}