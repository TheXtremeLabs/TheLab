package com.riders.thelab.central.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import com.riders.thelab.central.BuildConfig
import com.riders.thelab.central.R
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun CentralPackageItem(
    theme: AppTheme,
    darkTeme: Boolean,
    packageItem: PackageApp,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current

    TheLabTheme(theme = theme, darkTheme = darkTeme) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(194.dp)
                .padding(16.dp),
            onClick = { uiEvent.invoke(UiEvent.OnPackageClicked(packageItem = packageItem)) }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
            ) {
                Image(
                    bitmap = packageItem.drawableIcon?.toBitmapOrNull()
                        ?.asImageBitmap()
                        ?: UIManager.getDrawableAsBitmap(
                            context = context,
                            drawableRedId = com.riders.thelab.core.ui.R.drawable.ic_lab_6_lab
                        )!!.asImageBitmap(),
                    contentDescription = "package_drawable_image"
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = packageItem.name,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewCentralPackageItem(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val context = LocalContext.current

    TheLabTheme(theme = appTheme) {
        CentralPackageItem(
            theme = appTheme,
            darkTeme = isSystemInDarkTheme(),
            packageItem = PackageApp(
                name = stringResource(R.string.app_name),
                drawableIcon = UIManager.getDrawable(
                    context = context,
                    drawableResId = com.riders.thelab.core.ui.R.drawable.ic_lab_6_lab
                )!!,
                version = BuildConfig.VERSION_NAME,
                packageName = BuildConfig.APPLICATION_ID
            )
        ) {}
    }
}