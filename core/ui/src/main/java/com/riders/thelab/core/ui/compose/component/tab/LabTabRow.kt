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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Tab
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.color.md_theme_dark_primary
import com.riders.thelab.core.ui.compose.color.md_theme_dark_tertiaryContainer
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV
import timber.log.Timber
import java.util.Locale

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun LabTabRow(
    theme: AppTheme,
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    tabWidth: Dp = 150.dp,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    selectedTextColor: Color = MaterialTheme.colorScheme.inversePrimary,
    unselectedTextColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
    hasCustomShape: Boolean = false,
    shape: Shape = if (!hasCustomShape) RoundedCornerShape(16.dp) else RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp
    ),
    onClick: (index: Int) -> Unit
) {
    /* animateDpAsState is used to animate the tab indicator offset when the selected tab is changed */
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = tabWidth * selectedItemIndex,
        animationSpec = tween(easing = LinearEasing),
        label = "indicator animation",
    )

    TheLabTheme(theme = theme) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(backgroundColor)
                .height(intrinsicSize = IntrinsicSize.Min),
        ) {
            LabTabIndicator(
                indicatorWidth = tabWidth,
                indicatorOffset = indicatorOffset,
                indicatorColor = indicatorColor,
                shape = shape,
                hasCustomShape = hasCustomShape
            )

            Row(
                modifier = Modifier.clip(shape),
                horizontalArrangement = Arrangement.Center,
            ) {
                items.mapIndexed { index, text ->
                    val isSelected = index == selectedItemIndex
                    LabTabItem(
                        isSelected = isSelected,
                        onClick = {
                            Timber.d("LabTabRow | clicked index: $index")
                            onClick.invoke(index)
                        },
                        tabWidth = tabWidth,
                        text = text,
                        selectedTextColor = selectedTextColor,
                        unselectedTextColor = unselectedTextColor,
                        shape = shape
                    )
                }
            }
        }
    }
}

@Composable
fun LabTabRowTV(
    theme: AppTheme,
    bringIntoViewRequester: BringIntoViewRequester,
    focusRequester: FocusRequester,
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    tabWidth: Dp = 150.dp,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    selectedTextColor: Color = MaterialTheme.colorScheme.inversePrimary,
    unselectedTextColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
    hasCustomShape: Boolean = false,
    shape: Shape = if (!hasCustomShape) RoundedCornerShape(16.dp) else RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp
    ),
    onClick: (index: Int) -> Unit
) {
    /* animateDpAsState is used to animate the tab indicator offset when the selected tab is changed */
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = tabWidth * selectedItemIndex,
        animationSpec = tween(easing = LinearEasing),
        label = "indicator animation",
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TheLabThemeTV(theme = theme) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                // .background(backgroundColor)
                .height(72.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.tv.material3.TabRow(
                selectedTabIndex = selectedItemIndex,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(shape)
                    .bringIntoViewRequester(bringIntoViewRequester),
                //containerColor = backgroundColor,
                // contentColor = selectedTextColor,
                indicator = { tabPositions, doesTabRowHaveFocus ->
                    if (hasCustomShape) {
                        LabTabIndicator(
                            indicatorWidth = tabWidth,
                            indicatorOffset = indicatorOffset,
                            indicatorColor = indicatorColor,
                            shape = shape,
                            hasCustomShape = hasCustomShape
                        )
                    } else {
                        androidx.tv.material3.TabRowDefaults.PillIndicator(
                            modifier = Modifier.focusRequester(focusRequester),
                            currentTabPosition = tabPositions[selectedItemIndex],
                            doesTabRowHaveFocus = doesTabRowHaveFocus,
                            // activeColor = indicatorColor,
                            // inactiveColor = indicatorColor
                        )
                    }
                }
            ) {

                /* repeat(items.size) {
                     key(it) {
                         Tab(
                             selected = activeTabIndex == it,
                             onFocus = { focusedTabIndex = it },
                             onClick = {
                                 focusedTabIndex = it
                                 activeTabIndex = it
                             }
                         ) {

                         }
                     }
                 }*/
                items.mapIndexed { index, text ->
                    val isSelected = index == selectedItemIndex
                    Timber.d("LabTabRow | isSelected: $isSelected | index: $index")

                    if (isSelected) onClick(index)

                    if (hasCustomShape) {
                        LabTabItemTV(
                            focusRequester = focusRequester,
                            isSelected = isSelected,
                            onClick = {
                                Timber.d("LabTabRow | clicked index: $index")
                                onClick(index)
                            },
                            tabWidth = tabWidth,
                            text = text,
                            selectedTextColor = selectedTextColor,
                            unselectedTextColor = unselectedTextColor,
                            shape = shape
                        )
                    } else {
                        Tab(
                            modifier = Modifier
                                .bringIntoViewRequester(bringIntoViewRequester)
                                .focusRequester(focusRequester),
                            selected = isSelected,
                            onFocus = { onClick(index) },
                            onClick = { onClick(index) }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center) {

                                androidx.tv.material3.Text(
                                    modifier  = Modifier.padding(10.dp),
                                    text = text,
                                    style = androidx.tv.material3.MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewLabTabRow(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val selectedIndex = remember { mutableIntStateOf(1) }

    TheLabTheme(theme = appTheme) {
        LabTabRow(
            theme = appTheme,
            selectedItemIndex = selectedIndex.intValue,
            items = listOf(
                "details".uppercase(Locale.getDefault()),
                "instructions".uppercase(Locale.getDefault())
            ),
            onClick = {},
            hasCustomShape = false
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewLabTabRowWithCustomShape(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val selectedIndex = remember { mutableIntStateOf(1) }

    TheLabTheme(theme = appTheme) {
        LabTabRow(
            theme = appTheme,
            selectedItemIndex = selectedIndex.intValue,
            items = listOf(
                "details".uppercase(Locale.getDefault()),
                "instructions".uppercase(Locale.getDefault())
            ),
            onClick = {},
            backgroundColor = Color.Black,
            indicatorColor = md_theme_dark_primary,
            selectedTextColor = Color.White,
            unselectedTextColor = md_theme_dark_tertiaryContainer,
            hasCustomShape = true
        )
    }
}


@DevicePreviewsTV
@Composable
private fun PreviewLabTabRowTV() {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusRequester = remember { FocusRequester() }
    val selectedIndex = remember { mutableIntStateOf(1) }

    TheLabThemeTV(theme = AppTheme.Default) {
        LabTabRowTV(
            theme = AppTheme.Default,
            bringIntoViewRequester = bringIntoViewRequester,
            focusRequester = focusRequester,
            selectedItemIndex = selectedIndex.intValue,
            items = listOf(
                "details".uppercase(Locale.getDefault()),
                "instructions".uppercase(Locale.getDefault())
            ),
            onClick = {},
            hasCustomShape = false
        )
    }
}
