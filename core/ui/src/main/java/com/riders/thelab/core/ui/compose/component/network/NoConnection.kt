package com.riders.thelab.core.ui.compose.component.network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background

@DevicePreviews
@Composable
fun NoNetworkConnection() {
    TheLabTheme {
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
                    text = "${stringResource(id = R.string.network_status_disconnected)}\n${
                        stringResource(
                            id = R.string.network_check_connection
                        )
                    }",
                    style = TextStyle(textAlign = TextAlign.Center),
                    color = Color.LightGray
                )
            }
        }
    }
}