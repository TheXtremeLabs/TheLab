package com.riders.thelab.core.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.color.md_theme_dark_background
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@DevicePreviews
@Composable
fun NoItemFound(message: String? = null) {
    TheLabTheme(theme = AppTheme.Default) {
        Surface(modifier = Modifier.fillMaxSize(), color = md_theme_dark_background) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Lottie(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)
                        .weight(1.5f),
                    rawResId = R.raw.lottie_hot_coffee_loading
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .weight(1f),
                    text = message ?: "No item found",
                    style = TextStyle(textAlign = TextAlign.Center),
                    color = Color.LightGray
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewNoItemFound(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = AppTheme.Default) {
        NoItemFound("No song item found")
    }
}