package com.riders.thelab.core.ui.compose.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.utils.switch


@Composable
fun TheLabTheme(
    // Specify the theme here
    theme: AppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // Check if is dart theme or not
            if (darkTheme) {
                when (theme) {
                    AppTheme.Default -> theLabThemeDynamicDarkColorScheme(context)
                    AppTheme.Blue -> blueThemeDynamicDarkColorScheme(context)
                    AppTheme.Red -> redThemeDynamicDarkColorScheme(context)
                    AppTheme.Green -> greenThemeDynamicDarkColorScheme(context)
                    else -> dynamicDarkColorScheme(context)
                }
            } else {
                when (theme) {
                    AppTheme.Default -> theLabThemeDynamicLightColorScheme(context)
                    AppTheme.Blue -> blueThemeDynamicLightColorScheme(context)
                    AppTheme.Red -> redThemeDynamicLightColorScheme(context)
                    AppTheme.Green -> greenThemeDynamicLightColorScheme(context)
                    else -> dynamicLightColorScheme(context)
                }
            }
        }

        darkTheme -> when (theme) {
            AppTheme.Default -> TheLabThemeDarkColorScheme
            AppTheme.Blue -> BlueThemeDarkColorScheme
            AppTheme.Red -> RedThemeDarkColorScheme
            AppTheme.Green -> GreenThemeDarkColorScheme
        }

        else -> when (theme) {
            AppTheme.Default -> TheLabThemeLightColorScheme
            AppTheme.Blue -> BlueThemeLightColorScheme
            AppTheme.Red -> RedThemeLightColorScheme
            AppTheme.Green -> GreenThemeLightColorScheme
        }
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(
                (view.context as Activity).window,
                view
            ).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme.switch(),
        typography = Typography,
        content = content
    )
}