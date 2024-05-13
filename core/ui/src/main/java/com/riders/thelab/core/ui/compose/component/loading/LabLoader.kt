package com.riders.thelab.core.ui.compose.component.loading

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////
@Composable
fun LabLoader(modifier: Modifier) {
    TheLabTheme {
        BoxWithConstraints(
            modifier = Modifier
                .wrapContentSize()
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            Lottie(
                modifier = Modifier.size(width = this.maxWidth, height = maxHeight),
                rawResId = R.raw.lottie_circular_half_dotted_loading
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewLabLoader() {
    TheLabTheme {
        LabLoader(modifier = Modifier.size(56.dp))
    }
}