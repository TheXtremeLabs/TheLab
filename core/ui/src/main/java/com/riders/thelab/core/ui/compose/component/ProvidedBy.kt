package com.riders.thelab.core.ui.compose.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background

@Composable
fun ProvidedBy(
    modifier: Modifier = Modifier,
    @StringRes placeholderProvidedBy: Int? = null,
    @DrawableRes providerIcon: Int,
    hasRoundedCorners: Boolean = false,
    hasPadding: Boolean = false,
    textColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
    backgroundColor: Color? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (null == placeholderProvidedBy) "provided by" else stringResource(id = placeholderProvidedBy),
            color = textColor,
            fontSize = 12.sp
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(if (!hasRoundedCorners) 0.dp else 12.dp))
                .background(color = backgroundColor ?: Color.Transparent)
        ) {
            Image(
                modifier = Modifier
                    .height(28.dp)
                    .padding(if (!hasPadding) 0.dp else 4.dp),
                painter = painterResource(id = providerIcon),
                contentDescription = "open weather icon"
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewProvidedBy() {
    TheLabTheme {
        ProvidedBy(
            modifier = Modifier.background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            placeholderProvidedBy = R.string.weather_data_provided_by,
            providerIcon = R.drawable.openweathermap_logo_white
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewProvidedByWeather() {
    TheLabTheme {
        ProvidedBy(
            modifier = Modifier.background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            placeholderProvidedBy = R.string.weather_data_provided_by,
            providerIcon = R.drawable.openweathermap_logo_white
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewProvidedByTMDB() {
    TheLabTheme {
        ProvidedBy(
            modifier = Modifier.background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            providerIcon = R.drawable.tmdb_logo,
            hasRoundedCorners = true
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewProvidedByFlightAware() {
    TheLabTheme {
        ProvidedBy(
            modifier = Modifier.background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            providerIcon = R.drawable.ic_flightaware_logo,
            hasRoundedCorners = true,
            hasPadding = true,
            backgroundColor = Color(0xFF002f5d)
        )
    }
}