package com.riders.thelab.core.ui.compose.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun ProvidedBy(modifier: Modifier = Modifier, @StringRes placeholderProvidedBy: Int? = null, @DrawableRes providerIcon: Int) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (null == placeholderProvidedBy) "provided by" else stringResource(id = placeholderProvidedBy),
            fontSize = 12.sp
        )
        Image(
            modifier = Modifier.height(28.dp),
            painter = painterResource(id = providerIcon),
            contentDescription = "open weather icon"
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewProvidedBy() {
    TheLabTheme {
        ProvidedBy(
            placeholderProvidedBy = R.string.weather_data_provided_by,
            providerIcon = R.drawable.tmdb_logo
        )
    }
}