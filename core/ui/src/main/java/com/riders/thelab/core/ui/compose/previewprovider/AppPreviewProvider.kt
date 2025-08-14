package com.riders.thelab.core.ui.compose.previewprovider

import android.content.Context
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.utils.getDrawableFromIntResource

class LocalAppPreviewProvider(private val context: Context) : PreviewParameterProvider<App> {
    override val values: Sequence<App>
        get() = sequenceOf<App>(
            // Colors
            LocalApp(
                localId = 0,
                localTitle = context.getString(R.string.activity_title_colors),
                localDescription = "Change color programmatically...",
                localDrawableIcon = context.getDrawableFromIntResource(R.drawable.logo_colors),
                localActivity = null,
                localDate = "2015/01/20"
            ),
            // Jetpack Compose
            LocalApp(
                localId = 1,
                localTitle = context.getString(R.string.activity_title_compose),
                localDescription = "Jetpack Compose is Android’s modern toolkit for building native UI with less code, powerful tools, and intuitive Kotlin APIs...",
                localDrawableIcon = context.getDrawableFromIntResource(R.drawable.jetpack_compose),
                localActivity = null,
                localDate = "2023/01/29"
            ),

            // Screen shot
            LocalApp(
                localId = 2,
                localTitle = context.getString(R.string.activity_title_screen_shot),
                localDescription = "Screen Shot the device display programmatically...",
                localDrawableIcon = context.getDrawableFromIntResource(R.drawable.ic_fullscreen),
                localActivity = null,
                localDate = "2021/10/13"
            )
        )
}