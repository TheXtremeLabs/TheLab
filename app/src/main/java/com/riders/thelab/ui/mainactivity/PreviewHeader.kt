package com.riders.thelab.ui.mainactivity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.utils.toBitmap
import com.riders.thelab.utils.AppBuilderUtils


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun Header(
    theme: AppTheme,
    darkTheme: Boolean,
    whatsNewList: List<LocalApp>,
    isKeyboardVisible: Boolean,
    pagerAutoScroll: Boolean,
    onSearchClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    val config = LocalConfiguration.current
    val toolbarHeight = 112.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = config.screenWidthDp.dp / 2 - toolbarHeight)
            .padding(top = if (!isKeyboardVisible) 24.dp else 56.dp)
    ) {

        /*Box(
            modifier = Modifier.defaultMinSize(minHeight = config.screenWidthDp.dp / 2 - toolbarHeight)
        ) {*/
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
            visible = if (LocalInspectionMode.current) true else !isKeyboardVisible,
            enter = expandVertically() + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = shrinkVertically(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    visibilityThreshold = null
                )
            ) + fadeOut()
        ) {

            WhatsNewContent(
                theme = theme,
                darkTheme = darkTheme,
                whatsNewList = whatsNewList,
                pagerAutoScroll = pagerAutoScroll,
                onSearchClicked = onSearchClicked,
                onSettingsClicked = onSettingsClicked
            )
        }
        //

        Spacer(modifier = Modifier.size(32.dp))

        Text(
            text = stringResource(id = R.string.app_list_placeholder),
            fontSize = 18.sp,
            fontWeight = FontWeight.W600,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = stringResource(id = R.string.app_list_detail_placeholder),
            fontSize = 18.sp,
            fontWeight = FontWeight.Thin,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(16.dp))
    }

}

///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
fun PreviewHeader(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val context = LocalContext.current
    val appList = AppBuilderUtils.buildActivities(context).take(3).let {
        it.map { localApp ->
            val bitmap: Bitmap? = when (localApp.appDrawableIcon) {
                is BitmapDrawable -> {
                    (localApp.appDrawableIcon as BitmapDrawable).bitmap as Bitmap
                }

                is VectorDrawable -> {
                   (localApp.appDrawableIcon as VectorDrawable).toBitmap()
                }

                else -> {
                    null
                }
            }

            LocalApp(
                localApp.id,
                localApp.appTitle!!,
                localApp.appDescription!!,
                null,
                localApp.appActivity,
                localApp.appDate!!
            ).apply {
                this.bitmap = bitmap
            }
        }
    }

    Header(
        theme = appTheme,
        darkTheme = isSystemInDarkTheme(),
        whatsNewList = appList,
        isKeyboardVisible = true,
        pagerAutoScroll = true,
        onSearchClicked = { }
    ) {}
}