package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.previewprovider.AppPreviewProvider
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.app.App

@DevicePreviews
@Composable
fun WhatsNew(@PreviewParameter(AppPreviewProvider::class) item: App) {

    val context = LocalContext.current

    /* Convert our Image Resource into a Bitmap */
    val bitmap = remember {
        UIManager.drawableToBitmap(item.appDrawableIcon!!)
    }
    /* Create the Palette, pass the bitmap to it */
    val palette = remember {
        bitmap.let { Palette.from(it).generate() }
    }

    /* Get the dark vibrant swatch */
    val darkVibrantSwatch = palette.darkVibrantSwatch

    TheLabTheme {
        Card(
            modifier = Modifier
                .width(width = dimensionResource(id = R.dimen.max_card_image_width))
                .height(height = dimensionResource(id = R.dimen.max_card_image_height))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2F)
                        .background(darkVibrantSwatch?.rgb?.let { Color(it) } ?: Color.Transparent)
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .padding(32.dp),
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "app_icon",
                        contentScale = ContentScale.Fit
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .padding(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = if (item.appName != null) item.appName!! else item.appTitle!!
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = if (item.appDescription != null) item.appDescription!! else ""
                    )
                }
            }
        }
    }
}

@Composable
fun WhatsNewList(viewModel: MainActivityViewModel) {
    TheLabTheme {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(viewModel.whatsNewAppList.value) { whatsNewItem ->
                WhatsNew(item = whatsNewItem)
            }
        }
    }
}
