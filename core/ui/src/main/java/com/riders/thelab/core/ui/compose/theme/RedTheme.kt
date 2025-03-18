package com.riders.thelab.core.ui.compose.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_background
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_error
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_errorContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_inverseOnSurface
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_inversePrimary
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_inverseSurface
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onBackground
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onError
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onErrorContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onPrimary
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onPrimaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onSecondary
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onSecondaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onSurface
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onSurfaceVariant
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onTertiary
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_onTertiaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_outline
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_outlineVariant
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_primary
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_primaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_scrim
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_secondary
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_secondaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surface
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceBright
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceContainerHigh
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceContainerHighest
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceContainerLow
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceContainerLowest
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceDim
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_surfaceVariant
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_tertiary
import com.riders.thelab.core.ui.compose.color.md_red_theme_dark_tertiaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_background
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_error
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_errorContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_inverseOnSurface
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_inversePrimary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_inverseSurface
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onBackground
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onError
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onErrorContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onPrimary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onPrimaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onSecondary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onSecondaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onSurface
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onSurfaceVariant
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onTertiary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_onTertiaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_outline
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_outlineVariant
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_primary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_primaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_scrim
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_secondary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_secondaryContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surface
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceBright
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceContainer
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceContainerHigh
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceContainerHighest
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceContainerLow
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceContainerLowest
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceDim
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceVariant
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_tertiary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_tertiaryContainer

/**
 * Linked to RedThemeColors file
 */
val RedThemeLightColorScheme = lightColorScheme(
    primary = md_red_theme_light_primary,
    onPrimary = md_red_theme_light_onPrimary,
    primaryContainer = md_red_theme_light_primaryContainer,
    onPrimaryContainer = md_red_theme_light_onPrimaryContainer,
    secondary = md_red_theme_light_secondary,
    onSecondary = md_red_theme_light_onSecondary,
    secondaryContainer = md_red_theme_light_secondaryContainer,
    onSecondaryContainer = md_red_theme_light_onSecondaryContainer,
    tertiary = md_red_theme_light_tertiary,
    onTertiary = md_red_theme_light_onTertiary,
    tertiaryContainer = md_red_theme_light_tertiaryContainer,
    onTertiaryContainer = md_red_theme_light_onTertiaryContainer,
    error = md_red_theme_light_error,
    onError = md_red_theme_light_onError,
    errorContainer = md_red_theme_light_errorContainer,
    onErrorContainer = md_red_theme_light_onErrorContainer,
    background = md_red_theme_light_background,
    onBackground = md_red_theme_light_onBackground,
    surface = md_red_theme_light_surface,
    onSurface = md_red_theme_light_onSurface,
    surfaceVariant = md_red_theme_light_surfaceVariant,
    onSurfaceVariant = md_red_theme_light_onSurfaceVariant,
    outline = md_red_theme_light_outline,
    outlineVariant = md_red_theme_light_outlineVariant,
    scrim = md_red_theme_light_scrim,
    inverseSurface = md_red_theme_light_inverseSurface,
    inverseOnSurface = md_red_theme_light_inverseOnSurface,
    inversePrimary = md_red_theme_light_inversePrimary,
    surfaceDim = md_red_theme_light_surfaceDim,
    surfaceBright = md_red_theme_light_surfaceBright,
    surfaceContainerLowest = md_red_theme_light_surfaceContainerLowest,
    surfaceContainerLow = md_red_theme_light_surfaceContainerLow,
    surfaceContainer = md_red_theme_light_surfaceContainer,
    surfaceContainerHigh = md_red_theme_light_surfaceContainerHigh,
    surfaceContainerHighest = md_red_theme_light_surfaceContainerHighest,
)

@RequiresApi(Build.VERSION_CODES.S)
fun redThemeDynamicLightColorScheme(context: Context): ColorScheme =
    dynamicLightColorScheme(context).copy(
        primary = md_red_theme_light_primary,
        onPrimary = md_red_theme_light_onPrimary,
        primaryContainer = md_red_theme_light_primaryContainer,
        onPrimaryContainer = md_red_theme_light_onPrimaryContainer,
        secondary = md_red_theme_light_secondary,
        onSecondary = md_red_theme_light_onSecondary,
        secondaryContainer = md_red_theme_light_secondaryContainer,
        onSecondaryContainer = md_red_theme_light_onSecondaryContainer,
        tertiary = md_red_theme_light_tertiary,
        onTertiary = md_red_theme_light_onTertiary,
        tertiaryContainer = md_red_theme_light_tertiaryContainer,
        onTertiaryContainer = md_red_theme_light_onTertiaryContainer,
        error = md_red_theme_light_error,
        onError = md_red_theme_light_onError,
        errorContainer = md_red_theme_light_errorContainer,
        onErrorContainer = md_red_theme_light_onErrorContainer,
        background = md_red_theme_light_background,
        onBackground = md_red_theme_light_onBackground,
        surface = md_red_theme_light_surface,
        onSurface = md_red_theme_light_onSurface,
        surfaceVariant = md_red_theme_light_surfaceVariant,
        onSurfaceVariant = md_red_theme_light_onSurfaceVariant,
        outline = md_red_theme_light_outline,
        outlineVariant = md_red_theme_light_outlineVariant,
        scrim = md_red_theme_light_scrim,
        inverseSurface = md_red_theme_light_inverseSurface,
        inverseOnSurface = md_red_theme_light_inverseOnSurface,
        inversePrimary = md_red_theme_light_inversePrimary,
        surfaceDim = md_red_theme_light_surfaceDim,
        surfaceBright = md_red_theme_light_surfaceBright,
        surfaceContainerLowest = md_red_theme_light_surfaceContainerLowest,
        surfaceContainerLow = md_red_theme_light_surfaceContainerLow,
        surfaceContainer = md_red_theme_light_surfaceContainer,
        surfaceContainerHigh = md_red_theme_light_surfaceContainerHigh,
        surfaceContainerHighest = md_red_theme_light_surfaceContainerHighest,
    )

