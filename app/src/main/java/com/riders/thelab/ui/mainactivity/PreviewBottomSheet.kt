package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_light_onPrimaryContainer
import com.riders.thelab.core.compose.ui.theme.md_theme_light_primaryContainer


@DevicePreviews
@Composable
fun BottomSheetContent() {
    TheLabTheme {
        // Sheet content
        Column(
            modifier = Modifier
                .background(if (!isSystemInDarkTheme()) md_theme_light_primaryContainer else md_theme_light_onPrimaryContainer)
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            Text("This is a bottom sheet", fontSize = 24.sp, fontWeight = FontWeight.W600)

            Spacer(modifier = Modifier.size(16.dp))

            Card(
                modifier = Modifier.width(190.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "First name : Kendrick")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Last name : Lamar")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Age : 33 years")
                }
            }
        }
    }
}