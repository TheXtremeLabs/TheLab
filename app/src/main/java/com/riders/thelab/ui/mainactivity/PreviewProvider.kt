package com.riders.thelab.ui.mainactivity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.AppBuilder
import com.riders.thelab.ui.colors.ColorActivity
import com.riders.thelab.ui.compose.ComposeActivity
import com.riders.thelab.ui.screenshot.ScreenShotActivity

fun getListOfApps(context: Context): List<App> = listOf(
    // Colors
    AppBuilder
        .withId(0L)
        .withActivityTitle(context.getString(R.string.activity_title_colors))
        .withActivityDescription("Change color programmatically...")
        .withActivityIcon(
            getDrawableFromIntResource(
                context,
                R.drawable.logo_colors
            )
        )
        .withActivityClass(ColorActivity::class.java)
        .withActivityDate("2015/01/20")
        .build(),
    // Jetpack Compose
    AppBuilder
        .withId(28L)
        .withActivityTitle(context.getString(R.string.activity_title_compose))
        .withActivityDescription("Jetpack Compose is Android’s modern toolkit for building native UI with less code, powerful tools, and intuitive Kotlin APIs...")
        .withActivityIcon(
            getDrawableFromIntResource(
                context,
                R.drawable.jetpack_compose
            )
        )
        .withActivityClass(ComposeActivity::class.java)
        .withActivityDate("2023/01/29")
        .build(),

    // Music Recognition
    AppBuilder
        .withId(30L)
        .withActivityTitle("Screen Shot")
        .withActivityDescription("Screen Shot the device display programmatically...")
        .withActivityIcon(
            getDrawableFromIntResource(
                context,
                R.drawable.ic_fullscreen
            )
        )
        .withActivityClass(ScreenShotActivity::class.java)
        .withActivityDate("2021/10/13")
        .build()
)

class AppPreviewProvider(val context: Context) : PreviewParameterProvider<App> {
    override val values: Sequence<App>
        get() = sequenceOf(
            // Colors
            AppBuilder
                .withId(0L)
                .withActivityTitle(context.getString(R.string.activity_title_colors))
                .withActivityDescription("Change color programmatically...")
                .withActivityIcon(
                    getDrawableFromIntResource(
                        context,
                        R.drawable.logo_colors
                    )
                )
                .withActivityClass(ColorActivity::class.java)
                .withActivityDate("2015/01/20")
                .build(),
            // Jetpack Compose
            AppBuilder
                .withId(28L)
                .withActivityTitle(context.getString(R.string.activity_title_compose))
                .withActivityDescription("Jetpack Compose is Android’s modern toolkit for building native UI with less code, powerful tools, and intuitive Kotlin APIs...")
                .withActivityIcon(
                    getDrawableFromIntResource(
                        context,
                        R.drawable.jetpack_compose
                    )
                )
                .withActivityClass(ComposeActivity::class.java)
                .withActivityDate("2023/01/29")
                .build(),

            // Music Recognition
            AppBuilder
                .withId(30L)
                .withActivityTitle("Screen Shot")
                .withActivityDescription("Screen Shot the device display programmatically...")
                .withActivityIcon(
                    getDrawableFromIntResource(
                        context,
                        R.drawable.ic_fullscreen
                    )
                )
                .withActivityClass(ScreenShotActivity::class.java)
                .withActivityDate("2021/10/13")
                .build()
        )
}
class AppListPreviewProvider(val context: Context) : PreviewParameterProvider<List<App>> {
    override val values: Sequence<List<App>>
        get() = sequenceOf( getListOfApps(context))
}

private fun getDrawableFromIntResource(context: Context, redId: Int): Drawable {
    return ContextCompat.getDrawable(context, redId)!!
}