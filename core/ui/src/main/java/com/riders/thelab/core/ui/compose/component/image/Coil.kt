package com.riders.thelab.core.ui.compose.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImagePainter
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun CoilImage(
    theme: AppTheme,
    darkTheme: Boolean,
    painterState: AsyncImagePainter.State,
    @DrawableRes errorResId: Int = R.drawable.logo_colors,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            when (painterState) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterState.painter,
                        contentDescription = contentDescription ?: "coil_image_composable",
                        contentScale = contentScale
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(errorResId),
                        contentDescription = "coil_error_image",
                        contentScale = contentScale
                    )
                }

                else -> {
                    CircularProgressIndicator()
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
private fun PreviewCoilImage() {
    TheLabTheme(theme = AppTheme.Default) {
        CoilImage(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            painterState = AsyncImagePainter.State.Loading(null),
            errorResId = R.drawable.logo_colors
        )
    }
}