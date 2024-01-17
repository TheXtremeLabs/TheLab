package com.riders.thelab.core.ui.compose.component.tab

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primaryContainer
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primaryContainer
import java.util.Locale

@Composable
fun LabTabRow(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    tabWidth: Dp = 150.dp,
    onClick: (index: Int) -> Unit,
) {

    val shape = RoundedCornerShape(16.dp)
    /* animateDpAsState is used to animate the tab indicator offset when the selected tab is changed */
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = tabWidth * selectedItemIndex,
        animationSpec = tween(easing = LinearEasing),
        label = "indicator animation",
    )

    TheLabTheme {
        Box(
            modifier = modifier
                .clip(shape)
                .background(Color.Transparent)
                .height(intrinsicSize = IntrinsicSize.Min),
        ) {
            LabTabIndicator(
                indicatorWidth = tabWidth,
                indicatorOffset = indicatorOffset,
                indicatorColor = if (!isSystemInDarkTheme()) md_theme_light_primaryContainer else md_theme_dark_primaryContainer,
                shape = shape
            )

            Row(
                modifier = Modifier.clip(shape),
                horizontalArrangement = Arrangement.Center,
            ) {
                items.mapIndexed { index, text ->
                    val isSelected = index == selectedItemIndex
                    LabTabItem(
                        isSelected = isSelected,
                        onClick = { onClick(index) },
                        tabWidth = tabWidth,
                        text = text,
                        shape = shape
                    )
                }
            }
        }
    }
}


@DevicePreviews
@Composable
private fun PreviewLabTabRow() {
    val selectedIndex = remember { mutableIntStateOf(0) }

    TheLabTheme {
        LabTabRow(
            selectedItemIndex = selectedIndex.intValue,
            items = listOf(
                "details".uppercase(Locale.getDefault()),
                "instructions".uppercase(Locale.getDefault())
            ),
            onClick = {}
        )
    }
}