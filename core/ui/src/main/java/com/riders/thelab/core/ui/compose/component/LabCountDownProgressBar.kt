package com.riders.thelab.core.ui.compose.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.R

@Composable
fun LabCountDownProgressBar(
    secondsRemaining:Int,
    progressBarValue: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressBarValue.toFloat() / 100f,
        label = "wifiProgress"
    )

    MaterialTheme {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                modifier = Modifier.semantics {
                    contentDescription = "ResetWifiProgress"
                    testTag = "linear_progress_reset_wifi"
                },
                progress = { animatedProgress }
            )

            Text(
                text = pluralStringResource(
                    id = R.plurals.countdown_seconds_remaining,
                    count = secondsRemaining,
                    secondsRemaining
                )
            )
        }
    }
}