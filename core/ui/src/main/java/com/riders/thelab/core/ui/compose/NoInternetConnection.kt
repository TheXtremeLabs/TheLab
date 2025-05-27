package com.riders.thelab.core.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun NoInternetConnection(
    modifier: Modifier = Modifier,
    theme: AppTheme = AppTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    errorImageResId: Int? = R.drawable.logo_testing,
    lottieResId: Int? = R.raw.lottie_hot_coffee_loading,
    message: String? = null,
    action: (() -> Unit)? = null
) {
    check(null != errorImageResId || null != lottieResId) {
        "Either errorImageResId or lottieResId must be provided"
    }

    /*if(null == errorImageResId && null == lottieResId) {
        throw IllegalArgumentException("Either errorImageResId or lottieResId must be provided")
    }*/

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Surface(modifier = modifier) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (null != errorImageResId && null == lottieResId) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(.65f)
                            .weight(1.5f),
                        painter = painterResource(errorImageResId),
                        contentDescription = "Error image resource",
                        contentScale = ContentScale.Fit
                    )
                }

                if (null != lottieResId && null == errorImageResId) {
                    Lottie(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                            .weight(1.5f),
                        rawResId = lottieResId
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = message ?: "No item found",
                        style = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    AnimatedVisibility(visible = null != action) {
                        Button(onClick = action!!) {
                            Text(text = "Retry")
                        }
                    }
                }
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
private fun PreviewNoInternetConnectionWithImage(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        NoInternetConnection(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier= Modifier.fillMaxSize(),
            message = "No song item found",
            lottieResId = null,
            action = null
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewNoInternetConnectionWithLottie(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        NoInternetConnection(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier= Modifier.fillMaxSize(),
            message = stringResource(R.string.network_status_disconnected),
            errorImageResId = null,
            action = null
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewNoInternetConnectionWithoutAction(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        NoInternetConnection(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier= Modifier.fillMaxSize(),
            message = stringResource(R.string.network_status_disconnected),
            lottieResId = null,
            action = null
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewNoInternetConnectionWithAction(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        NoInternetConnection(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier= Modifier.fillMaxSize(),
            message = "No song item found",
            lottieResId = null,
            action = {}
        )
    }
}