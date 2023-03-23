package com.riders.thelab.core.compose.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.R
import com.riders.thelab.TheLabApplication
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.local.model.app.AppBuilder
import com.riders.thelab.ui.colors.ColorActivity
import com.riders.thelab.ui.googlemlkit.LiveBarcodeScanningActivity
import com.riders.thelab.ui.lottie.LottieActivity
import com.riders.thelab.utils.AppBuilderUtils

class AppPreviewProvider : PreviewParameterProvider<App> {
    val context = TheLabApplication.getInstance().getContext()

    override val values: Sequence<App>
        get() = sequenceOf(
            AppBuilder
                .withId(0L)
                .withActivityTitle("Colors")
                .withActivityDescription("Change color programmatically...")
                .withActivityIcon(
                    AppBuilderUtils.getDrawableFromIntResource(
                        context,
                        R.drawable.logo_colors
                    )
                )
                .withActivityClass(ColorActivity::class.java)
                .withActivityDate("2015/01/20")
                .build()
        )
}

class AppListPreviewProvider : PreviewParameterProvider<List<App>> {

    val context = TheLabApplication.getInstance().getContext()

    override val values: Sequence<List<App>>
        get() = sequenceOf(
            listOf(
                AppBuilder
                    .withId(0L)
                    .withActivityTitle("Colors")
                    .withActivityDescription("Change color programmatically...")
                    .withActivityIcon(
                        AppBuilderUtils.getDrawableFromIntResource(
                            context,
                            R.drawable.logo_colors
                        )
                    )
                    .withActivityClass(ColorActivity::class.java)
                    .withActivityDate("2015/01/20")
                    .build(),

                // Lottie
                AppBuilder
                    .withId(26L)
                    .withActivityTitle("Lottie")
                    .withActivityDescription("Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations and renders them natively on mobile!...")
                    .withActivityIcon(
                        AppBuilderUtils.getDrawableFromIntResource(
                            context,
                            R.drawable.ic_lottie_icon
                        )
                    )
                    .withActivityClass(LottieActivity::class.java)
                    .withActivityDate("2021/09/21")
                    .build(),

                // Google ML Kit - Live Barcode
                AppBuilder
                    .withId(31L)
                    .withActivityTitle("Google Ml Kit Live Barcode")
                    .withActivityDescription("ML Kit brings Google’s machine learning expertise to mobile developers in a powerful and easy-to-use package...")
                    .withActivityIcon(
                        AppBuilderUtils.getDrawableFromIntResource(
                            context,
                            R.drawable.logo_mlkit
                        )
                    )
                    .withActivityClass(LiveBarcodeScanningActivity::class.java)
                    .withActivityDate("2022/01/28")
                    .build()
            )
        )
}