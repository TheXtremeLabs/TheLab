package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.previewprovider.AppPreviewProvider
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.app.App

@DevicePreviews
@Composable
fun App(@PreviewParameter(AppPreviewProvider::class) item: App) {

    /* Convert our Image Resource into a Bitmap */
    val bitmap = remember {
        UIManager.drawableToBitmap(item.appDrawableIcon!!)
    }

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "app_icon",
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (item.appName != null) item.appName!! else item.appTitle!!
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = item.appDescription ?: item.appVersion!!,
                        maxLines = 1
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