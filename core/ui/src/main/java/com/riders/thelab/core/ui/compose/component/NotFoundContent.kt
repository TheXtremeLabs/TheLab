package com.riders.thelab.core.ui.compose.component

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import java.util.Locale


///////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////
@Composable
fun NoContentFound(
    theme: AppTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    message: String? = null,
    errorImageResId: Int? = null,
    lottieResId: Int? = null,
    basicAction: (() -> Unit)? = null,
    customAction: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    val context = LocalContext.current

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        content?.invoke() ?: run {
            Column(
                modifier = Modifier.fillMaxWidth(.8f),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Lottie(
                    modifier = Modifier.fillMaxSize(.5f),
                    rawResId = lottieResId
                        ?: com.riders.thelab.core.ui.R.raw.error_rolling_dark_theme
                )

                Text(
                    text = message ?: "Unable to play splashscreen video.",
                    color = MaterialTheme.colorScheme.onSurface
                )

                Button(
                    modifier = Modifier.fillMaxWidth(.6f),
                    onClick = basicAction ?: { (context.findActivity() as Activity).finish() }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = com.riders.thelab.core.ui.R.string.action_exit).uppercase(
                            Locale.getDefault()
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewNoContentFound(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        NoContentFound(theme = appTheme, darkTheme = isSystemInDarkTheme())
    }
}

@DevicePreviews
@Composable
private fun PreviewNoItemFound(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    NoItemFound(theme = appTheme, darkTheme = true)
}






