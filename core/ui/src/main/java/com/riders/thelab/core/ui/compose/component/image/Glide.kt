package com.riders.thelab.core.ui.compose.component.image

import android.widget.ImageView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@Composable
fun GlideCompose(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    resId: Int? = null,
    imageUrl: String? = null,
    gifUrl: String? = null
) {
    val context = LocalContext.current
    val view = remember { ImageView(context) }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(modifier = modifier) {
            DisposableEffect(context) {
                when {
                    null != resId -> {
                        // Load image with Glide library
                        Glide.with(context)
                            .load(resId)
                            .into(view)
                    }

                    null != imageUrl -> {
                        // Load image with Glide library
                        Glide.with(context)
                            .load(imageUrl)
                            .into(view)
                    }

                    null != gifUrl -> {
                        // Load Gif with Glide library
                        Glide.with(context)
                            .asGif()
                            .load(gifUrl)
                            .into(view)

                    }
                }
                onDispose {
                    // Cleanup when the composable is disposed
                    Glide.with(context).clear(view)
                }
            }

            // Wrap the ImageView with Compose's View composable
            AndroidView(factory = { view })
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewGlideImage() {
    TheLabTheme(theme = AppTheme.Default) {
        GlideCompose(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            imageUrl = "https://miro.medium.com/v2/resize:fit:1400/format:webp/1*R8o1lOKI4wjaQsmH9LQUOA.png"
        )
    }
}