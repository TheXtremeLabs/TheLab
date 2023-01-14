package com.riders.thelab.ui.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme

@Preview(showBackground = true)
@Composable
fun LoginContent() {
    TheLabTheme {
        Text(text = "Hello Login")
    }
}