package com.riders.thelab.core.ui.compose.component.tab

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV
import timber.log.Timber

///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun LabTabItem(
    isSelected: Boolean,
    tabWidth: Dp,
    text: String,
    shape: Shape = RoundedCornerShape(8.dp),
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
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

@Composable
fun LabTabItemTV(
    focusRequester: FocusRequester,
    isSelected: Boolean,
    tabWidth: Dp,
    text: String,
    shape: Shape = RoundedCornerShape(8.dp),
    selectedTextColor: Color = androidx.tv.material3.MaterialTheme.colorScheme.primary,
    unselectedTextColor: Color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedTextColor
        } else {
            unselectedTextColor
        },
        animationSpec = tween(easing = LinearEasing),
        label = "tab_text_animation"
    )

    LaunchedEffect(isSelected) {
        if (isSelected) {
            focusRequester.requestFocus()
        }
    }

    androidx.tv.material3.Text(
        modifier = Modifier
            .focusable(true, interactionSource)
            .clip(shape)
            .clickable(enabled = true, onClick = onClick)
            .width(tabWidth)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        text = text,
        fontSize = 14.sp,
        color = tabTextColor,
        textAlign = TextAlign.Center,
    )

    LaunchedEffect(isFocused) {
        Timber.d("Recomposition | Text: $text is focused")
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewLabTabItem(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Box(modifier = Modifier.background(if (!isSystemInDarkTheme()) Color.White else Color.Black)) {
            LabTabItem(
                isSelected = true,
                onClick = { },
                tabWidth = 150.dp,
                text = "Tab Text",
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedTextColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewLabTabItemTV() {
    TheLabThemeTV(theme = AppTheme.Default) {
        Box(modifier = Modifier.background(if (!isSystemInDarkTheme()) Color.White else Color.Black)) {
            LabTabItemTV(
                focusRequester = remember { FocusRequester() },
                isSelected = true,
                onClick = { },
                tabWidth = 150.dp,
                text = "Tab Text",
                selectedTextColor = androidx.tv.material3.MaterialTheme.colorScheme.onSurface,
                unselectedTextColor = androidx.tv.material3.MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }
}