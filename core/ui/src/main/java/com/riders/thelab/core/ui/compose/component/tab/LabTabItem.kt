package com.riders.thelab.core.ui.compose.component.tab

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primary
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primary

@Composable
fun LabTabItem(
    isSelected: Boolean,
    tabWidth: Dp,
    text: String,
    shape: Shape = RoundedCornerShape(8.dp),
    selectedTextColor: Color = if (!isSystemInDarkTheme()) md_theme_dark_primary else md_theme_light_primary,
    unselectedTextColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
    onClick: () -> Unit
) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedTextColor
        } else {
            unselectedTextColor
        },
        animationSpec = tween(easing = LinearEasing),
        label = "tab_text_animation"
    )

    Text(
        modifier = Modifier
            .clip(shape)
            .clickable(enabled = true, onClick = onClick)
            .width(tabWidth)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        text = text,
        fontSize = 14.sp,
        color = tabTextColor,
        textAlign = TextAlign.Center,
    )
}

@DevicePreviews
@Composable
private fun PreviewLabTabItem() {
    TheLabTheme {
        Box(modifier = Modifier.background(if (!isSystemInDarkTheme()) Color.White else Color.Black)) {
            LabTabItem(
                isSelected = true,
                onClick = { },
                tabWidth = 150.dp,
                text = "Tab Text"
            )
        }
    }
}