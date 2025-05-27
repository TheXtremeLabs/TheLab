package com.riders.thelab.core.ui.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

@Composable
fun NoItemFound(
    theme: AppTheme,
    darkTheme: Boolean,
    message: String? = null,
    errorImageResId: Int? = R.drawable.logo_testing,
    lottieResId: Int? = if (!isSystemInDarkTheme()) R.raw.error_rolling else R.raw.error_rolling_dark_theme
) {
    check(null != errorImageResId || null != lottieResId) {
        "Either errorImageResId or lottieResId must be provided"
    }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = md_theme_dark_background) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (null != errorImageResId && null == lottieResId) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(.65f)
                            .weight(1.5f),
                        painter = painterResource(errorImageResId),
                        contentDescription = "Error image resource",
                        contentScale = ContentScale.Fit
                    )
                }

                if (null != lottieResId && null == errorImageResId) {
                    Lottie(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                            .weight(1.5f),
                        rawResId = lottieResId
                    )
                }

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
    val searchedAppRequest: String = "Koal"

    TheLabTheme(theme = appTheme) {
        NoItemFound(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            message = "Oops! No item found for value \"$searchedAppRequest\"\nPlease retry..."
        )
    }
}