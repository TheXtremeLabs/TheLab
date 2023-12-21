package com.riders.thelab.ui.signup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_onPrimaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_onPrimaryContainer


@Composable
fun ExitSignUp(onConfirmed: () -> Unit, onDismiss: () -> Unit) {
    TheLabTheme {
        Card(modifier = Modifier.fillMaxWidth(.8f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
            ) {

                // Warning
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Warning",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.W600)
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Are you sure to leave the sign-up screen?\nAll your data will be lost."
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(.7f)
                        .height(48.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onConfirmed,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = stringResource(id = R.string.action_yes),
                            color = if (!isSystemInDarkTheme()) md_theme_light_onPrimaryContainer else md_theme_dark_onPrimaryContainer,
                            maxLines = 1
                        )
                    }
                    Button(modifier = Modifier.weight(1f), onClick = onDismiss) {
                        Text(
                            text = stringResource(id = R.string.action_no),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewExit() {
    TheLabTheme {
        ExitSignUp({}, {})
    }
}
