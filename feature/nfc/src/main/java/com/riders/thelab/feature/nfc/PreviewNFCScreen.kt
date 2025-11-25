package com.riders.thelab.feature.nfc

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


@Composable
fun NFCScreen() {
    TheLabTheme(theme = AppTheme.Default) { }
}


@DevicePreviewsPhoneOnly
@Composable
private fun PreviewNFCScreen() {
    TheLabTheme(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {
        NFCScreen()
    }
}