val RedThemeDarkColorScheme = darkColorScheme(
    primary = md_red_theme_dark_primary,
    onPrimary = md_red_theme_dark_onPrimary,
    primaryContainer = md_red_theme_dark_primaryContainer,
    onPrimaryContainer = md_red_theme_dark_onPrimaryContainer,
    secondary = md_red_theme_dark_secondary,
    onSecondary = md_red_theme_dark_onSecondary,
    secondaryContainer = md_red_theme_dark_secondaryContainer,
    onSecondaryContainer = md_red_theme_dark_onSecondaryContainer,
    tertiary = md_red_theme_dark_tertiary,
    onTertiary = md_red_theme_dark_onTertiary,
    tertiaryContainer = md_red_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_red_theme_dark_onTertiaryContainer,
    error = md_red_theme_dark_error,
    onError = md_red_theme_dark_onError,
    errorContainer = md_red_theme_dark_errorContainer,
    onErrorContainer = md_red_theme_dark_onErrorContainer,
    background = md_red_theme_dark_background,
    onBackground = md_red_theme_dark_onBackground,
    surface = md_red_theme_dark_surface,
    onSurface = md_red_theme_dark_onSurface,
    surfaceVariant = md_red_theme_dark_surfaceVariant,
    onSurfaceVariant = md_red_theme_dark_onSurfaceVariant,
    outline = md_red_theme_dark_outline,
    outlineVariant = md_red_theme_dark_outlineVariant,
    scrim = md_red_theme_dark_scrim,
    inverseSurface = md_red_theme_dark_inverseSurface,
    inverseOnSurface = md_red_theme_dark_inverseOnSurface,
    inversePrimary = md_red_theme_dark_inversePrimary,
    surfaceDim = md_red_theme_dark_surfaceDim,
    surfaceBright = md_red_theme_dark_surfaceBright,
    surfaceContainerLowest = md_red_theme_dark_surfaceContainerLowest,
    surfaceContainerLow = md_red_theme_dark_surfaceContainerLow,
    surfaceContainer = md_red_theme_dark_surfaceContainer,
    surfaceContainerHigh = md_red_theme_dark_surfaceContainerHigh,
    surfaceContainerHighest = md_red_theme_dark_surfaceContainerHighest,
)

@RequiresApi(Build.VERSION_CODES.S)
fun redThemeDynamicDarkColorScheme(context: Context) = dynamicDarkColorScheme(context).copy(
    primary = md_red_theme_dark_primary,
    onPrimary = md_red_theme_dark_onPrimary,
    primaryContainer = md_red_theme_dark_primaryContainer,
    onPrimaryContainer = md_red_theme_dark_onPrimaryContainer,
    secondary = md_red_theme_dark_secondary,
    onSecondary = md_red_theme_dark_onSecondary,
    secondaryContainer = md_red_theme_dark_secondaryContainer,
    onSecondaryContainer = md_red_theme_dark_onSecondaryContainer,
    tertiary = md_red_theme_dark_tertiary,
    onTertiary = md_red_theme_dark_onTertiary,
    tertiaryContainer = md_red_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_red_theme_dark_onTertiaryContainer,
    error = md_red_theme_dark_error,
    onError = md_red_theme_dark_onError,
    errorContainer = md_red_theme_dark_errorContainer,
    onErrorContainer = md_red_theme_dark_onErrorContainer,
    background = md_red_theme_dark_background,
    onBackground = md_red_theme_dark_onBackground,
    surface = md_red_theme_dark_surface,
    onSurface = md_red_theme_dark_onSurface,
    surfaceVariant = md_red_theme_dark_surfaceVariant,
    onSurfaceVariant = md_red_theme_dark_onSurfaceVariant,
    outline = md_red_theme_dark_outline,
    outlineVariant = md_red_theme_dark_outlineVariant,
    scrim = md_red_theme_dark_scrim,
    inverseSurface = md_red_theme_dark_inverseSurface,
    inverseOnSurface = md_red_theme_dark_inverseOnSurface,
    inversePrimary = md_red_theme_dark_inversePrimary,
    surfaceDim = md_red_theme_dark_surfaceDim,
    surfaceBright = md_red_theme_dark_surfaceBright,
    surfaceContainerLowest = md_red_theme_dark_surfaceContainerLowest,
    surfaceContainerLow = md_red_theme_dark_surfaceContainerLow,
    surfaceContainer = md_red_theme_dark_surfaceContainer,
    surfaceContainerHigh = md_red_theme_dark_surfaceContainerHigh,
    surfaceContainerHighest = md_red_theme_dark_surfaceContainerHighest,
)