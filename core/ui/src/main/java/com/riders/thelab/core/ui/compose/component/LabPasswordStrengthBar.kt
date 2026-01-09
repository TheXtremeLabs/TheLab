package com.riders.thelab.core.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.local.bean.StrengthLevel

//////////////////////////////////////////////////////////
//
// COMPOSE
//
//////////////////////////////////////////////////////////
@Composable
fun PasswordStrengthBar(
    theme: AppTheme,
    darkTheme: Boolean,
    score: Int,
    maxScore: Int,
    modifier: Modifier = Modifier
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(maxScore) { index ->
                val isActive = index < score

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (isActive) strengthColor(score)
                            else Color.LightGray
                        )
                )
            }
        }
    }
}

@Composable
fun strengthColor(score: Int): Color =
    when (StrengthLevel.getStrengthLevel(score)) {
        StrengthLevel.VERY_WEAK -> Color(0xFFD32F2F)
        StrengthLevel.WEAK -> Color(0xFFF57C00)
        StrengthLevel.MODERATE -> Color(0xFFFBC02D)
        StrengthLevel.STRONG -> Color(0xFF388E3C)
        StrengthLevel.VERY_STRONG -> Color(0xFF2E7D32)
    }


//////////////////////////////////////////////////////////
//
// PREVIEWS
//
//////////////////////////////////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewPasswordStrengthBar() {
    TheLabTheme(theme = AppTheme.Default) {
        PasswordStrengthBar(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            score = 3,
            maxScore = 5
        )
    }
}