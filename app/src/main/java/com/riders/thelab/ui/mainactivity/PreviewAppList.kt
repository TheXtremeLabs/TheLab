package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.previewprovider.AppPreviewProvider
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_background
import com.riders.thelab.core.compose.utils.findActivity
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.app.App
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun App(@PreviewParameter(AppPreviewProvider::class) item: App) {

    val context = LocalContext.current

    // App item variables
    val title = if (item.appName != null) item.appName!! else item.appTitle!!
    val description = item.appDescription ?: item.appVersion!!
    /* Convert our Image Resource into a Bitmap */
    val bitmap = remember {
        UIManager.drawableToBitmap(item.appDrawableIcon!!)
    }

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onClick = { (context.findActivity() as MainActivity).launchApp(item) },
            colors = CardDefaults.cardColors(containerColor = md_theme_dark_background),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = md_theme_dark_background),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        bitmap = when (title) {
                            stringResource(id = R.string.activity_title_palette) -> {
                                UIManager.addGradientToImageView(context, bitmap).asImageBitmap()
                            }
                            else -> {
                                bitmap.asImageBitmap()
                            }
                        },
                        contentDescription = "app_icon",
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(modifier = Modifier.fillMaxWidth(), text = title)

                    Text(
                        modifier = Modifier.fillMaxWidth(), text = description, maxLines = 1
                    )
                }

                Icon(
                    modifier = Modifier.weight(0.3f),
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "arrow_right_icon"
                )
            }
        }
    }
}
