package com.riders.thelab.core.ui.compose.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.riders.composemultiplethemes.core.compose.color.md_green_theme_light_primary
import com.riders.composemultiplethemes.core.compose.color.md_green_theme_light_secondary
import com.riders.composemultiplethemes.core.compose.color.md_green_theme_light_surfaceVariant
import com.riders.thelab.core.ui.compose.color.md_blue_theme_light_primary
import com.riders.thelab.core.ui.compose.color.md_blue_theme_light_secondary
import com.riders.thelab.core.ui.compose.color.md_blue_theme_light_surfaceVariant
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_primary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_secondary
import com.riders.thelab.core.ui.compose.color.md_red_theme_light_surfaceVariant
import com.riders.thelab.core.ui.compose.color.md_theme_light_primary
import com.riders.thelab.core.ui.compose.color.md_theme_light_secondary
import com.riders.thelab.core.ui.compose.color.md_theme_light_surfaceVariant
import kotlin.reflect.full.isSubclassOf

/**
 * This class represents the themes
 */
@Stable
sealed class AppTheme(
    val name: String,
    val primaryColor: Color,
    val primaryVariant: Color,
    val secondaryColor: Color
) {
    data object Default : AppTheme(
        "Default",
        md_theme_light_primary,
        md_theme_light_surfaceVariant,
        md_theme_light_secondary
    )

    data object Blue : AppTheme(
        "Blue",
        md_blue_theme_light_primary,
        md_blue_theme_light_surfaceVariant,
        md_blue_theme_light_secondary
    )

    data object Green : AppTheme(
        "Green",
        md_green_theme_light_primary,
        md_green_theme_light_surfaceVariant,
        md_green_theme_light_secondary
    )

    data object Red : AppTheme(
        "Red",
        md_red_theme_light_primary,
        md_red_theme_light_surfaceVariant,
        md_red_theme_light_secondary
    )
}


///////////////////////////////////////////////////////////////////////
//
// Extensions
//
///////////////////////////////////////////////////////////////////////
inline fun <reified T : AppTheme> valueOf(value: String): T? {
    return T::class.nestedClasses
        .filter { clazz -> clazz.isSubclassOf(T::class) }
        .map { clazz -> clazz.objectInstance }
        .filterIsInstance<T>()
        .associateBy { it.name }[value]
}

inline fun <reified T : AppTheme> values(): List<T> =
    T::class.nestedClasses
        .filter { clazz -> clazz.isSubclassOf(T::class) }
        .map { clazz -> clazz.objectInstance }
        .filterIsInstance<T>()