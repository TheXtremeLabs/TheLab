package com.riders.thelab.core.ui.compose.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Colors define for testing
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val Teal200 = Color(0xFF03DAC5)

val Red200 = Color(0xFFEF9A9A)
val Red500 = Color(0xFFF44336)
val Red700 = Color(0xFFD32F2F)

val Green200 = Color(0xFFA5D6A7)
val Green500 = Color(0xFF4CAF50)
val Green700 = Color(0xFF388E3C)
// Colors define for testing

val chronoBlue = Color(0xFF007AC7)

/*val success = Color(0xFF1D740E)
val warning = Color(0xFFC28621)
val error = Color(0xFFA30404)*/

val Orange = Color(0xFFBD5B05)

val success = Color(0xEE38810C)
val warning = Color(0xEE886E0F)
val error = Color(0xEE9B0606)


// Colors for shimmer effect
val ShimmerColorShades = listOf(
    Color.LightGray.copy(0.9f),
    Color.LightGray.copy(0.2f),
    Color.LightGray.copy(0.9f)
)


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)
