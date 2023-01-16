package com.riders.thelab.ui.colors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.riders.thelab.core.compose.ui.theme.TheLabTheme

class ColorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheLabTheme {
                Color()
            }
        }
    }
}