package com.riders.thelab.central.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun BottomSheetContent(
    theme: AppTheme,
    darkTheme: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Column(modifier = Modifier) {
            Text(
                modifier = Modifier,
                text = "Text test !!",
                fontSize = 62.sp
            )
        }
    }
}


@DevicePreviews
@Composable
fun PreviewBottomSheetContent() {
    TheLabTheme(theme = AppTheme.Default) {
        BottomSheetContent(theme = AppTheme.Default, darkTheme = isSystemInDarkTheme()) {}
    }
}