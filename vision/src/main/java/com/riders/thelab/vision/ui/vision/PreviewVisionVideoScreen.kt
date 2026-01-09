package com.riders.thelab.vision.ui.vision

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun VisionVideoScreen(
    theme: AppTheme,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    TheLabTheme(theme = theme, darkTheme = isDarkTheme) { }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewVisionVideoScreen() {
    TheLabTheme(theme = AppTheme.Default) { }
}