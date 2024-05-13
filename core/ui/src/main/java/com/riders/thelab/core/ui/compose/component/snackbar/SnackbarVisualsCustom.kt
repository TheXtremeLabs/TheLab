package com.riders.thelab.core.ui.compose.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
data class SnackbarVisualsCustom(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    // You can add custom things here (for you it's an icon)
    @DrawableRes val drawableRes: Int? = null,
    val icon: ImageVector? = null,
    val containerColor: Color = Color.Black
) : SnackbarVisuals {
    constructor() : this(message = "")
}
